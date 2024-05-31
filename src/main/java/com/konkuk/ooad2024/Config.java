package com.konkuk.ooad2024;

import com.konkuk.ooad2024.domain.Beverage;
import com.konkuk.ooad2024.domain.BeverageName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.AbstractMap;
import java.util.Map;

@Configuration
public class Config {

  @Bean
  public Map<BeverageName, Beverage> BeverageMap() {
    return Map.ofEntries(
        new AbstractMap.SimpleEntry<>(BeverageName.COKE, new Beverage(BeverageName.COKE, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.CIDER, new Beverage(BeverageName.CIDER, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.GREEN_TEA, new Beverage(BeverageName.GREEN_TEA, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.BLACK_TEA, new Beverage(BeverageName.BLACK_TEA, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.MILK_TEA, new Beverage(BeverageName.MILK_TEA, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.SPARKLING_WATER, new Beverage(BeverageName.SPARKLING_WATER, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.BARLEY_TEA, new Beverage(BeverageName.BARLEY_TEA, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.CAN_COFFEE, new Beverage(BeverageName.CAN_COFFEE, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.WATER, new Beverage(BeverageName.WATER, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.ENERGY_DRINK, new Beverage(BeverageName.ENERGY_DRINK, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.YUZA_TEA, new Beverage(BeverageName.YUZA_TEA, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.SWEET_SHAKE, new Beverage(BeverageName.SWEET_SHAKE, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.ICE_TEA, new Beverage(BeverageName.ICE_TEA, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.STRAWBERRY_JUICE, new Beverage(BeverageName.STRAWBERRY_JUICE, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.ORANGE_JUICE, new Beverage(BeverageName.ORANGE_JUICE, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.GRAPE_JUICE, new Beverage(BeverageName.GRAPE_JUICE, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.SPORT_DRINK, new Beverage(BeverageName.SPORT_DRINK, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.AMERICANO, new Beverage(BeverageName.AMERICANO, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.HOT_CHOCOLATE, new Beverage(BeverageName.HOT_CHOCOLATE, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.CAFE_LATTE, new Beverage(BeverageName.CAFE_LATTE, 1_000, 0)));
  }
}
