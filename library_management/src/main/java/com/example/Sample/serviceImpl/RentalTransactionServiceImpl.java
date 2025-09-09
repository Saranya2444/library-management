package com.example.Sample.serviceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.Sample.dao.BookDao;
import com.example.Sample.dao.MemberDao;
import com.example.Sample.dao.RentalTransactionDao;
import com.example.Sample.model.entity.Book;
import com.example.Sample.model.entity.Member;
import com.example.Sample.model.entity.RentalTransaction;
import com.example.Sample.service.RentalTransactionService;
import com.example.Sample.util.LibraryUtil;

@Service
public class RentalTransactionServiceImpl implements RentalTransactionService {

    @Autowired
    private RentalTransactionDao rentalTransactionDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private AllServiceMail mailService;
    
    @Override
    public String checkoutBook(Integer memberId, Integer bookId, LocalDateTime dueDate) {
        Member member = memberDao.findById(memberId);
        Book book = bookDao.findById(bookId);

        if (member == null) {
            return LibraryUtil.MEMBER_NOT_FOUND_WITH_ID + memberId;
        }
        if (book == null) {
            return LibraryUtil.BOOK_NOT_FOUND_WITH_ID + bookId;
        }
        if (!LibraryUtil.STATUS_AVAILABLE.equalsIgnoreCase(book.getBookStatus())) {
            return LibraryUtil.BOOK_NOT_AVAILABLE;
        }
        LocalDateTime borrowedDate = LocalDateTime.now();
        LocalDateTime dueDate1 = borrowedDate.plusDays(10);

        RentalTransaction transaction = new RentalTransaction();
        transaction.setMember(member);
        transaction.setBook(book);
        transaction.setBorrowedDate(borrowedDate);
        transaction.setReturnDate(dueDate1);
        transaction.setRentalStatus(LibraryUtil.STATUS_BORROWED);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        rentalTransactionDao.save(transaction);

        // update book status
        book.setBookStatus(LibraryUtil.STATUS_BORROWED);
        book.setBorrowedCount(book.getBorrowedCount() + 1);
        book.setUpdatedAt(LocalDateTime.now());
        bookDao.save(book);

        return  String.format(LibraryUtil.BOOK_BORROWED_SUCCESS, book.getTitle(), member.getMemberName());
    }

    @Override
    public String returnBook(Integer transactionId) {
        RentalTransaction transaction = rentalTransactionDao.findById(transactionId);
       
        if (transaction == null) {
            return LibraryUtil.TRANSACTION_NOT_FOUND;
        }

        if ("Returned".equalsIgnoreCase(transaction.getRentalStatus())) {
            return LibraryUtil.BOOK_ALREADY_RETURNED;
        }

        transaction.setActualReturnDate(LocalDateTime.now());
        transaction.setRentalStatus(LibraryUtil.STATUS_RETURNED);
        transaction.setUpdatedAt(LocalDateTime.now());
        rentalTransactionDao.save(transaction);

        
        Book book = transaction.getBook();
        book.setBookStatus(LibraryUtil.STATUS_AVAILABLE);
        book.setUpdatedAt(LocalDateTime.now());
        bookDao.save(book);

        return String.format(LibraryUtil.BOOK_RETURNED_SUCCESS, book.getTitle());
    }
    
    @Scheduled(cron = "0 45 16 * * ?", zone = "Asia/Kolkata")    public void updateFinesDaily() {
        rentalTransactionDao.updateFinesForOverdueBooks();
        
    }
    
    @Override
    public List<RentalTransaction> getTransactionsByMember(Integer memberId, LocalDateTime start, LocalDateTime end) {
        return rentalTransactionDao.findByMemberAndPeriod(memberId, start, end);
    }



    @Override
    public long getCurrentBorrowedCount(Integer memberId) {
        return rentalTransactionDao.countCurrentBorrowedByMember(memberId, LocalDateTime.now());
    }

    @Override
    public long getOverdueCount(Integer memberId) {
        return rentalTransactionDao.countOverdueByMember(memberId, LocalDateTime.now());
    }

    @Override
    public String getFavoriteAuthorsAndAvailability(Integer memberId) {
        List<Object[]> rows = rentalTransactionDao.topAuthorsForMember(memberId, 3);
        if (rows.isEmpty())
              return String.format(LibraryUtil.NO_RENTAL_HISTORY, memberId);
        
        StringBuilder sb = new StringBuilder("Top authors for member " + memberId + ":\n");
        for (Object[] row : rows) {
            String author = (String) row[0];
            Long count = (Long) row[1];
            sb.append("- ").append(author).append(" (rented ").append(count).append(" times)\n");

            // available books by this author
            var available = bookDao.findByAuthor(author);
            if (available.isEmpty()) {
                sb.append("  Available now: none\n");
            } else {
                sb.append("  Available now:\n");
                available.forEach(b ->
                        sb.append("   --->").append(b.getTitle())
                          .append(" | Lang: ").append(b.getLanguage())
                          .append(" | Genre: ").append(b.getGenre())
                          .append(" | Price: ").append(b.getPrice())
                          .append("\n"));
            }
        }
        return sb.toString();
    }

    
    @Override
    public String renewBook(Integer transactionId) {
        RentalTransaction transaction = rentalTransactionDao.findById(transactionId);
        if (transaction == null) {
            return  LibraryUtil.TRANSACTION_NOT_FOUND;
        }
        
        if (transaction.getActualReturnDate() != null) {
            return LibraryUtil.TRANSACTION_ALREADY_RETURNED;
        }
        LocalDateTime now = LocalDateTime.now();

        if (transaction.getReturnDate().isBefore(now)) {
            return LibraryUtil.TRANSACTION_ALREADY_OVERDUE;
        }

        
        if (transaction.isRenewed()) {
            return LibraryUtil.TRANSACTION_ALREADY_RENEWED;
        }
        
        transaction.setReturnDate(transaction.getReturnDate().plusDays(5));
        transaction.setRenewed(true);
        transaction.setUpdatedAt(LocalDateTime.now());

        rentalTransactionDao.save(transaction);
        return LibraryUtil.BOOK_RENEWED_SUCCESS;
    }
    
    @Override
    @Scheduled(cron = "0 15 11 * * ?")
    public void sendOverdueNotifications() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime threeDaysLater = today.plusDays(3);

        List<RentalTransaction> upcomingDueBooks =
                rentalTransactionDao.findDueInNextThreeDays(today, threeDaysLater);

        for (RentalTransaction rental : upcomingDueBooks) {
            Member member = rental.getMember();
            Book book = rental.getBook();

            long daysLeft = ChronoUnit.DAYS.between(today.toLocalDate(), rental.getReturnDate().toLocalDate());

            mailService.sendOverdueReminderMail(
            	    member.getMemberEmail(),
            	    member.getMemberName(),
            	    book,
            	    rental.getReturnDate().toLocalDate().toString(),
            	    daysLeft
            	);

        }
    }

}



    

