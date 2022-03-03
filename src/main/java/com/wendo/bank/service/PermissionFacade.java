package com.wendo.bank.service;

import com.wendo.bank.enitity.Account;
import com.wendo.bank.enitity.User;
import com.wendo.bank.enums.AccountStatus;
import com.wendo.bank.enums.UserType;
import com.wendo.bank.exception.AccessDeniedException;
import com.wendo.bank.exception.ValidationException;
import com.wendo.bank.repo.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class PermissionFacade {

    private final UserRepository userRepository;

    public PermissionFacade(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void authenticateUserAndAccount(String customer, String pin, String accountNumber) {
        User user = userRepository.findByCustomerNumber(customer);
        if(user == null) throw new ValidationException("Customer number %s is invalid", customer);
        Account account = user.getAccounts().stream().filter(acc -> AccountStatus.ACTIVE.name().equals(acc.getStatus().getStatus().name())).findFirst().orElse(null);
        if(account == null) throw new ValidationException("Account number %s is invalid or Account is not in active state", customer);
        if(!accountNumber.equals(account.getAccountNumber()) && pin.equals(account.getPin())) throw new AccessDeniedException("You don't have access to perform this operation");

    }

    public void authenticateManager(String managerId) {
        User user = userRepository.findByCustomerNumber(managerId);
        if(user == null) throw new ValidationException("Manager number %s is invalid", managerId);
        if(!UserType.MANAGER.name().equals(user.getType().name())) throw new AccessDeniedException("You don't have access to perform this operation");
    }
}
