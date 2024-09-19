package com.bananaapps.MyOnlineShoppingService.domain.dto.response;

import com.bananaapps.MyOnlineShoppingService.domain.entities.Customer;
import com.bananaapps.MyOnlineShoppingService.domain.exception.custom.NoSuchCustomerException;
import com.bananaapps.MyOnlineShoppingService.domain.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private Long id;

    @NotNull(message = "La cuenta no puede ser nula")
    @NotEmpty(message = "La cuenta no puede estar vacía")
    @Pattern(regexp = "Personal|Company", message = "Solo se admiten cuentas Personal o Company")
    private String type;

    @NotNull
    @PastOrPresent(message = "La fecha de apertura no puede ser posterior al presente")
    private LocalDate openingDate;

    @PositiveOrZero(message = "El balance no puede ser negativo")
    private double balance;

    private Long owner_id;
}
