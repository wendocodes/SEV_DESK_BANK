package com.wendo.bank.repo;

import com.wendo.bank.enitity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByCustomerNumber(String id);
    void deleteByCustomerNumber(String id);
}