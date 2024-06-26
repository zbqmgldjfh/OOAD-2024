package com.konkuk.ooad2024.service;

import com.konkuk.ooad2024.domain.Beverage;
import com.konkuk.ooad2024.domain.BeverageName;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Beverages {
  private final Map<BeverageName, Beverage> beverages;

  @Autowired
  public Beverages(Map<BeverageName, Beverage> stockMap) {
    this.beverages = stockMap;
  }

  public boolean checkStock(BeverageName bn, int quantity) {
    return this.beverages.get(bn).getStockValue() >= quantity;
  }

  public void reduce(BeverageName bn, int quantity) {
    this.beverages.get(bn).decreaseStock(quantity);
  }

  public long findPriceByName(BeverageName beverageName) {
    return beverages.get(beverageName).getPrice().getValue();
  }
}
