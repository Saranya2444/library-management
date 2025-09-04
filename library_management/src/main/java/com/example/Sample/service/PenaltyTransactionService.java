package com.example.Sample.service;

import java.util.List;

import com.example.Sample.model.dto.PenaltyTransactionDto;
import com.example.Sample.model.entity.PenaltyTransaction;

public interface PenaltyTransactionService {

	

	String payPenalty(Integer penaltyId,String paymentMode);

	String addPenalty(PenaltyTransactionDto dto);
   String getPenaltiesByRentalId(Integer rentalTransactionId);
  void generateOverduePenalties();

  String updateStatus(PenaltyTransactionDto dto); 
}
