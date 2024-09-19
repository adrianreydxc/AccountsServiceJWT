package com.bananaapps.MyOnlineShoppingService.controller;

import com.bananaapps.MyOnlineShoppingService.domain.controllers.AccountController;
import com.bananaapps.MyOnlineShoppingService.domain.dto.request.LoanDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.response.AccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("testing")
@Sql(value = "classpath:data.sql")
public class AccountControllerTest {

    @Autowired
    private AccountController accountController;

    @Test
    void givenValidAccount_whenCreateAccount_thenIsCreatedAndReturnsId() {
        AccountDto newAccount = new AccountDto(null, "Personal", LocalDate.now(), 100.00, 1L);
        ResponseEntity<AccountDto> response = accountController.createAccount(newAccount);
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getBody().getId()).isNotNull();
    }

}
