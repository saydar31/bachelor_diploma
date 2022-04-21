package ru.itis.resourcemanagement.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenPair {
    private String accessToken;
    private String refreshToken;
}
