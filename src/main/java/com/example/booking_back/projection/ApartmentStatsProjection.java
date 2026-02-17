package com.example.booking_back.projection;

import java.math.BigDecimal;

public interface ApartmentStatsProjection {
    int getNights();
    BigDecimal getRevenue();
    BigDecimal getAveragePrice();
    double getOccupancyRate();
}
