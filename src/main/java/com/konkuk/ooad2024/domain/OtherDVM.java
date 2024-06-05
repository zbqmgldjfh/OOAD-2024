package com.konkuk.ooad2024.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.*;
import java.net.Socket;
import java.util.*;


@Component
public class OtherDVM {
  private final Position position;

  // TODO: ip address 등 socket 통신에 필요한 정보를 private filed로 보유
  // NOTE: 통신을 위한 IP 등의 정보가 필요
  // private final information...?

  public OtherDVM(Position position) {
    this.position = position;
  }

  // XXX: need HELP!!
  public boolean checkStock(BeverageName bn, int quantity, WebSocketStompClient stompClient) {
    stompClient.start();
    return true;
  }

  public Position getPosition() {
    return this.position;
  }

  public boolean checkStock(BeverageName bn, int quantity) {
    // TODO: using socket
    return true;
  }

  public boolean prepay(Beverage beverage, AuthenticationCode authCode, Position position) throws IOException {
    String serverIP = "127.0.0.1"; // other DVM IP
    int serverPort = 9999; // other DVM의 Port 번호
    int src_id = 1; //일단 임의로 설정
    int dst_id = 2; //일단 임의로 설정
    ObjectMapper mapper = new ObjectMapper();

    //소켓 열기
    Socket socket = new Socket(serverIP, serverPort);
    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
         BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

      // 메시지 보내기
      Map<String, Object> msg = new HashMap<>();
      msg.put("msg_type", "req_prepay");
      msg.put("src_id", src_id);
      msg.put("dst_id", dst_id);
      msg.put(
              "msg_content",
              Map.of(
                      "item_code", beverage.getItemCode(),
                      "item_num", beverage.getStockValue(),
                      "cert_code", authCode.getValue()));

      String jsonMessage = mapper.writeValueAsString(msg);
      writer.write(jsonMessage);
      writer.newLine();
      writer.flush();

      // 메시지 받기
      String responseLine;
      if ((responseLine = reader.readLine()) != null) {
        System.out.println("Received from server: " + responseLine);
        // JSON 파싱
        Map<String, Object> responseMap = mapper.readValue(responseLine, Map.class);
        Map<String, Object> msgContent = (Map<String, Object>) responseMap.get("msg_content");
        String availability = (String) msgContent.get("availability");
        return "T".equals(availability);
      }
    } finally {
      socket.close();
    }
    return false;  // 서버 응답이 없거나, 파싱 과정에서 오류 발생 시
  }

}
