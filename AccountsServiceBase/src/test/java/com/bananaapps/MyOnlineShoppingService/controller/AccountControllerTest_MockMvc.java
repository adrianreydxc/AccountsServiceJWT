package com.bananaapps.MyOnlineShoppingService.controller;

import com.bananaapps.MyOnlineShoppingService.domain.entities.Account;
import com.bananaapps.MyOnlineShoppingService.domain.entities.Customer;
import com.bananaapps.MyOnlineShoppingService.util.JsonUtil;
import com.bananaapps.MyOnlineShoppingService.domain.dto.response.AccountDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("testing")
@Sql(value = "classpath:data.sql")
public class AccountControllerTest_MockMvc {



    @Autowired
    private MockMvc mockMvc;

//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
//    }

    @Test
    void create_allOk() throws Exception {
        AccountDto newAccount = new AccountDto(null, "Personal", LocalDate.now(), 1000.0, 1L);
        String accountJson = JsonUtil.asJsonString(newAccount);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.owner_id").value(1L))
                .andExpect(jsonPath("$.type").value("Personal"));
    }

    @Test
    void create_WrongValidation() throws Exception {
        AccountDto invalidAccount = new AccountDto(1L, "Personalitaaaasf", LocalDate.now(), 1000.0, 1L);
        String accountJson = JsonUtil.asJsonString(invalidAccount);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isNotFound());
    }
    @Test
    void getAccountById_AllOk() throws Exception {
        mockMvc.perform(get("/accounts/user/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id", is(greaterThanOrEqualTo(1))));
    }


    @Test
    void getAccountById_WrongId() throws Exception {
        // Simulamos que el servicio retorna un error 404 para un ID no existente
        mockMvc.perform(get("/accounts/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAccount_AllOK() throws Exception {
        Account accountToDelete = new Account(1L, "Personal", LocalDate.now(), 1000.0, null);
        String accountJson = JsonUtil.asJsonString(accountToDelete);

        mockMvc.perform(delete("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotModified());
    }


    @Test
    void deleteAccount_WrongId() throws Exception {
        Customer customer = new Customer(null, "Daniel", "correo@email.com");
        Account accountToDelete = new Account(null, "NonExistent", LocalDate.now(), 1000.0, customer);
        String accountJson = JsonUtil.asJsonString(accountToDelete);

        mockMvc.perform(delete("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isNotFound());
    }

}
