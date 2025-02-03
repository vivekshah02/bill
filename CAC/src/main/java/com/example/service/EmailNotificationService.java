package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.model.Bill;
import com.example.repository.BillingRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailNotificationService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private BillingRepository billingRepository;
    @Autowired
    private TemplateEngine templateEngine; // Thymeleaf TemplateEngine
    
   
  //bill email service
 /*   public void sendBillEmail(Bill bill) throws MessagingException {
        String emailBody = generateBillEmailBody(bill);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //email address
        //helper.setTo(bill.getAppointment().getPatientEmail());
        helper.setTo("anshitagupta2277@gmail.com");
        helper.setSubject("Your Bill Details");
        helper.setText(emailBody, true);

        // Optional: Attach a PDF or other file
        // helper.addAttachment("Bill.pdf", new File("path_to_file"));

        mailSender.send(message);
    }
    private String generateBillEmailBody(Bill bill) {
        return "<h1>Bill Details</h1>" +
               "<p>Bill ID: " + bill.getBillId() + "</p>" +
               "<p>Date: " + bill.getBillDate() + "</p>" +
               "<p>Consultation Fees: " + bill.getConsultationFees() + "</p>" +
               "<p>Medicine Fees: " + bill.getMedicineFees() + "</p>" +
               "<p>Total Amount: " + bill.getTotalamount()+ "</p>" +
               "<p>Final Amount: " + bill.getFinalamount() + "</p>" +
               "<p>Thank you for choosing our services.</p>";
    }*/
    public void sendBillEmail(Bill bill) throws MessagingException {
        Context context = new Context();
        context.setVariable("bill", bill); // Set the Bill object for template rendering

        // Process the Thymeleaf template
        String emailBody = templateEngine.process("email-template", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
      //email address
        //helper.setTo(bill.getAppointment().getPatientEmail());
        helper.setTo("anshitagupta2277@gmail.com");
        helper.setSubject("Your Bill Details");
        helper.setText(emailBody, true);

        mailSender.send(message);
    }

    public void sendPaymentSuccessEmail(String toEmail, String paymentId, String orderId, double amount, int billId, double balanceAmount) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Payment Confirmation - CareAndCure");

            String htmlContent = "<div style='font-family: Arial, sans-serif; color: #444; max-width: 600px; margin: auto; border: 1px solid #ddd; border-radius: 8px; padding: 20px;'>"
                    + "<div style='text-align: center; border-bottom: 1px solid #ddd; padding-bottom: 10px;'>"
                    + "<h1 style='color: #4CAF50;'>Payment Successful</h1>"
                    + "</div>"
                    + "<p style='margin-top: 20px;'>Dear User,</p>"
                    + "<p>Your payment has been processed successfully. Here are the details:</p>"
                    + "<table style='width: 100%; border-collapse: collapse; margin-top: 10px;'>"
                    + "<tr><td style='padding: 8px; font-weight: bold;'>Bill ID:</td><td style='padding: 8px;'>" + billId + "</td></tr>"
                    + "<tr><td style='padding: 8px; font-weight: bold;'>Payment ID:</td><td style='padding: 8px;'>" + paymentId + "</td></tr>"
                    + "<tr><td style='padding: 8px; font-weight: bold;'>Order ID:</td><td style='padding: 8px;'>" + orderId + "</td></tr>"
                    + "<tr><td style='padding: 8px; font-weight: bold;'>Amount Paid:</td><td style='padding: 8px;'>₹" + amount + "</td></tr>"                   
                    + "<tr><td style='padding: 8px; font-weight: bold;'>Balance Amount:</td><td style='padding: 8px;'>₹" + balanceAmount + "</td></tr>"
                    + "</table>"
                    + "<p style='margin-top: 20px;'>Thank you for your payment!</p>"
                    + "<p>Regards,<br><strong>CareAndCure Team</strong></p>"
                    + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending success email: " + e.getMessage());
        }
    }


    public void sendPaymentFailureEmail(String toEmail, String orderId, String failureReason, int billId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Payment Failure - CareAndCure");

            String htmlContent = "<div style='font-family: Arial, sans-serif; color: #444; max-width: 600px; margin: auto; border: 1px solid #ddd; border-radius: 8px; padding: 20px;'>"
                    + "<div style='text-align: center; border-bottom: 1px solid #ddd; padding-bottom: 10px;'>"
                    + "<h1 style='color: #F44336;'>Payment Failed</h1>"
                    + "</div>"
                    + "<p style='margin-top: 20px;'>Dear User,</p>"
                    + "<p>Unfortunately, your payment attempt was not successful. Please find the details below:</p>"
                    + "<table style='width: 100%; border-collapse: collapse; margin-top: 10px;'>"
                    + "<tr><td style='padding: 8px; font-weight: bold;'>Bill ID:</td><td style='padding: 8px;'>" + billId + "</td></tr>"
                    + "<tr><td style='padding: 8px; font-weight: bold;'>Order ID:</td><td style='padding: 8px;'>" + orderId + "</td></tr>"
                    + "<tr><td style='padding: 8px; font-weight: bold;'>Reason:</td><td style='padding: 8px;'>" + failureReason + "</td></tr>"                 
                    + "</table>"
                    + "<p style='margin-top: 20px;'>We apologize for the inconvenience. Please try again or contact our support team for assistance.</p>"
                    + "<p>Regards,<br><strong>CareAndCure Team</strong></p>"
                    + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending failure email: " + e.getMessage());
        }
    }

    
    
    
    
    
    
    
    
    
    
    //payment email service
    public void sendPaymentSuccessEmail(String toEmail, String paymentId, String orderId, double amount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Payment Successful");
        message.setText("Dear User,\n\nYour payment was successful.\n\n" +
                "Payment Details:\n" +
                "Payment ID: " + paymentId + "\n" +
                "Order ID: " + orderId + "\n" +
                "Amount: ₹" + amount + "\n\nThank you for your payment.\n\n" +
                "Regards,\nCareAndCure Team");

        mailSender.send(message);
    }

    public void sendPaymentFailureEmail(String toEmail, String orderId, String failureReason) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Payment Failed");
        message.setText("Dear User,\n\nUnfortunately, your payment attempt was not successful.\n\n" +
                "Order ID: " + orderId + "\n" +
                "Reason: " + failureReason + "\n\n" +
                "Please try again or contact our support team for assistance.\n\n" +
                "Regards,\nCareAndCure Team");

        mailSender.send(message);
    }
}
