package com.konkuk.ooad2024.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.ooad2024.controller.StockCheckRequest;
import com.konkuk.ooad2024.controller.StockCheckRequestContent;
import com.konkuk.ooad2024.controller.StockCheckResponse;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class OtherDVM {
  private final Position position;
  private final String ipAddr;
  private final int port;
  private final String id;

  public OtherDVM(Position position, String ipAddr, int port, String id) {
    this.position = position;
    this.ipAddr = ipAddr;
    this.port = port;
    this.id = id;
  }

  public Position getPosition() {
    return this.position;
  }

  public boolean checkStock(BeverageName bn, int quantity) {
    ObjectMapper mapper = new ObjectMapper();

    try (Socket socket = new Socket(this.ipAddr, this.port);
        BufferedWriter writer =
            new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
      socket.setSoTimeout(1000);

      StockCheckRequest request =
          new StockCheckRequest(
              "req_stock", "Team1", this.id, new StockCheckRequestContent(bn.getCode(), quantity));

      String jsonMessage = mapper.writeValueAsString(request);
      writer.write(jsonMessage);
      writer.newLine();
      writer.flush();

      StockCheckResponse response = mapper.readValue(reader, StockCheckResponse.class);
      System.out.println("[CLIENT] Received: " + response);

      return response.msg_content().item_num() >= quantity;
    } catch (Exception ignored) {
    }
    return false;
  }

  public boolean prepay(Beverage beverage, AuthenticationCode authCode, Position position)
      throws IOException {
    String serverIP = this.ipAddr; // other DVM IP
    int serverPort = this.port; // other DVM의 Port 번호
    String src_id = "Team1"; // 일단 임의로 설정
    String dst_id = this.id; // 일단 임의로 설정
    ObjectMapper mapper = new ObjectMapper();

    // 소켓 열기
    Socket socket = new Socket(serverIP, serverPort);
    try (BufferedWriter writer =
            new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

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
        Map<String, Object> responseMap = mapper.readValue(responseLine, Map.class);
        Map<String, Object> msgContent = (Map<String, Object>) responseMap.get("msg_content");
        String availability = (String) msgContent.get("availability");
        return "T".equals(availability);
      }
    } finally {
      socket.close();
    }
    return false; // 서버 응답이 없거나, 파싱 과정에서 오류 발생 시
  }
}
