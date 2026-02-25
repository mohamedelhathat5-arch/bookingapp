package com.example.booking_back.controllers;


import com.example.booking_back.dtos.ApartmentDTO;
import com.example.booking_back.dtos.PropertyCalendarDTO;
import com.example.booking_back.services.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@CrossOrigin("*")
public class PropertyController {
    @Autowired
    private PropertyService apartmentService;

    // 1. Liste des appartements avec stats
    @GetMapping
    public List<ApartmentDTO> getApartments(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") int month,
            @RequestParam(defaultValue = "#{T(java.time.Year).now().getValue()}") int year) {
        return apartmentService.getApartments(city, month, year,name);
    }

    // 2. Données calendrier pour un appartement
    @GetMapping("/{id}/calendar")
    public List<PropertyCalendarDTO> getCalendar(
            @PathVariable String id,
            @RequestParam(defaultValue = "1") int month,
            @RequestParam(defaultValue = "#{T(java.time.Year).now().getValue()}") int year) {
        return apartmentService.getCalendarForAllProperties(id, month, year);
    }
}
