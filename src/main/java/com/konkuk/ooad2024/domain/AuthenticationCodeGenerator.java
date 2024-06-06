package com.konkuk.ooad2024.domain;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationCodeGenerator {
  public AuthenticationCode createAuthenticationCode() {
    AuthenticationCode authenticationCode = AuthenticationCode.createRandomCode();
    return authenticationCode;
  }
}
