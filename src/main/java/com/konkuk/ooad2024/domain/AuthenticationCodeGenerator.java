package com.konkuk.ooad2024.domain;

import com.konkuk.ooad2024.domain.AuthenticationCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class AuthenticationCodeGenerator {
  public AuthenticationCode createAuthenticationCode() {
    AuthenticationCode authenticationCode = AuthenticationCode.createRandomCode();
    return authenticationCode;
  }
}
