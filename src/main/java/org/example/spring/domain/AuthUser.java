package org.example.spring.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AuthUser {
    private Long id;
    private String username;
    private String password;
    private boolean active;
    private String role;
//    private MultipartFile image;
}
