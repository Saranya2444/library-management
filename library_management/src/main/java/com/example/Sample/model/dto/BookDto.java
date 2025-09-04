package com.example.Sample.model.dto;

import java.time.LocalDateTime;

import com.example.Sample.util.DateDeserializeUtil;
import com.example.Sample.util.DateSerializeUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.NotBlank;

public class BookDto {

    private Integer bookId;
    @NotBlank(message = "Title should not be null or empty")
    private String title;
    @NotBlank(message="Author name should not be null or empty")
    private String author;
    @NotBlank(message="Language should not be null or empty")

    private String language;

    @JsonDeserialize(using = DateDeserializeUtil.class)
    @JsonSerialize(using = DateSerializeUtil.class)   
    private LocalDateTime bookRegisterDate;
    

    @JsonDeserialize(using = DateDeserializeUtil.class)
    @JsonSerialize(using = DateSerializeUtil.class) 
    private LocalDateTime bookDeletedDate;

    private String bookStatus;
    private Integer borrowedCount;
    private String genre;
    private Double price;

    @JsonDeserialize(using = DateDeserializeUtil.class)
    @JsonSerialize(using = DateSerializeUtil.class) 
    private LocalDateTime createdAt;

    @JsonDeserialize(using = DateDeserializeUtil.class)
    @JsonSerialize(using = DateSerializeUtil.class) 
    private LocalDateTime updatedAt;

    

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDateTime getBookRegisterDate() {
        return bookRegisterDate;
    }

    public void setBookRegisterDate(LocalDateTime bookRegisterDate) {
        this.bookRegisterDate = bookRegisterDate;
    }

    public LocalDateTime getBookDeletedDate() {
        return bookDeletedDate;
    }

    public void setBookDeletedDate(LocalDateTime bookDeletedDate) {
        this.bookDeletedDate = bookDeletedDate;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Integer getBorrowedCount() {
        return borrowedCount;
    }

    public void setBorrowedCount(Integer borrowedCount) {
        this.borrowedCount = borrowedCount;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}