package com.wendo.bank.controller;

import com.wendo.bank.dto.AccountDto;
import com.wendo.bank.enitity.Account;
import com.wendo.bank.repo.AccountRepository;
import com.wendo.bank.service.account.AccountReadOperations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account/r")
@ConditionalOnProperty(name = "account.read.enabled", havingValue = "true", matchIfMissing = true)
public class AccountReadController {
    private final AccountReadOperations accountService;


    public AccountReadController(AccountReadOperations accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Close/Block/Activate card")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "card details with updated state",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request, Invalid customer number, Invalid account number, Invalid card Number"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer or account or Card not found")})
    @GetMapping(value = "/all")
    public List<AccountDto> viewAccountDetails(@RequestParam("cid") String customerNumber) {
        return accountService.getByCustomer(customerNumber);
    }

    @Operation(summary = "Close/Block/Activate card")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "card details with updated state",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request, Invalid customer number, Invalid account number, Invalid card Number"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer or account or Card not found")})
    @GetMapping(value = "/view")
    public AccountDto viewAccountDetails(@RequestParam("cid") String customerNumber,@RequestParam("aid") String accountNumber) {
        return accountService.getDetails(customerNumber,accountNumber);
    }


}
