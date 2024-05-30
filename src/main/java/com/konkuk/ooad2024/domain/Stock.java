package com.konkuk.ooad2024.domain;

import java.util.Objects;

public class Stock implements Comparable<Stock> {

    private static final String INPUT_INVALID_MESSAGE = "유효하지 않은 하루입니다. 다시 입력해 주세요.";
    private static final int INITIAL_COUNT = 0;
    private static final int MAX_COUNT = 100;

    private final int value;

    public Stock(int value) {
        if(isInvalidInput(value)) {
            throw new IllegalArgumentException(INPUT_INVALID_MESSAGE);
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Stock o) {
        return this.value - o.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return getValue() == stock.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    private boolean isInvalidInput(int value) {
        return value < INITIAL_COUNT || value > MAX_COUNT;
    }
}
