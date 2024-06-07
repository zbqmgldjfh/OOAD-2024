package com.konkuk.ooad2024.domain;

import lombok.Getter;

@Getter
public class Position {
  private static final int AXIS_RANGE_LOWER = 0;
  private static final int AXIS_RANGE_UPPER = 100;
  private static final String INVALID_AXIS_MESSAGE = "유효하지 않은 범위의 자표입니다.";

  private final int xaxis;
  private final int yaxis;

  public Position(int xaxis, int yaxis) {
    isValidInput(xaxis, yaxis);

    this.xaxis = xaxis;
    this.yaxis = yaxis;
  }

  public double getDistance(Position other) {
    return Math.sqrt(Math.pow(this.xaxis - other.xaxis, 2) + Math.pow(this.yaxis - other.yaxis, 2));
  }

  private void isValidInput(int xaxis, int yaxis) {
    isValidAxis(xaxis);
    isValidAxis(yaxis);
  }

  private void isValidAxis(int axis) {
    if (axis < AXIS_RANGE_LOWER || axis >= AXIS_RANGE_UPPER) {
      throw new IllegalArgumentException(INVALID_AXIS_MESSAGE);
    }
  }
}
