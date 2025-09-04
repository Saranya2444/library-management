package com.example.Sample.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        mailService.sendEmail(email, subject, content);
    }

    // Profile Update Mail
    public void sendUpdateMail(String email, String name) {
        String subject = "Profile Updated Successfully";
        String content = "Dear " + name + ",\n\n" +
                         "Your profile details have been updated successfully.\n" +
                         
                         "Best Regards,\nLibrary Management Team.";
        mailService.sendEmail(email, subject, content);
    }

    // Membership Activation Mail
    public void sendActivationMail(String email, String name) {
        String subject = "Membership Activated ";
        String content = "Dear " + name + ",\n\n" +
                         "Your membership has been successfully activated.\n" +
        		
                         "Best Regards,\nLibrary Management Team.";
        mailService.sendEmail(email, subject, content);
    }
}