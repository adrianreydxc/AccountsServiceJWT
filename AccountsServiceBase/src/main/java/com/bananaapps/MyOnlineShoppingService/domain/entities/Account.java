package com.bananaapps.MyOnlineShoppingService.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Schema(name = "Account", description = "Account model")
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Account ID", example = "1", required = true)
    private Long id;

    @NotNull(message = "La cuenta no puede ser nula")
    @NotEmpty(message = "La cuenta no puede estar vacía")
    @Pattern(regexp = "Personal|Company", message = "Solo se admiten cuentas Personal o Company")
    @Schema(name = "Account type", example = "Personal o Company", required = true)
    private String type;

    @NotNull
    @PastOrPresent(message = "La fecha de apertura no puede ser posterior al presente")
    @Schema(name = "Account's oppening day", example = "2007-12-03", required = true)
    private LocalDate openingDate;

    @PositiveOrZero(message = "El balance no puede ser negativo")
    @Schema(name = "Account balance", example = "0.00, 5.50, 1000.00")
    private double balance;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @Schema(name = "Owner", description = "Owner of the account")
    private Customer owner;
}
