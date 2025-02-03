package com.example.service;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.example.model.Appointment;
import com.example.model.Bill;
import com.example.exception.BillNotFoundException;
import com.example.repository.AppointmentRepository;
import com.example.repository.BillingRepository;





@Service
public class BillingService {

	@Autowired
	private BillingRepository billingRepository;
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	public String displayWelcomeMessage() {
		return "welcome to billingSystem";
	}
	
	
	
	public Bill createBill(Bill bill, int appointmentId) {
		// Check if the appointment exists
		if (bill == null ||appointmentId <= 0) {
		    throw new IllegalArgumentException("Invalid Bill or Appointment ID.");
		}
       
	    Appointment appointment = appointmentRepository.findById(appointmentId)
	            .orElseThrow(() -> new BillNotFoundException("Appointment not found with ID: " + appointmentId));

	    
	    // Check if a bill is already associated with this appointment
	    Bill b=billingRepository.findByAppointment_AppointmentId(appointmentId);
        if (b!=null) {
            throw new IllegalArgumentException("A bill already exists for this appointment.");
        }   
	    double totalAmount = bill.getConsultationFees() +
                bill.getMedicineFees() +
                bill.getTestCharges() +
                bill.getMiscellaneousCharge();

	    float discountPercentage = (totalAmount >= 100000) ? 20 : (totalAmount >= 1000) ? 10 : 0;

	    double discount = (totalAmount * discountPercentage) / 100;
	    double taxableAmount = totalAmount - discount;
	    float taxPercentage = 2;
	    double finalAmount = taxableAmount + (taxableAmount * taxPercentage) / 100;

	    bill.setTotalamount(totalAmount);
	    bill.setDiscountPercentage(discountPercentage);
	    bill.setTaxableamount(taxableAmount);
	    bill.setTaxPercentage(taxPercentage);
	    bill.setFinalamount(finalAmount);
	    // Set the appointment to the bill
	    bill.setAppointment(appointment);

	    
	    return billingRepository.save(bill);
	}
	
	public Bill updatePaymentStatus(int billId, String paymentStatus) {
		if (billId <= 0) {
		    throw new IllegalArgumentException("Invalid Bill or Appointment ID.");
		}
       
        Bill bill = billingRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found with ID: " + billId));
        bill.setPaymentstatus(paymentStatus);
        return billingRepository.save(bill);
    }

	
	public Bill getBillById(int billId) {
	    return billingRepository.findById(billId)
	            .orElseThrow(() -> new BillNotFoundException("Bill not found with ID: " + billId));
	}
   
	public List<Bill> getBillsByPatientId(int patientId) {
	    return billingRepository.findByAppointment_PatientId(patientId);
	}

	
	public List<Bill> getBillsByDate(LocalDate billDate) {
	    return billingRepository.findByBillDate(billDate);
	}

	public Bill saveBill(Bill bill) {
	    return billingRepository.save(bill);
	}
	public Bill findByBillId(int billId) {
        return billingRepository.findByBillId(billId);
    }

}
