package com.example.Sample.service;

public interface MailService {
	void sendEmail(String receiverEmailId, String subject, String message,byte[] attachment, String fileName);

	
}
