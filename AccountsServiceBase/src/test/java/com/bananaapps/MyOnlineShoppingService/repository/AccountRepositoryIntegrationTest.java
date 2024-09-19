package com.bananaapps.MyOnlineShoppingService.repository;

import com.bananaapps.MyOnlineShoppingService.domain.entities.Account;
import com.bananaapps.MyOnlineShoppingService.domain.exception.custom.AccountsByUserException;
import com.bananaapps.MyOnlineShoppingService.domain.repositories.AccountServiceRepository;
import org.hibernate.type.IntegerType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountServiceRepository accountServiceRepository;

    @Test
    void getAccountsByUser_AllOk() {
        List<Account> accounts = accountServiceRepository.getAccountsByUser(1L).orElseThrow(
                () -> new AccountsByUserException("This exception shouldn't be visible")
        );
        assertThat(accounts.size(), greaterThan(0));
        assertThat(accounts.get(0).getBalance(), equalTo(1000.0));
        assertThat(accounts.get(0).getType(), equalTo("Personal"));
        assertThat(accounts.get(0).getOwner().getId(), equalTo(1L));
    }

    @Test
    void getAccountByUser_WrongId() {
        List<Account> accounts = accountServiceRepository.getAccountsByUser(1000L).orElseThrow(
                () -> new AccountsByUserException("This exception shouldn't be visible")
        );
        assertThat(accounts.size(), equalTo(0));
    }

    @Test
    void retirarDineroConOtraCuentaDineroDeMas_ok() {
        assertThat(
                accountServiceRepository.doWithDrawnEvenWithAnotherAccountUser(
                        1L,
                        2L,
                        1002
                ), equalTo(true)
        );
    }
    @Test
    void retirarDineroConOtraCuentaDineroDeMenos_ok() {
        assertThat(
                accountServiceRepository.doWithDrawnEvenWithAnotherAccountUser(
                        1L,
                        2L,
                        100
                ), equalTo(false)
        );
    }
    @Test
    void comprobarQueSeBorranCuentasUsuario() {
        List<Account> accounts = accountServiceRepository.getAccountsByUser(1L).orElseThrow(
                () -> new AccountsByUserException("This exception shouldn't be visible")
        );
        accountServiceRepository.deleteAllAccountsByUser(1L);
        List<Account> accounts2 = accountServiceRepository.getAccountsByUser(1L).orElseThrow(
                () -> new AccountsByUserException("This exception shouldn't be visible")
        );
        assertThat(accounts.size(), greaterThan(accounts2.size()));
        assertThat(accounts2.size(), equalTo(IntegerType.ZERO));
    }



    @Test
    void findAccount_AllOk() {
        Account account = accountServiceRepository.findById(1L).orElseThrow(
                () -> new AccountsByUserException("Usuario no encontrado")
        );
        assertThat(account, notNullValue());

    }

    @Test
    void findAccount_WrongId() {
        assertThrows(AccountsByUserException.class, () -> accountServiceRepository.findById(1000L).orElseThrow(
                () -> new AccountsByUserException("Usuario no encontrado")
        ));
    }
}
