package com.example.Sample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Sample.model.dto.PenaltyTransactionDto;
import com.example.Sample.model.entity.PenaltyTransaction;
import com.example.Sample.service.PenaltyTransactionService;

@RestController
@RequestMapping("/penalties")
public class PenaltyTransactionController {

    @Autowired
    private PenaltyTransactionService penaltyService;

    @PostMapping("/add")
    public String addPenalty(@RequestBody PenaltyTransactionDto dto) {
        return penaltyService.addPenalty(dto);  
    }

    @PutMapping("/pay/{penaltyId}")
    public String payPenalty(@PathVariable Integer penaltyId,@RequestParam String paymentMode) {
        return penaltyService.payPenalty(penaltyId,paymentMode);
    }
    
    @GetMapping("/rental/{rentalId}")
    public String getByRental(@PathVariable Integer rentalId) {
        return penaltyService.getPenaltiesByRentalId(rentalId);
    }
    
    @PutMapping("/update-status")
    public String updatePenaltyStatus(@RequestBody PenaltyTransactionDto dto) {
        
        return penaltyService.updateStatus(dto);
    }
}

