package com.example.booking_back.services;


import com.example.booking_back.models.Reservation;
import com.example.booking_back.projection.*;
import com.example.booking_back.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    ReservationRepository resRepo;

    public ReservationService(ReservationRepository resRepo) {
         this.resRepo=resRepo;
    }

    public List<DailyStatsProjection> getAllReservationsByMonth(
            Integer month,
            String city,
            String propertyId,
            String platform
    ) {

        if (city != null && city.isBlank()) city = null;
        if (propertyId != null && propertyId.isBlank()) propertyId = null;
        if (platform != null && platform.isBlank()) platform = null;

        return resRepo.getDailyStats(month, city, propertyId, platform);
    }

    public DashboardStatsProjection getDashboardStats(Integer month, String city, String propertyId, String platform) {
        if (city != null && city.isBlank()) city = null;
        if (propertyId != null && propertyId.isBlank()) propertyId = null;
        if (platform != null && platform.isBlank()) platform = null;

        return resRepo.getDashboardStats(month, city, propertyId, platform);
    }


    // Graphique en ligne : tendances par mois (année actuelle)
    public List<MonthlyTrendProjection> getMonthlyTrendsCurrentYear() {
        return resRepo.getMonthlyTrendsCurrentYear();
    }

    // Camembert : distribution par plateforme pour un mois donné (année actuelle)
    public List<PlatformDistributionProjection> getPlatformDistribution() {
        return resRepo.getPlatformDistributionCurrentYear();
    }


    public List<RecentReservationProjection> getRecentReservations() {
        return resRepo.findRecentReservations();
    }

}
