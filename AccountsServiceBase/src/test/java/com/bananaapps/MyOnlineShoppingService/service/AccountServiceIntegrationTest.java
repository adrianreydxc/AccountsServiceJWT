package com.bananaapps.MyOnlineShoppingService.service;

import com.bananaapps.MyOnlineShoppingService.domain.dto.request.LoanDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.request.MoneyTransactionsDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.response.AccountDto;
import com.bananaapps.MyOnlineShoppingService.domain.entities.Account;
import com.bananaapps.MyOnlineShoppingService.domain.exception.custom.*;
import com.bananaapps.MyOnlineShoppingService.domain.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
@Rollback
public class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Test
    void getAllAccounts_OK() throws Exception {
        List<AccountDto> list = accountService.getAllAcounts();
        assertThat(list, notNullValue());
        assertThat(list.size(), greaterThan(0));
        assertThat(list.get(0).getBalance(), equalTo(1000.0));
        assertThat(list.get(0).getType(), equalTo("Personal"));
        assertThat(list.get(0).getOwner_id(), equalTo(1L));
    }

    @Test
    void getAccountById_AllOk() throws Exception {
        AccountDto account = accountService.getAccountById(1L, 1L);
        assertThat(account, notNullValue());
        assertThat(account.getBalance(), equalTo(1000.0));
        assertThat(account.getType(), equalTo("Personal"));
        assertThat(account.getOwner_id(), equalTo(1L));
    }

    @Test
    void getAccountById_NoExistId() throws Exception {
        assertThrows(NoSuchAccountException.class, () -> {
            accountService.getAccountById(23232L, 1L);
        });
    }

    @Test
    void getAccountById_WrongUserId(){
        assertThrows(NoSuchAccountException.class, () -> {
            accountService.getAccountById( 1L, 342342343L);
        });
    }

    @Test
    void getAccountsByUser_AllOK() throws Exception {
        List<AccountDto> accounts = accountService.getAccountsByUser(1L);
        assertThat(accounts, notNullValue());
        assertThat(accounts.size(), greaterThan(0));
        assertThat(accounts.get(0).getBalance(), equalTo(1000.0));
        assertThat(accounts.get(0).getType(), equalTo("Personal"));
        assertThat(accounts.get(0).getOwner_id(), equalTo(1L));
    }

    @Test
    void getAccountsByUser_WrongUserId() throws Exception {
      List<AccountDto> list = accountService.getAccountsByUser(23423432432L);
        assertThat(list.size(), equalTo(0));
    }

        @Test
    void doWithdraw_EnoughMoneyInMainAccount_AllOk() throws Exception {
        MoneyTransactionsDto request = MoneyTransactionsDto.builder()
                .idAccount(1L)
                .idUser(1L)
                .amount(90)
                .build();

        assertThat(accountService.doWithdraw(request), equalTo(false));
    }

    @Test
    void doWithdraw_NotEnoughMoneyInMainAccount_AllOk() throws Exception {
        MoneyTransactionsDto request = MoneyTransactionsDto.builder()
                .idAccount(1L)
                .idUser(1L)
                .amount(1001)
                .build();

        assertThat(accountService.doWithdraw(request), equalTo(true));
    }

    @Test
    void doWithdraw_NotEnoughMoneyInAllAccount_GetException(){
        MoneyTransactionsDto request = MoneyTransactionsDto.builder()
                .idAccount(1L)
                .idUser(1L)
                .amount(2300)
                .build();
        assertThrows(InsuficientBalanceException.class, () -> {
            accountService.doWithdraw(request);
        });
    }

    @Test
    void addMoney_AllOk(){
        MoneyTransactionsDto request = MoneyTransactionsDto.builder()
                .idAccount(1L)
                .idUser(1L)
                .amount(2300)
                .build();

        assertThat(accountService.addMoney(request), equalTo(true));
    }

    @Test
    void addMoney_erroIdAccount(){
        MoneyTransactionsDto request = MoneyTransactionsDto.builder()
                .idAccount(2341L)
                .idUser(1L)
                .amount(2300)
                .build();

        assertThrows(AddMoneyException.class, () -> {
            accountService.addMoney(request);
        });
    }

    @Test
    void checkLoan_AllOk(){
        LoanDto loanDto = LoanDto.builder()
                .amount(100)
                .id(1L)
                .build();

        assertThat(accountService.checkLoan(loanDto), equalTo(true));
    }
    
    @Test
    void checkLoand_NotEnoughTotalBalance(){
        LoanDto loanDto = LoanDto.builder()
                .amount(103434330)
                .id(1L)
                .build();

        assertThrows(LoanException.class, () -> {
            accountService.checkLoan(loanDto);
        });
    }

    @Test
    void checkLoand_ErrorUserId(){
        LoanDto loanDto = LoanDto.builder()
                .amount(100)
                .id(13434L)
                .build();

        assertThrows(LoanException.class, () -> {
            accountService.checkLoan(loanDto);
        });
    }
}
