package com.example.Sample.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Sample.model.dto.RentalTransactionDto;
import com.example.Sample.model.entity.RentalTransaction;
import com.example.Sample.service.RentalTransactionService;

@RestController
@RequestMapping("/rentals")
public class RentalTransactionController {
	
	
	 @Autowired
	    private RentalTransactionService rentalService;
	 
	 
	@PostMapping("/checkout")
	public String checkoutBook(@RequestParam Integer memberId,
	                           @RequestParam Integer bookId) {
	    
		return rentalService.checkoutBook(memberId, bookId, null);
	}

	@PutMapping("/return/{transactionId}")
    public String returnBook(@PathVariable Integer transactionId) {
        return rentalService.returnBook(transactionId);
    }
	@GetMapping("/member/{memberId}/period")
	public List<RentalTransaction> byMemberAndPeriod(@PathVariable Integer memberId,
	                                                 @RequestParam("start") String start,
	                                                 @RequestParam("end") String end) {
	    LocalDateTime s = LocalDateTime.parse(start);
	    LocalDateTime e = LocalDateTime.parse(end);
	    
	    
	    return rentalService.getTransactionsByMember(memberId, s, e);
	}

	 
	 @GetMapping("/member/{memberId}/current-count")
	    public String currentBorrowed(@PathVariable Integer memberId) {
	        return "Current borrowed count: " + rentalService.getCurrentBorrowedCount(memberId);
	    }
	 
	 @GetMapping("/member/{memberId}/overdue-count")
	    public String overdueCount(@PathVariable Integer memberId) {
	        return "not return count " + rentalService.getOverdueCount(memberId);
	    }
	 
	 @GetMapping(value="/member/{memberId}/favorite-authors", produces = "text/html")
	    public String favoriteAuthors(@PathVariable Integer memberId) {
	        return rentalService.getFavoriteAuthorsAndAvailability(memberId);
	    }
	 
	 @PutMapping("/{transactionId}/renew")
	    public String renewBook(@PathVariable Integer transactionId) {
	        return rentalService.renewBook(transactionId);
	    }

}
