package com.example.Sample.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.Sample.model.dto.RentalTransactionDto;
import com.example.Sample.model.entity.RentalTransaction;

public interface RentalTransactionService {
	String checkoutBook(Integer memberId, Integer bookId, LocalDateTime dueDate);
	String returnBook(Integer transactionId);
	

    
	List<RentalTransaction> getTransactionsByMember(Integer memberId, LocalDateTime start, LocalDateTime end);
    
    long getCurrentBorrowedCount(Integer memberId);

    
    long getOverdueCount(Integer memberId);

   
    String getFavoriteAuthorsAndAvailability(Integer memberId);
	String renewBook(Integer transactionId);
   
}
