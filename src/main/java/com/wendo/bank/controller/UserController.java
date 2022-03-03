package com.wendo.bank.controller;

import com.wendo.bank.dto.UserDto;
import com.wendo.bank.exception.ResourceNotFoundException;
import com.wendo.bank.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "Creates a customer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Created the user",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid data supplied")})
    @PostMapping(value = "/create")
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info("Create User : {}", userDto);
        return userService.createUser(userDto);
    }

    @Operation(summary = "View customer details")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the customer details",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request, Invalid customer number")})
    @GetMapping(value = "/view")
    public UserDto viewUserDetails(@RequestParam("cid") String customerNumber) {
        return userService.getUserDetails(customerNumber);
    }

    @Operation(summary = "Update customer details")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the updated customer details",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request, Invalid customer number"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found, Invalid customer number"),})
    @PutMapping(value = "/update")
    public UserDto updateUser(@RequestParam("cid") String customerNumber, @RequestBody UserDto userDto) {
        return userService.updateUserDetails(customerNumber, userDto);
    }

    @Operation(summary = "Delete customer details")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the deleted customer details",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request, Invalid customer number, active accounts available"),})
    @DeleteMapping(value = "/delete")
    public UserDto deleteUser(@RequestParam("cid") String customerNumber) {

        return userService.deleteUser(customerNumber);

    }
}
