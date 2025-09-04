package com.example.Sample.service;

import java.util.List;

import com.example.Sample.model.dto.BookDto;

public interface BookService {
    String addBook(BookDto dto);
    String getBookById(Integer id);
    List<String> getBooksByTitle(String title);
    List<String> getBooksByAuthor(String author);
    String getAllBooks();   
    String updateBookStatus(Integer id, String status);
	List<String> getBooksByStatus(String status); 

}
