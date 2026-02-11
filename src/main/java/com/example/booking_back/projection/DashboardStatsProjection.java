package com.example.booking_back.projection;

public interface DashboardStatsProjection {

    Long getTotalReservations();
    Double getTotalRevenue();
    Double getTotalExpenses();
    Double getNetProfit();
}