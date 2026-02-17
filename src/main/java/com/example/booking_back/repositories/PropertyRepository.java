package com.example.booking_back.repositories;

import com.example.booking_back.models.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property,String> {
    List<Property> findAll();
    List<Property> findPropertiesByCity(String city);
    List<Property> findPropertiesByName(String name);
}
