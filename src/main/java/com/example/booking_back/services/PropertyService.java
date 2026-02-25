package com.example.booking_back.services;

import com.example.booking_back.dtos.ApartmentDTO;
import com.example.booking_back.dtos.CalendarDayDTO;
import com.example.booking_back.dtos.PropertyCalendarDTO;
import com.example.booking_back.models.Property;
import com.example.booking_back.projection.ApartmentStatsProjection;
import com.example.booking_back.projection.PlatformDistributionProjection;
import com.example.booking_back.projection.PlatformeDistributionForAppartmentsProjection;
import com.example.booking_back.projection.ReservationCalendarProjection;
import com.example.booking_back.repositories.PropertyRepository;
import com.example.booking_back.repositories.ReservationRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PropertyService {
    @Autowired
    private  PropertyRepository propertyRepository;
    @Autowired
    private  ReservationRepository reservationRepository;


    public List<ApartmentDTO> getApartments(
            String city,
            int month,
            int year,
            String name) {

        // 1. Récupérer les propriétés selon les filtres
        List<Property> properties;

        if (StringUtils.isNotBlank(name)) {
            // Filtre par nom (insensible à la casse + recherche partielle)
            properties = propertyRepository.findPropertiesByCity(name);
        } else if (StringUtils.isNotBlank(city)) {
            // Filtre par ville
            properties = propertyRepository.findPropertiesByName(city);
        } else {
            // Pas de filtre sur ville ou nom → tous
            properties = propertyRepository.findAll();
        }

        // 2. Transformer en DTO avec les stats du mois/année
        return properties.stream()
                .map(prop -> buildApartmentDTO(prop, month, year))
                .toList();
    }

    private ApartmentDTO buildApartmentDTO(Property prop, int month, int year) {
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        // Stats
        ApartmentStatsProjection stats = reservationRepository.getApartmentStats(
                prop.getId(), month, year, daysInMonth);

        // Distribution
        List<PlatformeDistributionForAppartmentsProjection> distribList = reservationRepository.getPlatformDistribution(
                prop.getId(), month, year);

        Map<String, BigDecimal> distribution = new HashMap<>();
        distribList.forEach(d -> distribution.put(d.getPlatform().toLowerCase(), d.getRevenue()));

        return ApartmentDTO.builder()
                .id(prop.getId())
                .name(prop.getName())
                .revenue(stats != null ? stats.getRevenue() : BigDecimal.ZERO)
                .nights(stats != null ? stats.getNights() : 0)
                .averagePrice(stats != null ? BigDecimal.valueOf(((long)(stats.getNights()/daysInMonth)*100 )): BigDecimal.ZERO)
                .occupancyRate(stats != null ? stats.getOccupancyRate() : 0.0)
                .distribution(distribution)
                .build();
    }

    /**
     * Retourne les jours du calendrier pour un appartement donné
     */
    public List<PropertyCalendarDTO> getCalendarForAllProperties(String id,int month, int year) {

        YearMonth ym = YearMonth.of(year, month);
        int daysInMonth = ym.lengthOfMonth();

        List<Property> properties = propertyRepository.findAll();

        List<PropertyCalendarDTO> result = new ArrayList<>();

        for (Property property : properties) {

            // Récupérer les réservations pour CET appartement
            List<ReservationCalendarProjection> reservations =
                    reservationRepository.getReservationsForCalendar(
                            property.getId(), month, year);

            List<CalendarDayDTO> days = new ArrayList<>();

            for (int day = 1; day <= daysInMonth; day++) {

                LocalDate current = LocalDate.of(year, month, day);

                CalendarDayDTO dto = CalendarDayDTO.builder()
                        .day(day)
                        .price(BigDecimal.ZERO)
                        .source("available")
                        .isCurrentMonth(true)
                        .build();

                for (ReservationCalendarProjection res : reservations) {

                    LocalDate start = res.getStartDate();
                    LocalDate lastNight = res.getEndDate().minusDays(1);

                    if (!current.isBefore(start) && !current.isAfter(lastNight)) {

                        dto.setReservationId(res.getReservationId());
                        dto.setSource(res.getSource().toLowerCase());
                        dto.setPrice(res.getPrice());

                        dto.setStart(current.equals(start));
                        dto.setEnd(current.equals(lastNight));
                        dto.setMiddle(current.isAfter(start) && current.isBefore(lastNight));

                        break;
                    }
                }

                days.add(dto);
            }

            PropertyCalendarDTO propertyCalendar = new PropertyCalendarDTO();
            propertyCalendar.setPropertyId(property.getId());
            propertyCalendar.setPropertyName(property.getName());
            propertyCalendar.setDays(days);

            result.add(propertyCalendar);
        }

        return result;
    }
}