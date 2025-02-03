package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Appointment;

import com.example.repository.AppointmentRepository;



@Service
public class AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;
	
	public Appointment  addAppointment(Appointment appointment) {
		return appointmentRepository.save(appointment);
	}
	
}
