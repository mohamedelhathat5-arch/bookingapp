package com.example.booking_back.dtos;


import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentDTO {
    private String id;
    private String name;
    private BigDecimal revenue;         // Revenu total
    private int nights;                 // Nuits louées
    private BigDecimal averagePrice;    // Prix moyen par nuit
    private double occupancyRate;       // Taux d'occupation (%)
    private Map<String, BigDecimal> distribution; // ex: {"airbnb": 5100, "booking": 5100, "direct": 5200}
}