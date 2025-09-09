package com.example.Sample.serviceImpl;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Sample.model.entity.Book;
import com.example.Sample.model.entity.PenaltyTransaction;
import com.example.Sample.service.MailService;

@Service
public class AllServiceMail {

    @Autowired
    private MailService mailService;

    // Registration Mail
    public void sendRegistrationMail(String email, String name) {
        String subject = "Welcome to Our Library!";
        String content = "Dear " + name + ",\n\n" +
                         "Thank you for registering with our Library System.\n" +
                         
                         "Best Regards,\nLibrary Management Team.";
        mailService.sendEmail(email, subject, content,null,null);
    }

    // Profile Update Mail
    public void sendUpdateMail(String email, String name) {
        String subject = "Profile Updated Successfully";
        String content = "Dear " + name + ",\n\n" +
                         "Your profile details have been updated successfully.\n" +
                         
                         "Best Regards,\nLibrary Management Team.";
        mailService.sendEmail(email, subject, content,null,null);
    }

    // Membership Activation Mail
    public void sendActivationMail(String email, String name) {
        String subject = "Membership Activated ";
        String content = "Dear " + name + ",\n\n" +
                         "Your membership has been successfully activated.\n" +
        		
                         "Best Regards,\nLibrary Management Team.";
        mailService.sendEmail(email, subject, content,null,null);
    }
    
    // membership DeActive mail
    public void sendDeActivationMail(String email, String name) {
        String subject = "Membership Deactivated ";
        String content = "Dear " + name + ",\n\n" +
                         "Your library membership is currently deactivated. We kindly remind you to activate your account to continue enjoying access to our library services.\n" +
        		
                         "Best Regards,\nLibrary Management Team.";
        mailService.sendEmail(email, subject, content,null,null);
    }
  
    
    public void sendOverduePenaltyMail(String email, String name, Book book, double fine, String dueDate) {
        String subject = "Overdue Book Notice – Penalty Accrued";
        String content = "Dear " + name + ",\n\n" +
                "Our records show that you have not returned the book before the due date (" + dueDate + ").\n\n" +
                "Book Details:\n" +
                "- Title: " + book.getTitle() + "\n" +
                "- Author: " + book.getAuthor() + "\n\n" +
                "-Current Fine: ₹" + fine + "\n\n" +
                "Please note: A fine of ₹10 per day will continue to accumulate until you return the book.\n\n" +
                "Kindly return the book as soon as possible to avoid further penalties.\n\n" +
                "Best Regards,\nLibrary Management Team.";
        
        mailService.sendEmail(email, subject, content,null,null);
    }
    
    public void sendPenaltyPaymentMail(String email, String name, Book book, PenaltyTransaction penalty, byte[] pdfBytes) {
        String subject = "Payment Confirmation - Library System";
        String content = "Dear " + name + ",\n\n" +
                "Your payment has been successfully processed.\n\n" +
                "Details:\n" +
                "- Book Title: " + book.getTitle() + "\n" +
                "- Author: " + book.getAuthor() + "\n" +
                "- Amount Paid: ₹" + penalty.getAmount() + "\n" +
                "- Payment Mode: " + penalty.getPaymentMode() + "\n" +
                "- Payment Date: " + penalty.getPaidAt().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")) + "\n\n" +
                "Please find the attached receipt for your records.\n\n" +
                "Best Regards,\nLibrary Management Team.";

        mailService.sendEmail(email, subject, content, pdfBytes, "Payment_Receipt.pdf");
    }
    
    
    public void sendOverdueReminderMail(String email, String name, Book book, String dueDate, long daysLeft) {
        String subject = "Reminder: Book Due Soon";
        String content = "Dear " + name + ",\n\n" +
                "This is a friendly reminder that the book you borrowed is due on (" + dueDate + ").\n\n" +
                "Book Details:\n" +
                "- Title: " + book.getTitle() + "\n" +
                "- Author: " + book.getAuthor() + "\n\n";

        if (daysLeft > 0) {
            content += "You have " + daysLeft + " day(s) left to return the book.\n\n";
        } else {
            content += "The book is already overdue. Please return it immediately to avoid penalties.\n\n";
        }

        content += "Best Regards,\nLibrary Management Team.";

        mailService.sendEmail(email, subject, content,null,null);
    }
}

    
    
