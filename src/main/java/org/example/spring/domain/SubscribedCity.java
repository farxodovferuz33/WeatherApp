package org.example.spring.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscribedCity {
    private Integer id;
    private Integer user_id;
    private String city_name;
    private int city_id;
}
