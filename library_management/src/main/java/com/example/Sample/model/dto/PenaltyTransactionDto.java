package com.example.Sample.model.dto;

import java.time.LocalDateTime;

import com.example.Sample.util.DateTimeDeserializeUtil;
import com.example.Sample.util.DateTimeSerializeUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class PenaltyTransactionDto {

    private Integer penaltyId;
    private Integer rentalTransactionId;
    private Double amount;
    private String reason;
    private String status;
    @JsonSerialize(using = DateTimeSerializeUtil.class)
    @JsonDeserialize(using = DateTimeDeserializeUtil.class) 
    private LocalDateTime createdAt;
    @JsonSerialize(using = DateTimeSerializeUtil.class)
    @JsonDeserialize(using = DateTimeDeserializeUtil.class) 
    private LocalDateTime paidAt;
    private String payment_mode ;


    public Integer getPenaltyId() {
        return penaltyId;
    }

    public void setPenaltyId(Integer penaltyId) {
        this.penaltyId = penaltyId;
    }

    public Integer getRentalTransactionId() {
        return rentalTransactionId;
    }

    public void setRentalTransactionId(Integer rentalTransactionId) {
        this.rentalTransactionId = rentalTransactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
    
    public void setPaymentMode(String payment_mode) {
    	this.payment_mode=payment_mode;}
    public String getPaymentMode() {
    	return payment_mode;}

    }

