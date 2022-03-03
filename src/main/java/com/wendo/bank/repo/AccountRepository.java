package com.wendo.bank.repo;

import com.wendo.bank.enitity.Account;
import com.wendo.bank.enitity.Amount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);

    Account findByAccountNumberAndCustomerCustomerNumber(String accountNumber, String customerNumber);
}