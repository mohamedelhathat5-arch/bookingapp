package com.example.booking_back.projection;

import java.time.LocalDate;

public interface DailyStatsProjection {

    LocalDate getDate();

    Integer getTotalNuits();
    Double getTotalRevenue();

    Integer getDirectCount();
    Double getDirectRevenue();

    Integer getAirbnbCount();
    Double getAirbnbRevenue();

    Integer getBookingCount();
    Double getBookingRevenue();
}
