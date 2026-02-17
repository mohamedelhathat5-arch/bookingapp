package com.example.booking_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booking_back.models.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    
}
