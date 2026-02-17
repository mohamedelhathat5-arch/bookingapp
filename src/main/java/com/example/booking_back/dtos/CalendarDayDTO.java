package com.example.booking_back.dtos;


import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CalendarDayDTO {
    private int day;                    // Numéro du jour (1-31)
    private BigDecimal price;           // Prix / tarif du jour
    private String source;              // "airbnb", "booking", "direct", "available"
    private boolean isCurrentMonth;     // true pour jours du mois courant
    private String reservationId;       // ID de la réservation (nullable)
    private boolean isStart;            // Premier jour de la réservation
    private boolean isEnd;              // Dernier jour
    private boolean isMiddle;           // Jour intermédiaire
}