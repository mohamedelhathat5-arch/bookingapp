package com.example.booking_back.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private String clientName;
    private String clientCin;
    private String clientPhone;
    private String clientEmail;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer nights;
    private String platform;          // "AIRBNB", "BOOKING", "DIRECT" ou minuscule si config accept-case-insensitive
    private String propertyId;        // ← juste l'ID (String)
    private BigDecimal nightlyRate;
    private BigDecimal totalAmount;
    private String responsable;
    private String paymentStatus;
    private String paymentMethod;
    private String userId;           // ID de l'utilisateur qui crée la réservation
}

