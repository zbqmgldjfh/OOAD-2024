package com.konkuk.ooad2024.controller;

import com.konkuk.ooad2024.domain.*;
import com.konkuk.ooad2024.dto.PrePaymentResponseDto;
import com.konkuk.ooad2024.service.Beverages;
import com.konkuk.ooad2024.service.OtherDVMs;
import com.konkuk.ooad2024.service.PaymentMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(DVM.class)
public class DVMTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private Beverages beverages;
  @MockBean
  private OtherDVMs otherDVMs;
  @MockBean
  private PaymentMachine paymentMachine;
  @MockBean
  private Position position;
  @MockBean
  private Bank bank;

  @Test
  void 컨트롤러_인증코드_음료수_반환_테스트() throws Exception{
    // Given
    Beverage beverage = new Beverage(BeverageName.COKE, 1, 1);
    String jsonRequest = "{\"authenticationCode\":\"1234567890\"}";

    // When
    when(paymentMachine.getPrePaiedBeverage(any(String.class))).thenReturn(beverage);

    // Then
    mockMvc.perform(post("/paiedBeverages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.beverageId").value("01"))
            .andExpect(jsonPath("$.quantity").value(1));
  }

  @Test
  void 컨트롤러_선결제_성공_테스트() throws Exception {
    // Given
    long accountId = 1L;
    BeverageName beverageName = BeverageName.COKE;
    int quantity = 2;
    long price = 500;
    long totalAmount = quantity * price;
    Position position = new Position(10, 20);
    PrePaymentResponseDto responseDto = new PrePaymentResponseDto(true, "abcdefghij");

    String jsonRequest = String.format("{\"accountId\": %d, \"beverageId\": \"%s\", \"quantity\": %d, \"x\": %d, \"y\": %d}",
            accountId, beverageName.getCode(), quantity, position.getXaxis(), position.getYaxis());

    // When
    when(beverages.findPriceByName(beverageName)).thenReturn(price);
    when(bank.balanceCheck(accountId, totalAmount)).thenReturn(true);
    when(otherDVMs.findByPosition(any(Position.class))).thenReturn(new OtherDVM(position,"127.0.0.1",9999,"1"));
    when(paymentMachine.prePayment(any(Position.class), any(Beverage.class))).thenReturn(responseDto);

    // Then
    mockMvc.perform(post("/prepay")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.haveBalance").value(true))
            .andExpect(jsonPath("$.authenticationCode").value("abcdefghij"));
  }

  @Test
  void 컨트롤러_선결제_잔액부족_테스트() throws Exception {
    // Given
    long accountId = 1L;
    BeverageName beverageName = BeverageName.COKE;
    int quantity = 2;
    long price = 500;
    long totalAmount = quantity * price;
    Position position = new Position(10, 20);

    String jsonRequest = String.format("{\"accountId\": %d, \"beverageId\": \"%s\", \"quantity\": %d, \"x\": %d, \"y\": %d}",
            accountId, beverageName.getCode(), quantity, position.getXaxis(), position.getYaxis());

    // When
    when(beverages.findPriceByName(beverageName)).thenReturn(price);
    when(bank.balanceCheck(accountId, totalAmount)).thenReturn(false);

    // Then
    mockMvc.perform(post("/prepay")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.haveBalance").value(false))
            .andExpect(jsonPath("$.authenticationCode").doesNotExist());
  }


}
