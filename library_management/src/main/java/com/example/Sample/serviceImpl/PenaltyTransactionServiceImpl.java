package com.example.Sample.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.Sample.dao.PenaltyTransactionDao;
import com.example.Sample.dao.RentalTransactionDao;
import com.example.Sample.model.dto.PenaltyTransactionDto;
import com.example.Sample.model.entity.PenaltyTransaction;
import com.example.Sample.model.entity.RentalTransaction;
import com.example.Sample.service.PenaltyTransactionService;
import com.example.Sample.util.LibraryUtil;
@Service
public class PenaltyTransactionServiceImpl implements PenaltyTransactionService {

    @Autowired
    private PenaltyTransactionDao penaltyDao;

    @Autowired
    private RentalTransactionDao rentalDao;

    @Override
    public String addPenalty(PenaltyTransactionDto dto) {
        
        RentalTransaction rental = rentalDao.findById(dto.getRentalTransactionId());
        PenaltyTransaction existingPenalty = penaltyDao
                .findPendingPenaltyByRentalId(dto.getRentalTransactionId());
        if (rental == null) {
            return LibraryUtil.PENALTY_RENTAL_NOT_FOUND;
        }

        double fineAccrued = rental.getFineAccrued() != null ? rental.getFineAccrued() : 0.0;
        double bookPrice = rental.getBook().getPrice();
        double amount;

        if (dto.getReason().toLowerCase().contains(LibraryUtil.REASON_MISSING)) {
            amount = fineAccrued + bookPrice;
        } else if (dto.getReason().toLowerCase().contains(LibraryUtil.REASON_DAMAGE)) {
            amount = fineAccrued + (bookPrice / 2);
        } else {
            amount = fineAccrued;
        }
        if (existingPenalty != null) {
        	
        	return LibraryUtil.PENALTY_ALREADY_EXISTS;
        }
        
        
        
        PenaltyTransaction penalty = new PenaltyTransaction();
        penalty.setRentalTransaction(rental);
        penalty.setReason(dto.getReason());
        penalty.setAmount(amount);
        penalty.setStatus(LibraryUtil.STATUS_PENDING);
        penalty.setCreatedAt(LocalDateTime.now());
        penaltyDao.save(penalty);

        return LibraryUtil.PENALTY_ADDED_SUCCESS;
    }

    @Override
    public String payPenalty(Integer penaltyId,String paymentMode) {
        PenaltyTransaction penalty = penaltyDao.findById(penaltyId).orElse(null);

        if (penalty == null) {
            return LibraryUtil.PENALTY_NOT_FOUND;
        }
        RentalTransaction rental = penalty.getRentalTransaction();
        if (rental.getActualReturnDate() == null) {
            return  LibraryUtil.PENALTY_CANNOT_PAY_UNTIL_RETURN;
        }

        
        if (!paymentMode.equalsIgnoreCase(LibraryUtil.PAYMENT_MODE_CASH) 
                && !paymentMode.equalsIgnoreCase(LibraryUtil.PAYMENT_MODE_UPI) 
                && !paymentMode.equalsIgnoreCase(LibraryUtil.PAYMENT_MODE_ONLINE) 
                && !paymentMode.equalsIgnoreCase(LibraryUtil.PAYMENT_MODE_CARD)) {
            return LibraryUtil.PENALTY_INVALID_PAYMENT_MODE;
        }
        penalty.setStatus(LibraryUtil.STATUS_PAID);
        penalty.setPaidAt(LocalDateTime.now());
        penalty.setPaymentMode(paymentMode);
        penaltyDao.save(penalty);

        return LibraryUtil.PENALTY_PAYMENT_SUCCESS;
    }
    
    @Override
    public String getPenaltiesByRentalId(Integer rentalId) {
        List<PenaltyTransaction> penalties = penaltyDao.findByRentalTransactionId(rentalId);

        if (penalties.isEmpty()) {
            return LibraryUtil.PENALTY_NO_PENALTIES_FOUND + rentalId;
        }

        StringBuilder sb = new StringBuilder("Penalties for rental ID " + rentalId + ":\n");
        for (PenaltyTransaction p : penalties) {
            sb.append(" - Penalty ID: ").append(p.getPenaltyId())
              .append(" | Amount: ").append(p.getAmount())
              .append(" | Reason: ").append(p.getReason())
              .append(" | Status: ").append(p.getStatus())
              .append("\n");
        }
        return sb.toString();
    }
    @Override
    @Scheduled(cron = "0 30 17 * * ?")
    public void generateOverduePenalties() {
        List<RentalTransaction> overdueRentals = penaltyDao.findOverdueRentals();
        

        for (RentalTransaction rental : overdueRentals) {
            PenaltyTransaction existing = penaltyDao.findPendingPenaltyByRentalId(rental.getTransactionId());
            if (existing != null) continue; 

            PenaltyTransaction penalty = new PenaltyTransaction();
            penalty.setRentalTransaction(rental);
            penalty.setAmount(rental.getFineAccrued());
            penalty.setReason(null);
            penalty.setStatus(LibraryUtil.STATUS_PENDING);
            penalty.setCreatedAt(LocalDateTime.now());
           

            penaltyDao.save(penalty);
            
        }
       
    }
    
    
    @Override
    public String updateStatus(PenaltyTransactionDto dto) {
        
        Optional<PenaltyTransaction> optionalPenalty = penaltyDao.findById(dto.getPenaltyId());
        if (optionalPenalty.isEmpty()) {
            return LibraryUtil.PENALTY_NOT_FOUND + " with ID: " + dto.getPenaltyId();
        }

        PenaltyTransaction penalty = optionalPenalty.get();

        
        RentalTransaction rental = penalty.getRentalTransaction();
        

        double fineAccrued = rental.getFineAccrued() != null ? rental.getFineAccrued() : 0.0;
        double bookPrice = rental.getBook().getPrice();
        double amount;

        if (dto.getReason().toLowerCase().contains(LibraryUtil.REASON_MISSING)) {
            amount = fineAccrued + bookPrice;
        } else if (dto.getReason().toLowerCase().contains(LibraryUtil.REASON_DAMAGE)) {
            amount = fineAccrued + (bookPrice / 2);
        } else {
            amount = fineAccrued;
        }
        penalty.setReason(dto.getReason());
        penalty.setAmount(amount);
       

        penaltyDao.save(penalty);

        return LibraryUtil.PENALTY_UPDATED_SUCCESS;
    }
}

