package com.bananaapps.MyOnlineShoppingService.service;

import com.bananaapps.MyOnlineShoppingService.domain.dto.request.LoanDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.request.MoneyTransactionsDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.response.AccountDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.response.CustomerDto;
import com.bananaapps.MyOnlineShoppingService.domain.entities.Account;
import com.bananaapps.MyOnlineShoppingService.domain.entities.Customer;
import com.bananaapps.MyOnlineShoppingService.domain.exception.custom.AccountsByUserException;
import com.bananaapps.MyOnlineShoppingService.domain.exception.custom.InsuficientBalanceException;
import com.bananaapps.MyOnlineShoppingService.domain.exception.custom.NoSuchAccountException;
import com.bananaapps.MyOnlineShoppingService.domain.mappers.AccountMapper;
import com.bananaapps.MyOnlineShoppingService.domain.repositories.AccountServiceRepository;
import com.bananaapps.MyOnlineShoppingService.domain.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.time.LocalDate;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AccountServiceMockTest {

    @Mock
    private AccountServiceRepository accountServiceRepository;
    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setup() throws Exception {
        Customer customer = new Customer();
        customer.setEmail("adri@adri.com");
        customer.setId(1L);
        customer.setName("Adri");
        Account account = new Account(1L,"Type", LocalDate.now().minusDays(2),1000, customer);
        List<Account> accounts = List.of(account);
        AccountDto accountDto = new AccountDto(1L,"Type", LocalDate.MIN,1000, 1L);
        List<AccountDto> dtoList = List.of(accountDto);

        Mockito.when(accountMapper.toDto(account)).thenReturn(accountDto);
        Mockito.when(accountServiceRepository.findAll()).thenReturn(accounts);
        Mockito.when(accountServiceRepository.findById(1L)).thenReturn(java.util.Optional.of(account));
        Mockito.when(accountServiceRepository.getAccountsByUser(1L)).thenReturn(java.util.Optional.of(accounts));
    }

    @Test
    void getAllAccounts_OK() throws Exception {
        List<AccountDto> list = accountService.getAllAcounts();
        assertThat(list, notNullValue());
        assertThat(list.get(0).getBalance(), equalTo(1000.0));
        assertThat(list.get(0).getType(), equalTo("Type"));
        assertThat(list.get(0).getOwner_id(), equalTo(1L));
        assertThat(list.get(0).getId(), equalTo(1L));
    }

    @Test
    void getAccountById_AllOk(){
        AccountDto result = accountService.getAccountById(1L, 1L);
        assertThat(result, notNullValue());
        assertThat(result.getBalance(), equalTo(1000.0));
        assertThat(result.getType(), equalTo("Type"));
        assertThat(result.getOwner_id(), equalTo(1L));
        assertThat(result.getId(), equalTo(1L));
    }

    @Test
    void getAccountById_NoExistId(){
        assertThrows(NoSuchAccountException.class, () -> {
           accountService.getAccountById(12322L, 1L);
        });
    }

    @Test
    void getAccountById_WrongUserId(){
        assertThrows(NoSuchAccountException.class, () -> {
           accountService.getAccountById(1L, 12322L);
        });
    }

    @Test
    void getAccountsByUser_AllOK(){
        List<AccountDto> result = accountService.getAccountsByUser(1L);
        assertThat(result, notNullValue());
        assertThat(result.get(0).getBalance(), equalTo(1000.0));
        assertThat(result.get(0).getType(), equalTo("Type"));
        assertThat(result.get(0).getOwner_id(), equalTo(1L));
        assertThat(result.get(0).getId(), equalTo(1L));
    }

    @Test
    void getAccountsByUser_WrongUserId(){
       assertThrows(AccountsByUserException.class, () -> {
          accountService.getAccountsByUser(23434L);
       });
    }

    @Test
    void doWithdraw_EnoughMoneyInMainAccount_AllOk(){
        Mockito.when(accountServiceRepository.doWithDrawnEvenWithAnotherAccountUser(1L, 1L, 900)).thenReturn(false);

        boolean result = accountServiceRepository.doWithDrawnEvenWithAnotherAccountUser(1l, 1L, 900);

        assertThat(result, equalTo(false));
    }

    @Test
    void doWithdraw_NotEnoughMoneyInMainAccount_AllOk(){
        Mockito.when(accountServiceRepository.doWithDrawnEvenWithAnotherAccountUser(1L, 1L, 1002)).thenReturn(true);

        boolean result = accountServiceRepository.doWithDrawnEvenWithAnotherAccountUser(1l, 1L, 1002);

        assertThat(result, equalTo(true));
    }

    @Test
    void doWithdraw_NotEnoughMoneyInAllAccount_GetException(){
        Mockito.when(accountServiceRepository.doWithDrawnEvenWithAnotherAccountUser(1L, 1L, 2002))
                .thenThrow(InsuficientBalanceException.class);

        assertThrows(InsuficientBalanceException.class, () -> {
            accountServiceRepository.doWithDrawnEvenWithAnotherAccountUser(1l, 1L, 2002);
        });
    }
}
