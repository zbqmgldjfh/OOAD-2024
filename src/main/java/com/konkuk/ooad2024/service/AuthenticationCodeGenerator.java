package com.konkuk.ooad2024.service;

import com.konkuk.ooad2024.domain.AuthenticationCode;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationCodeGenerator {
  public AuthenticationCode createAuthenticationCode(){
    AuthenticationCode authenticationCode = AuthenticationCode.createRandomCode();
    return authenticationCode;
  }

}
