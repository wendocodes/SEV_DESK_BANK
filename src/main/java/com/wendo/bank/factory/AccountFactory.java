package com.wendo.bank.factory;

import com.wendo.bank.enums.AccountType;
import com.wendo.bank.service.account.AccountWriteOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountFactory {

    private static final Map<AccountType, Object> accountCache = new ConcurrentHashMap<>();
    private List<Account> handlers = new ArrayList<>();

    @Autowired
    private AccountFactory(List<Account> handlers) {
        this.handlers = handlers;
    }

    public AccountFactory() {}


    @PostConstruct
    public void initAccountCache() {
        handlers.forEach(service -> {
            Account annotation = service.getClass().getAnnotation(Account.class);
            AccountType accountType = annotation.type();
            accountCache.put(accountType, service);
        });
    }

    public static AccountWriteOperations getAccountImplementation(AccountType accountType) {

        AccountWriteOperations service = (AccountWriteOperations) accountCache.get(accountType);
        if (null == service) {
            throw new RuntimeException("Unknown account type: " + accountType.name() + " or bean is not registered");
        }
        return FactoryUtil.getBean(service.getClass());
    }



}