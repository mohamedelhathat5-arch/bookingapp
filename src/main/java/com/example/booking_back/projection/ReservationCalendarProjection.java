package com.example.booking_back.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ReservationCalendarProjection {
    String getReservationId();
    LocalDate getStartDate();
    LocalDate getEndDate();
    BigDecimal getPrice();
    String getSource();
}
