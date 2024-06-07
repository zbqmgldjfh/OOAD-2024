package com.konkuk.ooad2024.config;

import com.konkuk.ooad2024.domain.Account;
import com.konkuk.ooad2024.domain.Beverage;
import com.konkuk.ooad2024.domain.BeverageName;
import com.konkuk.ooad2024.domain.Database;
import com.konkuk.ooad2024.domain.OtherDVM;
import com.konkuk.ooad2024.domain.Position;
import java.util.AbstractMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Metadata {

  @Bean
  public Database myDatabase() {
    Database db = new Database();
    db.addAccount(Account.createNewAccount(1_000_000l));
    db.addAccount(Account.createNewAccount(1_000_000l));
    db.addAccount(Account.createNewAccount(1_000_000l));
    db.addAccount(Account.createNewAccount(1_000_000l));
    db.addAccount(Account.createNewAccount(1_000_000l));
    return db;
  }

  @Bean
  public Position myPosition() {
    return new Position(7, 7);
  }

  @Bean
  public Map<String, OtherDVM> otherDVMsMap() {
    return Map.ofEntries(
        //        new AbstractMap.SimpleEntry<>(
        //            "Team2", new OtherDVM(new Position(12, 7), "127.0.0.1", 9999, "Team2")),
        //        new AbstractMap.SimpleEntry<>(
        //            "Team3", new OtherDVM(new Position(66, 66), "43.200.64.96", 8080, "Team3")),
        //        new AbstractMap.SimpleEntry<>(
        //            "Team4", new OtherDVM(new Position(40, 40), "127.0.0.1", 9999, "Team4")),
        //        new AbstractMap.SimpleEntry<>(
        //            "Team5", new OtherDVM(new Position(50, 1), "127.0.0.1", 9999, "Team5")),
        //        new AbstractMap.SimpleEntry<>(
        //            "Team6", new OtherDVM(new Position(27, 80), "127.0.0.1", 9999, "Team6")),
        //        new AbstractMap.SimpleEntry<>(
        //            "Team7", new OtherDVM(new Position(0, 0), "127.0.0.1", 9999, "Team7")),
        //        new AbstractMap.SimpleEntry<>(
        //            "Team8", new OtherDVM(new Position(30, 30), "127.0.0.1", 9999, "Team8")),
        new AbstractMap.SimpleEntry<>(
            "Team9", new OtherDVM(new Position(7, 17), "15.164.4.234", 8888, "Team9")));
  }

  @Bean
  public Map<BeverageName, Beverage> myStock() {
    return Map.ofEntries(
        new AbstractMap.SimpleEntry<>(
            BeverageName.COKE, new Beverage(BeverageName.COKE, 2_000, 99)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.CIDER, new Beverage(BeverageName.CIDER, 2_000, 99)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.GREEN_TEA, new Beverage(BeverageName.GREEN_TEA, 1_500, 99)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.BLACK_TEA, new Beverage(BeverageName.BLACK_TEA, 1_500, 99)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.MILK_TEA, new Beverage(BeverageName.MILK_TEA, 2_000, 99)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.SPARKLING_WATER, new Beverage(BeverageName.SPARKLING_WATER, 1_500, 99)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.BARLEY_TEA, new Beverage(BeverageName.BARLEY_TEA, 1_500, 99)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.CAN_COFFEE, new Beverage(BeverageName.CAN_COFFEE, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.WATER, new Beverage(BeverageName.WATER, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.ENERGY_DRINK, new Beverage(BeverageName.ENERGY_DRINK, 2_500, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.YUZA_TEA, new Beverage(BeverageName.YUZA_TEA, 2_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.SWEET_SHAKE, new Beverage(BeverageName.SWEET_SHAKE, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.ICE_TEA, new Beverage(BeverageName.ICE_TEA, 2_500, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.STRAWBERRY_JUICE, new Beverage(BeverageName.STRAWBERRY_JUICE, 3_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.ORANGE_JUICE, new Beverage(BeverageName.ORANGE_JUICE, 3_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.GRAPE_JUICE, new Beverage(BeverageName.GRAPE_JUICE, 3_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.SPORT_DRINK, new Beverage(BeverageName.SPORT_DRINK, 2_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.AMERICANO, new Beverage(BeverageName.AMERICANO, 2_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.HOT_CHOCOLATE, new Beverage(BeverageName.HOT_CHOCOLATE, 1_000, 0)),
        new AbstractMap.SimpleEntry<>(
            BeverageName.CAFE_LATTE, new Beverage(BeverageName.CAFE_LATTE, 2_500, 0)));
  }
}
