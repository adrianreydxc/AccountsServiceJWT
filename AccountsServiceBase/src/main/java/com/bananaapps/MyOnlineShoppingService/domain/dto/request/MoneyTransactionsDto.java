package com.bananaapps.MyOnlineShoppingService.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransactionsDto {
    private Long idAccount;
    private Long idUser;
    private double amount;
}
