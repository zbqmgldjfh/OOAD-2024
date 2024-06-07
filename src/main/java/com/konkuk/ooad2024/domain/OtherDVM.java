package com.konkuk.ooad2024.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
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

      Map<String, Object> requestMap = new HashMap<>();
      requestMap.put("msg_type", "req_stock");
      requestMap.put("src_id", "Team1");
      requestMap.put("dst_id", "0");
      requestMap.put("msg_content", Map.of("item_code", bn.getCode(), "item_num", quantity));

      String jsonMessage = mapper.writeValueAsString(requestMap);
      writer.write(jsonMessage);
      writer.newLine();
      writer.flush();

      String responseLine = reader.readLine();
      if (responseLine == null) return false;

      System.out.println("[CLIENT] Received: " + responseLine);

      Map<String, Object> responseMap = mapper.readValue(responseLine, Map.class);
      String msg_type = (String) responseMap.get("msg_type");
      if (!"resp_stock".equals(msg_type)) return false;

      Map<String, Object> msgContent = (Map<String, Object>) responseMap.get("msg_content");
      String item_code = (String) msgContent.get("item_code");
      int item_num = (int) msgContent.get("item_num");

      return item_num >= quantity;
    } catch (Exception ignored) {
    }
    return false;
  }

  public boolean prepay(Beverage beverage, AuthenticationCode authCode, Position position) {
    ObjectMapper mapper = new ObjectMapper();

    try (Socket socket = new Socket(this.ipAddr, this.port);
        BufferedWriter writer =
            new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
      socket.setSoTimeout(1000);

      // 메시지 보내기
      Map<String, Object> msg = new HashMap<>();
      msg.put("msg_type", "req_prepay");
      msg.put("src_id", "Team1");
      msg.put("dst_id", this.id);
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
      String responseLine = reader.readLine();
      if (responseLine == null) return false;

      Map<String, Object> responseMap = mapper.readValue(responseLine, Map.class);
      Map<String, Object> msgContent = (Map<String, Object>) responseMap.get("msg_content");
      String availability = (String) msgContent.get("availability");
      return "T".equals(availability);
    } catch (Exception ignored) {
    }
    return false; // 서버 응답이 없거나, 파싱 과정에서 오류 발생 시
  }
}
