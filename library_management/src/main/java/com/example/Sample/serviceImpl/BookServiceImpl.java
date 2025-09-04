package com.example.Sample.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Sample.dao.BookDao;
import com.example.Sample.model.dto.BookDto;
import com.example.Sample.model.entity.Book;
import com.example.Sample.service.BookService;
import com.example.Sample.util.LibraryUtil;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;
   

    @Override
    public String addBook(BookDto dto) {
    	
    	if (bookDao.existsByTitleAndAuthor(dto.getTitle(), dto.getAuthor())) {
    		return LibraryUtil.BOOK_ALREADY_EXISTS;        }
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setLanguage(dto.getLanguage());
        book.setGenre(dto.getGenre());
        book.setPrice(dto.getPrice());
        book.setBookRegisterDate(LocalDateTime.now());
        book.setBookStatus(LibraryUtil.STATUS_AVAILABLE);
        book.setBorrowedCount(0);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());

        bookDao.save(book);
        return LibraryUtil.BOOK_ADDED_SUCCESS;
    }
    
    @Override
    public String getBookById(Integer id) {
        Book book = bookDao.findById(id);
        if (book == null) {
            return LibraryUtil.BOOK_NOT_FOUND + id;
        }

        return formatBook(book);
    }

    @Override
    public List<String> getBooksByTitle(String title) {
        return bookDao.findByTitle(title)
                .stream()
                .map(book -> formatBook(book))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getBooksByAuthor(String author) {
        return bookDao.findByAuthor(author)
                .stream()
                .map(book -> formatBook(book))
                .collect(Collectors.toList());
    }
    
    @Override
    public String getAllBooks() {   
        List<Book> books = bookDao.findAll();
        if (books.isEmpty()) {
            return LibraryUtil.NO_BOOKS_AVAILABLE;
        }
        return books.stream().map(this::formatBook).collect(Collectors.joining("\n"));
    }
    
    @Override
    public String updateBookStatus(Integer id, String status) {
        
        
    	 if (!LibraryUtil.ALLOWED_BOOK_STATUSES.contains(status)) {
             return LibraryUtil.INVALID_BOOK_STATUS;
         }

         Book updatedBook = bookDao.updateBookStatus(id, status);
         if (updatedBook == null) {
             return LibraryUtil.BOOK_NOT_FOUND + id;
         }

         return LibraryUtil.BOOK_STATUS_UPDATED;
     }
    
    
    @Override
    public List<String> getBooksByStatus(String status) {
        
        List<Book> books = bookDao.findByStatus(status);

        if (books.isEmpty()) {
            return List.of(LibraryUtil.NO_BOOKS_FOUND_BY_STATUS + status);
        }
        return books.stream()
                    .map(this::formatBook)
                    .collect(Collectors.toList());
    }

    
    private String formatBook(Book book) {
        return "Book: " + book.getTitle() +
               " | Author: " + book.getAuthor() +
               " | Language: " + book.getLanguage() +
               " | Borrowed Count: " + book.getBorrowedCount() +
               " | Genre: " + book.getGenre() +
               " | Price: " + book.getPrice();
    }

}
