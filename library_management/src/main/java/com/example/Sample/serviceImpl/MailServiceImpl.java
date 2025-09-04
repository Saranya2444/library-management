package com.example.Sample.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.Sample.service.MailService;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final String SENDER_EMAIL = "saranyamurugavel2004@gmail.com";

    @Override
    public void sendEmail(String receiverEmailId, String subject, String message) {
        MimeMessage preparator = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(preparator, true);
            helper.setFrom(SENDER_EMAIL);
            helper.setTo(receiverEmailId);
            helper.setText(message, false); // HTML enabled
            helper.setSubject(subject);
            mailSender.send(preparator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
