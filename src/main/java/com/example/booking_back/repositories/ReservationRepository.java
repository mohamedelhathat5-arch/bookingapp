package com.example.booking_back.repositories;

import com.example.booking_back.models.Reservation;
import com.example.booking_back.projection.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,String> {
public List<Reservation> getReservationsByMonth(Integer month);


    @Query(value = """
    SELECT 
        DATE(r.check_in) AS date,
        SUM(r.nights) AS totalNuits,
        SUM(r.total_amount) AS totalRevenue,

        COUNT(CASE WHEN r.platform = 'DIRECT' THEN 1 END) AS directCount,
        SUM(CASE WHEN r.platform = 'DIRECT' THEN r.total_amount ELSE 0 END) AS directRevenue,

        COUNT(CASE WHEN r.platform = 'AIRBNB' THEN 1 END) AS airbnbCount,
        SUM(CASE WHEN r.platform = 'AIRBNB' THEN r.total_amount ELSE 0 END) AS airbnbRevenue,

        COUNT(CASE WHEN r.platform = 'BOOKING' THEN 1 END) AS bookingCount,
        SUM(CASE WHEN r.platform = 'BOOKING' THEN r.total_amount ELSE 0 END) AS bookingRevenue

    FROM reservations r
    JOIN properties p ON r.property_id = p.id

    WHERE r.month = :month
    AND (:city IS NULL OR p.city = :city)
    AND (:propertyId IS NULL OR r.property_id = :propertyId)
    AND (:platform IS NULL OR r.platform = :platform)

    GROUP BY DATE(r.check_in)
    ORDER BY DATE(r.check_in)
""", nativeQuery = true)
    List<DailyStatsProjection> getDailyStats(
            @Param("month") Integer month,
            @Param("city") String city,
            @Param("propertyId") String propertyId,
            @Param("platform") String platform
    );







    @Query(value = """
    SELECT 
        COUNT(DISTINCT r.id) AS totalReservations,
        COALESCE(SUM(DISTINCT r.total_amount), 0) AS totalRevenue,
        COALESCE(SUM(e.amount), 0) AS totalExpenses,
        COALESCE(SUM(DISTINCT r.total_amount), 0) - COALESCE(SUM(e.amount), 0) AS netProfit

    FROM reservations r
    JOIN properties p ON r.property_id = p.id
    LEFT JOIN expense e ON e.property_id = r.property_id 
        AND MONTH(e.date) = :month

    WHERE r.month = :month
    AND (:city IS NULL OR p.city = :city)
    AND (:propertyId IS NULL OR r.property_id = :propertyId)
    AND (:platform IS NULL OR r.platform = :platform)
""", nativeQuery = true)
    DashboardStatsProjection getDashboardStats(
            @Param("month") Integer month,
            @Param("city") String city,
            @Param("propertyId") String propertyId,
            @Param("platform") String platform
    );



    // Pour le graphique en ligne
    @Query(value = """
    SELECT 
        r.month AS month,
        COUNT(DISTINCT r.id) AS reservations,
        COALESCE(SUM(r.total_amount), 0) AS revenue,
        COALESCE(SUM(e.amount), 0) AS expenses
    FROM reservations r
    LEFT JOIN expense e 
        ON e.property_id = r.property_id 
        AND MONTH(e.date) = r.month 
        AND YEAR(e.date) = YEAR(CURRENT_DATE)
    WHERE YEAR(r.check_in) = YEAR(CURRENT_DATE)
    GROUP BY r.month
    ORDER BY r.month
    """, nativeQuery = true)
    List<MonthlyTrendProjection> getMonthlyTrendsCurrentYear();

    // Pour le camembert : distribution par plateforme (sur une période donnée)
    @Query(value = """
    SELECT 
        r.platform AS platform,
        COUNT(*) AS count
    FROM reservations r
    WHERE YEAR(r.check_in) = YEAR(CURRENT_DATE)
    GROUP BY r.platform
    ORDER BY count DESC
    """, nativeQuery = true)
    List<PlatformDistributionProjection> getPlatformDistributionCurrentYear();



    @Query(value = """
        SELECT 
            r.reference AS reference,
            c.full_name AS client,
            r.check_in AS arrivee,
            r.check_out AS depart,
            r.nights AS nuits,
            r.platform AS plateforme,
            CASE 
                WHEN r.status = 'PAID' THEN 'Payé'
                WHEN r.status = 'PENDING' THEN 'En cours'
                WHEN r.status = 'CANCELLED' THEN 'Annulé'
                ELSE 'Impáyé'
            END AS statut,
            COALESCE(p.method, 'En cours') AS typePaiement,
            r.total_amount AS montantTotal
        FROM reservations r
        LEFT JOIN client c ON r.client_id = c.id
        LEFT JOIN payments p ON p.reservation_id = r.id   -- si tu as une table payment
        ORDER BY r.check_in DESC
        LIMIT 10   -- les 10 plus récentes
        """, nativeQuery = true)
    List<RecentReservationProjection> findRecentReservations();




    // 1. Stats globales par appartement et mois
    @Query(value = """
        SELECT 
            SUM(r.nights) AS nights,
            COALESCE(SUM(r.total_amount), 0) AS revenue,
            COALESCE(AVG(r.nightly_rate), 0) AS averagePrice,
            (COUNT(r.id) * 100.0 / :daysInMonth) AS occupancyRate
        FROM reservations r
        WHERE r.property_id = :propertyId
          AND r.month = :month
          AND YEAR(r.check_in) = :year
        group by r.property_id
        """, nativeQuery = true)
    ApartmentStatsProjection getApartmentStats(
            @Param("propertyId") String propertyId,
            @Param("month") int month,
            @Param("year") int year,
            @Param("daysInMonth") int daysInMonth
    );

    // 2. Distribution par plateforme pour un appartement et mois
    @Query(value = """
        SELECT 
            r.platform AS platform,
            COALESCE(SUM(r.total_amount), 0) AS revenue
        FROM reservations r
        WHERE r.property_id = :propertyId
          AND r.month = :month
          AND YEAR(r.check_in) = :year
        GROUP BY r.platform
        """, nativeQuery = true)
    List<PlatformeDistributionForAppartmentsProjection> getPlatformDistribution(
            @Param("propertyId") String propertyId,
            @Param("month") int month,
            @Param("year") int year
    );

    // 3. Réservations pour le calendrier (un mois donné)
    @Query(value = """
        SELECT 
            r.id AS reservationId,
            r.check_in AS startDate,
            r.check_out AS endDate,
            r.nightly_rate AS price,
            r.platform AS source
        FROM reservations r
        WHERE r.property_id = :propertyId
          AND r.month = :month
          AND YEAR(r.check_in) = :year
        """, nativeQuery = true)
    List<ReservationCalendarProjection> getReservationsForCalendar(
            @Param("propertyId") String propertyId,
            @Param("month") int month,
            @Param("year") int year
    );




}
