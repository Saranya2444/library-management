package com.example.Sample.dao;

import java.util.List;

import com.example.Sample.model.entity.Book;

public interface BookDao {
    Book save(Book book);

	boolean existsByTitleAndAuthor(String title, String author);
	Book findById(Integer id);
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findAll();
    Book updateBookStatus(Integer id, String status);

	List<Book> findByStatus(String status);
    
    
    
}
