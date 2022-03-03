package com.wendo.bank.converter.dtoconverter;

import com.wendo.bank.dto.UserDto;
import com.wendo.bank.enitity.User;
import com.wendo.bank.enums.UserType;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<UserDto, User> {
    @Override
    public User convertToEntity(UserDto userDto) {
        return User.builder()
                .customerNumber(userDto.getCustomerNumber())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .dateOfBirth(userDto.getDateOfBirth())
                .gender(userDto.getGender())
                .type(userDto.getType() == null ? UserType.CUSTOMER : userDto.getType())
                .build();
    }

    @Override
    public UserDto convertToDto(User user) {
        return UserDto.builder()
                .createdTimestamp(user.getCreatedTimestamp())
                .updatedTimestamp(user.getUpdatedTimestamp())
                .customerNumber(user.getCustomerNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .type(user.getType() == null ? UserType.CUSTOMER : user.getType())
                .build();
    }
}
