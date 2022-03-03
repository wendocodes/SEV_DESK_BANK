package com.wendo.bank.dto;

import com.wendo.bank.enums.Gender;
import com.wendo.bank.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private ZonedDateTime createdTimestamp;
    private ZonedDateTime updatedTimestamp;
    private String customerNumber;
    @NotEmpty
    @Size(min = 2, message = "user name should have at least 2 characters")
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private UserType type;
}
