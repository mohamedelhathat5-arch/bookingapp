package com.example.booking_back.controllers;

import com.example.booking_back.models.Reservation;
import com.example.booking_back.projection.*;
import com.example.booking_back.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/reservations")
public class ReservationController {
    ReservationService resService;
    public ReservationController(ReservationService resService){
        this.resService=resService;
    }


    @GetMapping("/byMonth")
    public List<DailyStatsProjection> allReservationperMonth(
            @RequestParam Integer month,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String propertyId,
            @RequestParam(required = false) String platform
    ) {
        return resService.getAllReservationsByMonth(month, city, propertyId, platform);
    }


    @GetMapping("/dashboard")
    public DashboardStatsProjection getDashboard(
            @RequestParam Integer month,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String propertyId,
            @RequestParam(required = false) String platform
    ) {
        return resService.getDashboardStats(month, city, propertyId, platform);
    }



    @GetMapping("/trends")
    public ResponseEntity<List<MonthlyTrendProjection>> getTrendsCurrentYear() {
        return ResponseEntity.ok(resService.getMonthlyTrendsCurrentYear());
    }

    @GetMapping("/platform-distribution")
    public ResponseEntity<List<PlatformDistributionProjection>> getPlatformDistribution() {
        return ResponseEntity.ok(resService.getPlatformDistribution());
    }


    @GetMapping("/recent")
    public ResponseEntity<List<RecentReservationProjection>> getRecentReservations() {
        List<RecentReservationProjection> reservations = resService.getRecentReservations();
        return ResponseEntity.ok(reservations);
    }
}
