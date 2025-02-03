package com.example.repository;

import java.time.LocalDate;
import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Bill;




@Repository
public interface BillingRepository extends JpaRepository<Bill, Integer> {	

	 Bill findByBillId(int billId);
	
	 Bill findByAppointment_AppointmentId(int appointmentId);

	
	 List<Bill> findByAppointment_PatientId(int patientId);

	 List<Bill> findByBillDate(LocalDate billDate);
	
}
