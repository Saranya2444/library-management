package com.example.Sample.util;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import com.example.Sample.model.entity.Book;
import com.example.Sample.model.entity.PenaltyTransaction;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class PdfUtil {

    public static byte[] generatePaymentReceipt(Book book, PenaltyTransaction penalty) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            document.add(new Paragraph("Payment Details:"));
            document.add(new Paragraph("---------------------------"));
            document.add(new Paragraph("Book Title: " + book.getTitle()));
            document.add(new Paragraph("Author: " + book.getAuthor()));
            document.add(new Paragraph("Amount Paid: ₹" + penalty.getAmount()));
            document.add(new Paragraph("Payment Mode: " + penalty.getPaymentMode()));
            document.add(new Paragraph("Payment Date: " +
                    penalty.getPaidAt().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))));
            document.close();

            return baos.toByteArray();
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
