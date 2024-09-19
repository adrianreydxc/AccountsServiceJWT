package com.bananaapps.MyOnlineShoppingService.domain.controllers;

import com.bananaapps.MyOnlineShoppingService.domain.dto.request.LoanDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.request.MoneyTransactionsDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.response.AccountDto;
import com.bananaapps.MyOnlineShoppingService.domain.entities.Account;
import com.bananaapps.MyOnlineShoppingService.domain.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(
        value = "/accounts",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE}
)
@Validated
@Tag(name = "Account Management", description = "Operations related to managing accounts, including CRUD operations, balance management, and loan eligibility checks.")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping()
    @Operation(summary = "Get all accounts")
    @ApiResponse(responseCode = "200", description = "Tag added successfully")
    public ResponseEntity<List<AccountDto>> getAccounts() throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAllAcounts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Fetches an account by its ID and the user associated with it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<AccountDto> getAccountById(@PathVariable("id") Long id, @RequestParam("user") Long userId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountById(id, userId));
    }

    @GetMapping("user/{id}")
    @Operation(summary = "Get accounts by user ID", description = "Fetches all accounts associated with a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found or no accounts available",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<AccountDto>> getAccountByUserId(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountsByUser(id));
    }
    @PostMapping()
    @Operation(summary = "Create a new account", description = "Creates a new account for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountDto account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(account));
    }

    @PutMapping()
    @Operation(summary = "Update an account", description = "Updates an existing account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDto.class))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<AccountDto> updateAccount(@RequestBody AccountDto account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.updateAccount(account));
    }

    @DeleteMapping()
    @Operation(summary = "Delete an account", description = "Deletes an existing account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Boolean> deleteAccount(@RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(accountService.deleteAccount(account));
    }

    @PutMapping("/addMoney")
    @Operation(summary = "Add money to an account", description = "Adds a specified amount of money to an account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money added successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Boolean> addMoney(@RequestBody MoneyTransactionsDto request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.addMoney(request));
    }

    @PutMapping("/withdrawMoney")
    @Operation(summary = "Withdraw money from an account", description = "Withdraws a specified amount of money from an account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money withdrawn successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Insufficient balance or invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Boolean> withdrawMoney(@RequestBody MoneyTransactionsDto request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.doWithdraw(request));
    }

    @DeleteMapping("/deleteAllFromUser/{id}")
    @Operation(summary = "Delete all accounts from a user", description = "Deletes all accounts associated with a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found or no accounts to delete",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Boolean> deleteAllFromUser(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(accountService.deleteAllAccountByUser(id));
    }

    @PostMapping("/loan/{userId}")
    @Operation(summary = "Check loan eligibility", description = "Checks if a user is eligible for a loan based on their account balance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan eligibility checked successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Loan request invalid",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found or no accounts",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Boolean> isLoanAccepted(@PathVariable("userId") Long userId, @RequestBody LoanDto body) {
        body.setId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(accountService.checkLoan(body));
    }

}
