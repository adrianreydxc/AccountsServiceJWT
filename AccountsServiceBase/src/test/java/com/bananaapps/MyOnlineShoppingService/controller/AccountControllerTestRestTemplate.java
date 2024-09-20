package com.bananaapps.MyOnlineShoppingService.controller;


import com.bananaapps.MyOnlineShoppingService.domain.dto.request.LoanDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.request.MoneyTransactionsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("testing")
@Sql("classpath:data.sql")
public class AccountControllerTestRestTemplate {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void givenValidRequest_whenAddMoney_thenReturnCreatedAndTrue() {
        // Given
        MoneyTransactionsDto request = new MoneyTransactionsDto();
        request.setIdUser(3L);
        request.setIdAccount(1L);
        request.setAmount(100.0);

        String url = "http://localhost:" + port + "/accounts/addMoney";

        HttpEntity<MoneyTransactionsDto> entity = new HttpEntity<>(request);

        // When
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Boolean.class);

        // Then
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.CREATED)));
        assertThat(response.getBody(), is(true));
        assertThat(response.getHeaders().getContentType(), is(equalTo(MediaType.APPLICATION_JSON)));
        assertThat(response.getBody(), is(notNullValue()));
    }

    @Test
    public void givenValidRequest_whenWithdrawMoney_thenReturnOkAndTrue() {
        // Given
        MoneyTransactionsDto request = new MoneyTransactionsDto();
        request.setIdAccount(1L);
        request.setIdUser(1L);
        request.setAmount(10.0);

        String url = "http://localhost:" + port + "/accounts/withdrawMoney";

        HttpEntity<MoneyTransactionsDto> entity = new HttpEntity<>(request);

        // When
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Boolean.class);

        // Then
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.CREATED)));
        assertThat(response.getBody(), is(false));
    }

    @Test
    public void givenValidUserAndLoanDto_whenCheckLoan_thenReturnOkAndTrue() {
        // Given
        Long userId = 1L;
        LoanDto loanDto = LoanDto.builder()
                .amount(1000.0)
                .build();

        String url = "http://localhost:" + port + "/accounts/loan/" + userId;

        HttpEntity<LoanDto> entity = new HttpEntity<>(loanDto);

        // When
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.POST, entity, Boolean.class);

        // Then
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.OK)));
        assertThat(response.getBody(), is(true));
        assertThat(response.getHeaders().getContentType(), is(equalTo(MediaType.APPLICATION_JSON)));
        assertThat(response.getBody(), is(notNullValue()));
    }



}
