package com.wendo.bank.service;

import com.wendo.bank.converter.dtoconverter.UserConverter;
import com.wendo.bank.dto.UserDto;
import com.wendo.bank.enitity.User;
import com.wendo.bank.enums.AccountStatus;
import com.wendo.bank.enums.UserType;
import com.wendo.bank.exception.ResourceNotFoundException;
import com.wendo.bank.exception.ValidationException;
import com.wendo.bank.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserConverter userConverter;



    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user =userConverter.convertToEntity(userDto);
        log.info("User : {}", user);
        user.setCustomerNumber(UUID.randomUUID().toString());
        return userConverter.convertToDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserDetails(String customerId) {
        User user = userRepository.findByCustomerNumber(customerId);
        if(user == null) throw new ResourceNotFoundException("Customer number %s is invalid", customerId);
        return userConverter.convertToDto(user);
    }

    @Override
    public UserDto updateUserDetails(String customerId, UserDto userDto) {
        User user = userRepository.findByCustomerNumber(customerId);
        if(user == null) throw new ResourceNotFoundException("Customer number %s is invalid", customerId);
        if(userDto == null) throw new ValidationException("User data cannot be null");
        if(!Objects.equals(userDto.getFirstName(), user.getFirstName()))
            user.setFirstName(userDto.getFirstName());
        if(!Objects.equals(userDto.getLastName(), user.getLastName()))
            user.setLastName(userDto.getLastName());
        if(!Objects.equals(userDto.getDateOfBirth(), user.getDateOfBirth()))
            user.setDateOfBirth(userDto.getDateOfBirth());
        if(!Objects.equals(userDto.getGender(), user.getGender()))
            user.setGender(userDto.getGender());
        return userConverter.convertToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto deleteUser(String customerId) {
        User user = userRepository.findByCustomerNumber(customerId);
        if(user == null) throw new ResourceNotFoundException("Customer number %s is invalid", customerId);
        List<String> activeAccountStatus = Arrays.asList(AccountStatus.ACTIVE.name(), AccountStatus.PENDING_ACTIVAION.name());
        long activeAccounts =
                user.getAccounts().stream()
                        .filter(account -> activeAccountStatus.contains(account.getStatus().getStatus().name()))
                        .count();

        if(activeAccounts > 0)
            throw new ResourceNotFoundException("There are " + activeAccounts + " active accounts! User cannot be deleted");
        userRepository.deleteByCustomerNumber(customerId);
        return userConverter.convertToDto(user);
    }

}
