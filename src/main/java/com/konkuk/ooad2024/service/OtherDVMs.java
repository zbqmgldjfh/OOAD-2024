package com.konkuk.ooad2024.service;

import com.konkuk.ooad2024.domain.BeverageName;
import com.konkuk.ooad2024.domain.OtherDVM;
import com.konkuk.ooad2024.domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Map;

@Service
public class OtherDVMs {
  private final Map<String, OtherDVM> otherDVMs;
  private final WebSocketStompClient stompClient;

  @Autowired
  public OtherDVMs(Map<String, OtherDVM> otherDVMsMap, WebSocketStompClient stompClient) {
    this.otherDVMs = otherDVMsMap;
    this.stompClient = stompClient;
  }

  public Position findNearestDVM(BeverageName bn, int quantity, Position myPosition) {
    return this.otherDVMs.values().stream()
        .filter((otherDVM) -> otherDVM.checkStock(bn, quantity, this.stompClient))
        .map(OtherDVM::getPosition)
        .sorted(
            (dvm1, dvm2) -> {
              double dis1 = myPosition.getDistance(dvm1);
              double dis2 = myPosition.getDistance(dvm2);

              if (dis1 > dis2) return 1;
              if (dis1 < dis2) return -1;
              return 0;
            })
        .toList()
        .get(0);
  }
}
