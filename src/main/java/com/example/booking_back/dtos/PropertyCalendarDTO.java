package com.example.booking_back.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PropertyCalendarDTO {
    private String propertyId;
    private String propertyName;
    private List<CalendarDayDTO> days;
}
