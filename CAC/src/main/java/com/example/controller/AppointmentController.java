package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Appointment;
import com.example.model.Bill;
import com.example.repository.AppointmentRepository;
import com.example.service.AppointmentService;
import com.example.service.BillingService;

@RestController
public class AppointmentController {
	
	@Autowired
	private AppointmentService appointmentService;

	@PostMapping("/addAppointment")
	public ResponseEntity<Appointment> addAppointmentObject(@RequestBody Appointment appointment) {

		return new ResponseEntity<>(appointmentService.addAppointment(appointment), HttpStatus.OK);
	}
	//http://localhost:8080/addAppointment
	
}
