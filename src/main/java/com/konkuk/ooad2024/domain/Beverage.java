package com.konkuk.ooad2024.domain;

import lombok.Getter;

@Getter
public class Beverage {

    private BeverageName name;
    private Money price;
    private Stock stock;

    public Beverage(BeverageName name, int price, int stock) {
        this.name = name;
        this.price = new Money(price);
        this.stock = new Stock(stock);
    }

    public void decreaseStock(int count) {
        this.stock = new Stock(this.stock.getValue() - count);
    }

    public int getStockValue() {
        return this.stock.getValue();
    }
}
