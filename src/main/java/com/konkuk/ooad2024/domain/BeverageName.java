package com.konkuk.ooad2024.domain;

import lombok.Getter;

@Getter
public enum BeverageName {
  COKE("01"),
  CIDER("02"),
  GREEN_TEA("03"),
  BLACK_TEA("04"),
  MILK_TEA("05"),
  SPARKLING_WATER("06"),
  BARLEY_TEA("07"),
  CAN_COFFEE("08"),
  WATER("09"),
  ENERGY_DRINK("10"),
  YUZA_TEA("11"),
  SWEET_SHAKE("12"),
  ICE_TEA("13"),
  STRAWBERRY_JUICE("14"),
  ORANGE_JUICE("15"),
  GRAPE_JUICE("16"),
  SPORT_DRINK("17"),
  AMERICANO("18"),
  HOT_CHOCOLATE("19"),
  CAFE_LATTE("20");

  private static final String CODE_NOT_FOUND_MESSAGE = "코드와 일치하는 음료가 없습니다.";

  private final String code;

  BeverageName(String code) {
    this.code = code;
  }

  public static BeverageName from(String code) {
    for (BeverageName bn : BeverageName.values()) {
      if (bn.code.equals(code)) {
        return bn;
      }
    }

    throw new IllegalArgumentException(CODE_NOT_FOUND_MESSAGE);
  }
}
