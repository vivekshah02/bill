package com.example.controller;

import java.time.LocalDate;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.exception.BillNotFoundException;
import com.example.model.Bill;
import com.example.service.BillingService;
import com.example.service.EmailNotificationService;

import jakarta.mail.MessagingException;




@RestController
public class BillingController {

	@Autowired
	private BillingService billingService;
	
	 @Autowired
	 private EmailNotificationService emailNotificationService;
	
	@GetMapping("/welcome")
	public String displayWelcomeMessage() {

		String msg = billingService.displayWelcomeMessage();
		return msg;
	}
 
	
	
	
	
	@PostMapping("/bills/{appointmentId}")
	public ResponseEntity<?> createBill(@RequestBody Bill bill, @PathVariable int appointmentId) throws BillNotFoundException,NullPointerException,IllegalArgumentException,MessagingException,Exception{
		

	  //  try {
	    	System.out.println("Bill: " + bill);
			System.out.println("Appointment ID: " + appointmentId);
	        Bill savedBill = billingService.createBill(bill, appointmentId);
	        
	        
	        // Send the email
	        
	        	emailNotificationService.sendBillEmail(savedBill);
	       
	        return ResponseEntity.status(HttpStatus.CREATED).body(savedBill); // Return 201 
	   /* }
	    catch (IllegalArgumentException ex) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	    }*/
	  /*  catch (BillNotFoundException ex) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	    }*/
	    
	  /*  catch (Exception ex) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
	    }*/
	}
    //http://localhost:8080/bills/5
	
	
	//updatebillById
		@PutMapping("/bills/{billId}")
		public ResponseEntity<?> updateBillById(@RequestBody Bill bill,@PathVariable int billId) throws BillNotFoundException,NullPointerException,IllegalArgumentException,Exception{
			
			Bill b = billingService.updatePaymentStatus(billId, bill.getPaymentstatus());
	        return ResponseEntity.ok(b); // Return 200 OK
	        
			/* try {
		        Bill b = billingService.updatePaymentStatus(billId, bill.getPaymentstatus());
		        return ResponseEntity.ok(b); // Return 200 OK
		    } catch (ResourceNotFoundException ex) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); // Return 404 Not Found
		    } catch (Exception ex) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
		    }*/
		}

	//searchBillbyBillId
	@GetMapping("/bills/{billId}")
	public ResponseEntity<?> getBillById(@PathVariable int billId) throws ResourceNotFoundException,Exception{
	  //  try {
	        Bill bill = billingService.getBillById(billId);
	        return ResponseEntity.ok(bill); // Return 200 OK
	  /*  } catch (ResourceNotFoundException ex) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); // Return 404 Not Found
	    } catch (Exception ex) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
	    }*/
	}
	//http://localhost:8080/bills/2
	
	
	
	//searchBillByPatientID
	@GetMapping("/bills/patient/{patientId}")
	public ResponseEntity<?> getBillsByPatientId(@PathVariable int patientId) {
	    try {
	        List<Bill> bills = billingService.getBillsByPatientId(patientId); 
	        if (bills.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No bills found for patient with ID: " + patientId);
	        }
	        return ResponseEntity.ok(bills); // Return 200 OK
	    } 
	    catch (Exception ex) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
	    }
	}
    //http://localhost:8080/bills/patient/103
	
	
	
	//searchBillByDate
	@GetMapping("/bills/date/{billDate}")
	public ResponseEntity<?> getBillsByDate(@PathVariable String billDate) {
	    try {
	        // Convert the billDate string to LocalDate
	        LocalDate parsedDate = LocalDate.parse(billDate);
	        
	        List<Bill> bills = billingService.getBillsByDate(parsedDate);
	        
	        if (bills.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No bills found for date: " + billDate);
	        }
	        
	        return ResponseEntity.ok(bills); // Return 200 OK 
	    } catch (DateTimeParseException ex) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format. Please use 'YYYY-MM-DD'.");
	    } catch (Exception ex) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
	    }
	}
    //http://localhost:8080/bills/date/2024-12-20
	
	
	// Update bill status
   /* @PutMapping("/update/{billId}")
    public ResponseEntity<Bill> updateBillStatus(@PathVariable int billId, @RequestBody Bill updatedBill) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isPresent()) {
            Bill bill = billOptional.get();
            bill.setPaymentstatus(updatedBill.getPaymentstatus());
            // You can update other fields if needed.
            billRepository.save(bill);
            return ResponseEntity.ok(bill);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }*/
	
	
}
