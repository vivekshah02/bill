package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Appointment;
import com.example.model.Bill;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer>{

}
