package com.konkuk.ooad2024.domain;

import org.springframework.web.socket.messaging.WebSocketStompClient;

public class OtherDVM {
  private final Position position;

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
}
