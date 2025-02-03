package com.example.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.example.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {	
	 List<Payment> findByBill_BillId(int billId);
	    List<Payment> findByPaymentMethod(String paymentMethod);
	    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
	    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
}
