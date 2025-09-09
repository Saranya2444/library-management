package com.example.Sample.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.Sample.dao.PenaltyTransactionDao;
import com.example.Sample.dao.RentalTransactionDao;
import com.example.Sample.model.dto.PenaltyTransactionDto;
import com.example.Sample.model.entity.Book;
import com.example.Sample.model.entity.Member;
import com.example.Sample.model.entity.PenaltyTransaction;
import com.example.Sample.model.entity.RentalTransaction;
import com.example.Sample.service.PenaltyTransactionService;
import com.example.Sample.util.LibraryUtil;
import com.example.Sample.util.PdfUtil;
@Service
public class PenaltyTransactionServiceImpl implements PenaltyTransactionService {

    @Autowired
    private PenaltyTransactionDao penaltyDao;

    @Autowired
    private RentalTransactionDao rentalDao;
    
    @Autowired
    private AllServiceMail allServiceMail;
   

    @Override
    public String addPenalty(PenaltyTransactionDto dto) {
        
        RentalTransaction rental = rentalDao.findById(dto.getRentalTransactionId());
        PenaltyTransaction existingPenalty = penaltyDao
                .findPendingPenaltyByRentalId(dto.getRentalTransactionId());
        if (rental == null) {
            return LibraryUtil.PENALTY_RENTAL_NOT_FOUND;
        }

        double fineAccrued = rental.getFineAccrued() != null ? rental.getFineAccrued() : 0.0;
        double bookPrice = rental.getBook().getPrice();
        double amount;

        if (dto.getReason().toLowerCase().contains(LibraryUtil.REASON_MISSING)) {
            amount = fineAccrued + bookPrice;
        } else if (dto.getReason().toLowerCase().contains(LibraryUtil.REASON_DAMAGE)) {
            amount = fineAccrued + (bookPrice / 2);
        } else {
            amount = fineAccrued;
        }
        if (existingPenalty != null) {
        	
        	return LibraryUtil.PENALTY_ALREADY_EXISTS;
        }
        
        
        
        PenaltyTransaction penalty = new PenaltyTransaction();
        penalty.setRentalTransaction(rental);
        penalty.setReason(dto.getReason());
        penalty.setAmount(amount);
        penalty.setStatus(LibraryUtil.STATUS_PENDING);
        penalty.setCreatedAt(LocalDateTime.now());
        penaltyDao.save(penalty);

        return LibraryUtil.PENALTY_ADDED_SUCCESS;
    }

    @Override
    public String payPenalty(Integer penaltyId,String paymentMode) {
        PenaltyTransaction penalty = penaltyDao.findById(penaltyId).orElse(null);

        if (penalty == null) {
            return LibraryUtil.PENALTY_NOT_FOUND;
        }
        RentalTransaction rental = penalty.getRentalTransaction();
        if (rental.getActualReturnDate() == null) {
            return  LibraryUtil.PENALTY_CANNOT_PAY_UNTIL_RETURN;
        }

        
        if (!paymentMode.equalsIgnoreCase(LibraryUtil.PAYMENT_MODE_CASH) 
                && !paymentMode.equalsIgnoreCase(LibraryUtil.PAYMENT_MODE_UPI) 
                && !paymentMode.equalsIgnoreCase(LibraryUtil.PAYMENT_MODE_ONLINE) 
                && !paymentMode.equalsIgnoreCase(LibraryUtil.PAYMENT_MODE_CARD)) {
            return LibraryUtil.PENALTY_INVALID_PAYMENT_MODE;
        }
        penalty.setStatus(LibraryUtil.STATUS_PAID);
        penalty.setPaidAt(LocalDateTime.now());
        penalty.setPaymentMode(paymentMode);
        penaltyDao.save(penalty);
        
        byte[] pdfBytes = PdfUtil.generatePaymentReceipt(rental.getBook(), penalty);
        Member member = rental.getMember();
        Book book = rental.getBook();
        if (member != null && book != null) {
        	allServiceMail.sendPenaltyPaymentMail(
        		    member.getMemberEmail(),
        		    member.getMemberName(),
        		    book,
        		    penalty,
        		    pdfBytes
        		);
        }

        return LibraryUtil.PENALTY_PAYMENT_SUCCESS;
    }
    
    @Override
    public String getPenaltiesByRentalId(Integer rentalId) {
        List<PenaltyTransaction> penalties = penaltyDao.findByRentalTransactionId(rentalId);

        if (penalties.isEmpty()) {
            return LibraryUtil.PENALTY_NO_PENALTIES_FOUND + rentalId;
        }

        StringBuilder sb = new StringBuilder("Penalties for rental ID " + rentalId + ":\n");
        for (PenaltyTransaction p : penalties) {
            sb.append(" - Penalty ID: ").append(p.getPenaltyId())
              .append(" | Amount: ").append(p.getAmount())
              .append(" | Reason: ").append(p.getReason())
              .append(" | Status: ").append(p.getStatus())
              .append("\n");
        }
        return sb.toString();
    }
    @Override
    @Scheduled(cron = "0 0 16 * * ?")
    
    
    //Scheduled(cron = "0 * * * * ?")
    public void generateOverduePenalties() {
    	
    	
    	
    	//System.out.println(">>> Cron job started at " + LocalDateTime.now());
        List<RentalTransaction> overdueRentals = penaltyDao.findOverdueRentals();
        //System.out.println(">>> Overdue rentals found: " + overdueRentals.size());


        for (RentalTransaction rental : overdueRentals) {
            PenaltyTransaction existing = penaltyDao.findPendingPenaltyByRentalId(rental.getTransactionId());
            if (existing != null) {
               // System.out.println(">>> Penalty already exists for rental " + rental.getTransactionId());

            	continue; 
            }
            

            PenaltyTransaction penalty = new PenaltyTransaction();
            penalty.setRentalTransaction(rental);
            penalty.setAmount(rental.getFineAccrued());
            penalty.setReason(null);
            penalty.setStatus(LibraryUtil.STATUS_PENDING);
            penalty.setCreatedAt(LocalDateTime.now());
            Member member = rental.getMember();
            Book book = rental.getBook();
            
            if (member != null && book != null) {
            allServiceMail.sendOverduePenaltyMail(
            	    member.getMemberEmail(),
            	    member.getMemberName(),
            	    book,
            	    rental.getFineAccrued(),
            	    rental.getReturnDate().toString()
            	);
            }
            else {
                System.out.println(">>> Missing member/book info for rental " + rental.getTransactionId());
            }
            penaltyDao.save(penalty);
           // System.out.println(">>> Penalty saved for rental " + rental.getTransactionId());

            
        }
       
    }
    
    
    @Override
    public String updateStatus(PenaltyTransactionDto dto) {
        
        Optional<PenaltyTransaction> optionalPenalty = penaltyDao.findById(dto.getPenaltyId());
        if (optionalPenalty.isEmpty()) {
            return LibraryUtil.PENALTY_NOT_FOUND + " with ID: " + dto.getPenaltyId();
        }

        PenaltyTransaction penalty = optionalPenalty.get();

        
        RentalTransaction rental = penalty.getRentalTransaction();
        

        double fineAccrued = rental.getFineAccrued() != null ? rental.getFineAccrued() : 0.0;
        double bookPrice = rental.getBook().getPrice();
        double amount;
        
        String reason = dto.getReason().toLowerCase();
        boolean sendMail = false;

        if (reason.contains(LibraryUtil.REASON_MISSING)) {
            amount = fineAccrued + bookPrice;
            penalty.setReason(dto.getReason());
            penalty.setAmount(amount);
            sendMail = true;   
        } else if (reason.contains(LibraryUtil.REASON_DAMAGE)) {
            amount = fineAccrued + (bookPrice / 2);
            penalty.setReason(dto.getReason());
            penalty.setAmount(amount);
            sendMail = true;   
        } else {
            amount = fineAccrued;
            penalty.setAmount(amount);
            penalty.setReason(dto.getReason());
            sendMail = false;  
        }
        
       

        penaltyDao.save(penalty);
        
        if (sendMail) {
            Member member = rental.getMember();
            Book book = rental.getBook();
            if (member != null && book != null) {
                allServiceMail.sendOverduePenaltyMail(
                    member.getMemberEmail(),
                    member.getMemberName(),
                    book,
                    penalty.getAmount(),   
                    rental.getReturnDate().toString()
                );
            }
        }


        return LibraryUtil.PENALTY_UPDATED_SUCCESS;
    }
    
    
   
}

