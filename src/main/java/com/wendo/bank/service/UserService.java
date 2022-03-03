package com.wendo.bank.service;

import com.wendo.bank.dto.UserDto;

import java.util.Optional;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserDetails(String customerId);
    UserDto updateUserDetails(String customerId, UserDto userDto);
    UserDto deleteUser(String customerId);
}
