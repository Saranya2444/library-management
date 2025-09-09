package com.example.Sample.model.dto;

import java.time.LocalDateTime;

import com.example.Sample.util.DateTimeDeserializeUtil;
import com.example.Sample.util.DateTimeSerializeUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class RentalTransactionDto {

    private Integer transactionId;
    private Integer memberId;
    private Integer bookId;
    @JsonSerialize(using = DateTimeSerializeUtil.class)
    @JsonDeserialize(using = DateTimeDeserializeUtil.class) 
    private LocalDateTime borrowedDate;
    @JsonSerialize(using = DateTimeSerializeUtil.class)
    @JsonDeserialize(using = DateTimeDeserializeUtil.class) 
    private LocalDateTime returnDate;
    @JsonSerialize(using = DateTimeSerializeUtil.class)
    @JsonDeserialize(using = DateTimeDeserializeUtil.class) 
    private LocalDateTime actualReturnDate;
    private String rentalStatus;
    private Double fineAccrued;
    private boolean renewed;

    
    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDateTime borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDateTime getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDateTime actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public String getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(String rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public Double getFineAccrued() {
        return fineAccrued;
    }

    public void setFineAccrued(Double fineAccrued) {
        this.fineAccrued = fineAccrued;
    }
    

    public boolean isRenewed() { return renewed; }
    public void setRenewed(boolean renewed) { this.renewed = renewed; }
}
