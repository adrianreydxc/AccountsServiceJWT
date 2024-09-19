package com.bananaapps.MyOnlineShoppingService.domain.mappers;

import com.bananaapps.MyOnlineShoppingService.domain.dto.response.AccountDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.response.CustomerDto;
import com.bananaapps.MyOnlineShoppingService.domain.entities.Account;
import com.bananaapps.MyOnlineShoppingService.domain.entities.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public static CustomerDto toDto(Customer customer) {

        if (customer == null) {
            return null;
        }

        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getName())
                .build();
    }

    public static Customer toEntity(CustomerDto customerDto) {
        if (customerDto == null) {
            return null;
        }

        return Customer.builder()
                .id(customerDto.getId())
                .name(customerDto.getName())
                .email(customerDto.getName())
                .build();
    }
}
