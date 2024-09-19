package com.bananaapps.MyOnlineShoppingService.domain.mappers;

import com.bananaapps.MyOnlineShoppingService.domain.dto.response.AccountDto;
import com.bananaapps.MyOnlineShoppingService.domain.entities.Account;
import com.bananaapps.MyOnlineShoppingService.domain.entities.Customer;
import com.bananaapps.MyOnlineShoppingService.domain.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {
    private final CustomerService customerService;
    public AccountDto toDto(Account account) {

        if(account == null) {
            return null;
        }

        return AccountDto.builder()
                .id(account.getId())
                .type(account.getType())
                .openingDate(account.getOpeningDate())
                .owner_id(CustomerMapper.toDto(account.getOwner()).getId())
                .balance(account.getBalance())
                .build();
    }

    public static Account toEntity(AccountDto accountDto, Customer customer) {
        if (accountDto == null) {
            return null;
        }

        Account account = new Account();
        account.setId(accountDto.getId());
        account.setType(accountDto.getType());
        account.setOpeningDate(accountDto.getOpeningDate());
        account.setOwner(customer); // Asume que tienes un CustomerMapper
        account.setBalance(accountDto.getBalance());
        return account;
    }

}
