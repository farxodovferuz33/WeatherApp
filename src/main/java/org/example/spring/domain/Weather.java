package org.example.spring.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Weather {
    private int id;
    private int city_id;
    private int celsius;
    private int fahrenheit;
    private LocalDate date;
}
