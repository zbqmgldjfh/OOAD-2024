package com.konkuk.ooad2024.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.ooad2024.domain.AuthenticationCode;
import com.konkuk.ooad2024.domain.AuthenticationCodeGenerator;
import com.konkuk.ooad2024.domain.Beverage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.Socket;
import java.util.*;
import java.io.*;

@RequiredArgsConstructor
@Service
public class PrePaymentMachine {
  private final AuthenticationCodeGenerator authenticationCodeGenerator;
  private final Map<AuthenticationCode, Beverage> codeToBeverageMap = new HashMap<>();
  private static final String INVALID_AUTH_CODE_MESSAGE = "유효하지 않은 인증 코드입니다.";

  public void prePayment(int position, Beverage beverage) throws IOException {
    // 새 인증코드 생성
    AuthenticationCode newCode = authenticationCodeGenerator.createAuthenticationCode();

    // 다른 DVM으로 인증코드와 음료수 정보 보내기
    String serverIP = "127.0.0.1"; // other DVM IP
    int serverPort = 9999; // other DVM의 Port 번호
    int src_id = 1;
    int dst_id = position;
    ObjectMapper mapper = new ObjectMapper();

    try (Socket socket = new Socket(serverIP, serverPort);
        BufferedWriter writer =
            new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

      Map<String, Object> msg = new HashMap<>();
      msg.put("msg_type", "req_prepay");
      msg.put("src_id", src_id);
      msg.put("dst_id", dst_id);
      msg.put(
          "msg_content",
          Map.of(
              "item_code", beverage.getItemCode(),
              "item_num", beverage.getStockValue(),
              "cert_code", newCode.getValue()));

      String jsonMessage = mapper.writeValueAsString(msg);
      writer.write(jsonMessage);
      writer.newLine();
      writer.flush();
    }
  }

  public void storeBeverage(AuthenticationCode authenticationCode, Beverage beverage) {
    codeToBeverageMap.put(authenticationCode, beverage);
  }

  public Beverage getPrePaiedBeverage(AuthenticationCode authenticationCode) {
    // HashMap에 해당 인증코드가 존재하는지 확인
    Beverage beverage = codeToBeverageMap.get(authenticationCode);
    if (beverage == null) {
      throw new IllegalArgumentException(INVALID_AUTH_CODE_MESSAGE);
    }

    return beverage;
  }
}
