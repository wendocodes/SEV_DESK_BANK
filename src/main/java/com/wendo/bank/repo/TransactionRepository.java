package com.wendo.bank.repo;

import com.wendo.bank.enitity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findByRef(String ref);
    Set<Transaction> findByAccountAccountNumber(String accountNumber);

}