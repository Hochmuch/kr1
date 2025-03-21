package org.example.facades;

import org.example.domains.BankAccount;
import org.example.services.FinancialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountFacade {
    private final FinancialManager manager;

    @Autowired
    public AccountFacade(FinancialManager manager) {
        this.manager = manager;
    }

    public BankAccount createAccount(String id, String name, double initialBalance) {
        BankAccount account = new BankAccount(id, name, initialBalance);
        manager.addAccount(account);
        return account;
    }

    public BankAccount getAccount(String id) {
        return manager.getAccount(id);
    }

    public void deleteAccount(String id) {
        manager.removeAccount(id);
    }
} 