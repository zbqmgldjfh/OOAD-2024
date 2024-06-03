package com.konkuk.ooad2024.domain;

import org.springframework.web.socket.messaging.WebSocketStompClient;

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

  public boolean prepay(String beverageId, int quantity, AuthenticationCode authCode) {
    // TODO: using socket
    return true;
  }
}
