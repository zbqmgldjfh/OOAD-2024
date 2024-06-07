package com.konkuk.ooad2024.service;

import com.konkuk.ooad2024.domain.BeverageName;
import com.konkuk.ooad2024.domain.OtherDVM;
import com.konkuk.ooad2024.domain.Position;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtherDVMs {
  private final Map<String, OtherDVM> otherDVMs;

  @Autowired
  public OtherDVMs(Map<String, OtherDVM> otherDVMsMap) {
    this.otherDVMs = otherDVMsMap;
  }

  public Position findNearestDVM(BeverageName bn, int quantity, Position myPosition) {
    return this.otherDVMs.values().stream()
        .filter((otherDVM) -> otherDVM.checkStock(bn, quantity))
        .map(OtherDVM::getPosition)
        .sorted(
            (dvm1, dvm2) -> {
              double dis1 = myPosition.getDistance(dvm1);
              double dis2 = myPosition.getDistance(dvm2);

              if (dis1 > dis2) return 1;
              if (dis1 < dis2) return -1;
              return 0;
            })
        .findFirst()
        .orElse(null);
  }

  public OtherDVM findByPosition(Position position) {
    return this.otherDVMs.values().stream()
        .filter(
            (dvm) ->
                dvm.getPosition().getXaxis() == position.getXaxis()
                    && dvm.getPosition().getYaxis() == position.getYaxis())
        .findFirst()
        .orElse(null);
  }
}
