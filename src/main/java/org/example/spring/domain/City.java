package org.example.spring.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class City {
    private int id;
    private String name;
    private String country;
    private String created_by;
}
