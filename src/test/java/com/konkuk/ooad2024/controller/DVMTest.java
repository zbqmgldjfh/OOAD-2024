package com.konkuk.ooad2024.controller;

import com.konkuk.ooad2024.domain.AuthenticationCode;
import com.konkuk.ooad2024.domain.Beverage;
import com.konkuk.ooad2024.domain.BeverageName;
import com.konkuk.ooad2024.domain.Position;
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

  @BeforeEach
  void setUp() {
  }

  @Test
  void 컨트롤러_인증코드_음료수_반환_테스트() throws Exception{
    // Given
    Beverage beverage = new Beverage(BeverageName.COKE, 1, 1);
    String jsonRequest = "{\"authenticationCode\":\"1234567890\"}";

    // When
    when(paymentMachine.getPrePaiedBeverage(any(AuthenticationCode.class))).thenReturn(beverage);

    // Then
    mockMvc.perform(post("/paiedBeverages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.beverageId").value("01"))
            .andExpect(jsonPath("$.quantity").value(1));
  }
}