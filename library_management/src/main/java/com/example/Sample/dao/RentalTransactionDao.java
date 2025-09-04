package com.example.Sample.dao;

import java.time.LocalDateTime;
import java.util.List;

import com.example.Sample.model.entity.RentalTransaction;

public interface RentalTransactionDao {
	RentalTransaction save(RentalTransaction rentalTransaction);

	RentalTransaction findById(Integer transactionId);
    void updateFinesForOverdueBooks();
    List<RentalTransaction> findByMemberAndPeriod(Integer memberId, LocalDateTime start, LocalDateTime end);
    long countCurrentBorrowedByMember(Integer memberId, LocalDateTime now);
    long countOverdueByMember(Integer memberId, LocalDateTime now);

    
    List<Object[]> topAuthorsForMember(Integer memberId, int limit);
}
