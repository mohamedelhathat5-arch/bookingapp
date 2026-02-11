package com.example.booking_back.projection;

import java.math.BigDecimal;

public interface MonthlyTrendProjection {
    Integer getMonth();
    Long getReservations();
    BigDecimal getRevenue();
    BigDecimal getExpenses();
}