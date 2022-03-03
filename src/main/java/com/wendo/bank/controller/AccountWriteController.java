package com.wendo.bank.controller;

import com.wendo.bank.dto.*;
import com.wendo.bank.service.PermissionFacade;
import com.wendo.bank.service.account.AccountWriteOperations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account/w")
@ConditionalOnProperty(name = "account.write.enabled", havingValue = "true", matchIfMissing = true)
public class AccountWriteController {


    private final AccountWriteOperations accountService;
    private final PermissionFacade permissionFacade;

    public AccountWriteController(AccountWriteOperations accountService, PermissionFacade permissionFacade) {
        this.accountService = accountService;
        this.permissionFacade = permissionFacade;
    }

    @Operation(summary = "Add account to customer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the account details",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request, Invalid customer number, Invalid details"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not found")})
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AccountDto addAccount(@RequestBody AccountCreationDto accountCreationDto) {
        return accountService.createAccount( accountCreationDto);
    }

    @Operation(summary = "Update account details for customer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the updated account details",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request, Invalid customer number, Invalid account number"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer or account not found")})
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AccountDto updateAccount(@RequestBody AccountUpdationDto accountUpdationDto) {
        permissionFacade.authenticateUserAndAccount(accountUpdationDto.getCustomerNumber(), accountUpdationDto.getPin(), accountUpdationDto.getAccountNumber());
        return accountService.updateAccount(accountUpdationDto.getCustomerNumber(),  accountUpdationDto);
    }

    @Operation(summary = "Close/Block/Activate account on customer request")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "account details with updated state",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request, Invalid customer number, Invalid account number"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer or account not found")})
    @PostMapping(value = "/update/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AccountDto updateAccountStatus(@RequestBody AccountStateChangeRequestDto accountDeletionRequestDto) {
        permissionFacade.authenticateManager(accountDeletionRequestDto.getManagerNumber());
        return accountService.updateStatusOfAccount(accountDeletionRequestDto);

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
    @PostMapping(value = "/update/card/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CardDto updateCardStatus(@RequestBody CardStateChangeDto cardStateChangeDto) {
        permissionFacade.authenticateManager(cardStateChangeDto.getManagerNumber());
        return accountService.updateStatusOfCard(cardStateChangeDto);

    }

}
