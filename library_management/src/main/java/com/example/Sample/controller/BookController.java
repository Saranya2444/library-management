package com.example.Sample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Sample.model.dto.BookDto;
import com.example.Sample.service.BookService;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public String addBook(@Valid @RequestBody BookDto dto,BindingResult result) {
        
    	
    	if (result.hasErrors()) {
            
            return result.getAllErrors().get(0).getDefaultMessage();
        }
    	return bookService.addBook(dto);
        
        
        
    }
    
    @GetMapping("/{id}")
    public String getBookById(@PathVariable Integer id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/title/{title}")
    public List<String> getBooksByTitle(@PathVariable String title) {
        return bookService.getBooksByTitle(title);
    }

    @GetMapping("/author/{author}")
    public List<String> getBooksByAuthor(@PathVariable String author) {
        return bookService.getBooksByAuthor(author);
    }
    
    @GetMapping("/all")
    public String getAllBooks() {
        return bookService.getAllBooks();
    }
    
    @PutMapping("/{id}/status")
    public String updateBookStatus(@PathVariable Integer id, @RequestParam String status) {
        return bookService.updateBookStatus(id, status);
    }
    
    
    
    @GetMapping("/status/{status}")
    public List<String> getBooksByStatus(@PathVariable String status) {
        return bookService.getBooksByStatus(status);
    }

    
}
