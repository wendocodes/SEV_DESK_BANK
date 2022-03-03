package com.wendo.bank.controller;

import com.wendo.bank.dto.TransactionDto;
import com.wendo.bank.dto.UserDto;
import com.wendo.bank.exception.ValidationException;
import com.wendo.bank.service.TransactionService;
import com.wendo.bank.service.account.AccountReadOperations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountReadOperations accountReadOperations;

    public TransactionController(TransactionService transactionService, AccountReadOperations accountReadOperations) {
        this.transactionService = transactionService;
        this.accountReadOperations = accountReadOperations;
    }

    @Operation(summary = "View transactions of account by account number")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transactions done by customer on specific account",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid data supplied")})
    @GetMapping(value = "/")
    public List<TransactionDto> viewTransactionsOfAccount(@RequestParam("cid") String accountNumber) {
        if(!accountReadOperations.isAccountNumberValid(accountNumber)) throw new ValidationException("Invalid account Number : %s ", accountNumber);
        return transactionService.viewTransactions(accountNumber);
    }

    @Operation(summary = "View transactions of account by reference number")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transaction done by customer on specific account",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid data supplied")})
    @GetMapping(value = "/ref")
    public TransactionDto viewTransactionByRef(@RequestParam("ref") String ref) {
        return transactionService.viewTransaction(ref);
    }
}
