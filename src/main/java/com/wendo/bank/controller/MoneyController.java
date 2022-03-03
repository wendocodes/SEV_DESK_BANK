package com.wendo.bank.controller;

import com.wendo.bank.dto.*;
import com.wendo.bank.service.MoneyService;
import com.wendo.bank.service.PermissionFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/money")
public class MoneyController {

    private final PermissionFacade permissionFacade;
    private  final MoneyService moneyService;

    public MoneyController(PermissionFacade permissionFacade,
                           MoneyService moneyService) {
        this.permissionFacade = permissionFacade;
        this.moneyService = moneyService;
    }


    @Operation(summary = "Deposit amount to customer account")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the transaction details",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountCreationDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request, Invalid customer number, Invalid details"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not found")})
    @PostMapping(value = "/deposit")
    public TransactionDto depositAmount(@RequestBody MoneyRequestDto moneyRequestDto) {
        permissionFacade.authenticateUserAndAccount(moneyRequestDto.getCustomerNumber(), moneyRequestDto.getPin(), moneyRequestDto.getAccountNumber());
        return moneyService.deposit(moneyRequestDto);

    }


    @Operation(summary = "Withdraw amount from customer account")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the transaction details",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountCreationDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request, Invalid customer number, Invalid details"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not found")})
    @PostMapping(value = "/withdraw")
    public TransactionDto withdrawAmount(@RequestBody MoneyRequestDto moneyRequestDto) {
        permissionFacade.authenticateUserAndAccount(moneyRequestDto.getCustomerNumber(), moneyRequestDto.getPin(), moneyRequestDto.getAccountNumber());
        return moneyService.withdraw(moneyRequestDto);
    }

    @Operation(summary = "Transfer amount from customer account")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the transaction details",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountCreationDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request, Invalid customer number, Invalid details"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not found")})
    @PostMapping(value = "/transfer")
    public TransactionDto transferAmount(@RequestBody MoneyTransferRequestDto moneyTransferRequestDto) {
        permissionFacade.authenticateUserAndAccount(moneyTransferRequestDto.getCustomerNumber(), moneyTransferRequestDto.getPin(), moneyTransferRequestDto.getAccountNumber());
        if(moneyTransferRequestDto.isExternalAccount()) {
            return moneyService.externalTransfer(moneyTransferRequestDto);
        } else {
            return moneyService.transfer(moneyTransferRequestDto);
        }
    }

    @Operation(summary = "Acknowledges the external transaction api")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the transaction details",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountCreationDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request, Invalid customer number, Invalid details"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not found")})
    @PostMapping(value = "/transfer/ack")
    public TransactionDto transferAmount(@RequestBody MoneyTransferAckDto moneyTransferAckDto) {
        return moneyService.externalTransferAcknowledge(moneyTransferAckDto);
    }


}
