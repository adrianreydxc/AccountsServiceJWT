package com.bananaapps.MyOnlineShoppingService.domain.services;

import com.bananaapps.MyOnlineShoppingService.domain.dto.request.LoanDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.request.MoneyTransactionsDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.response.AccountDto;
import com.bananaapps.MyOnlineShoppingService.domain.entities.Account;

import java.util.List;

public interface AccountService {
    List<AccountDto> getAllAcounts() throws Exception;
    AccountDto getAccountById(Long id, Long userId) throws Exception;
    List<AccountDto> getAccountsByUser(Long userId) throws Exception;
    AccountDto createAccount(AccountDto account);
    AccountDto updateAccount(AccountDto account);
    boolean deleteAccount(Account account);
    boolean doWithdraw(MoneyTransactionsDto withdrawDto) throws Exception;
    boolean deleteAllAccountByUser(Long userId);
    boolean addMoney(MoneyTransactionsDto request);
    boolean checkLoan(LoanDto loanDto);
}
