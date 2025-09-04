package com.example.Sample.dao;


import java.util.List;
import java.util.Optional;

import com.example.Sample.model.entity.PenaltyTransaction;
import com.example.Sample.model.entity.RentalTransaction;

public interface PenaltyTransactionDao {
    PenaltyTransaction save(PenaltyTransaction penalty);

	Optional<PenaltyTransaction> findById(Integer penaltyId);


	PenaltyTransaction findPendingPenaltyByRentalId(Integer rentalId);
	List<PenaltyTransaction> findByRentalTransactionId(Integer rentalTransactionId);
    List<RentalTransaction> findOverdueRentals();


}
