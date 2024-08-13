package com.example.library.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AuthenticationResponse {
   private String token;
}
