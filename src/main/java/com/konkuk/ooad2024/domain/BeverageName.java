package com.konkuk.ooad2024.domain;

public enum BeverageName {

    COKE(1), CIDER(2), GREEN_TEA(3), BLACK_TEA(4), MILK_TEA(5),
    SPARKLING_WATER(6), BARLEY_TEA(7), CAN_COFFEE(8), WATER(9), ENERGY_DRINK(10),
    YUZA_TEA(11), SWEET_SHAKE(12), ICE_TEA(13), STRAWBERRY_JUICE(14), ORANGE_JUICE(15),
    GRAPE_JUICE(16), SPORT_DRINK(17), AMERICANO(18), HOT_CHOCOLATE(19), CAFE_LATTE(20);

    private int code;

    BeverageName(int code) {
        this.code = code;
    }
}
