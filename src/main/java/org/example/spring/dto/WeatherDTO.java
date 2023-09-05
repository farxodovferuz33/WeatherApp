package org.example.spring.dto;

import java.time.LocalDate;

public record WeatherDTO(int celsius, int fahrenheit, LocalDate date) {
}
