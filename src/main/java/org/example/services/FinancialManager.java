package org.example.services;

import org.example.domains.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FinancialManager {
    private final Map<String, BankAccount> accounts = new HashMap<>();
    private final Map<String, Category> categories = new HashMap<>();
    private final List<Operation> operations = new ArrayList<>();

    public FinancialManager() {}

    public void addAccount(BankAccount bankAccount) {
        accounts.put(bankAccount.getId(), bankAccount);
    }

    public void addCategory(Category category) {
        categories.put(category.getId(), category);
    }

    public void addOperation(Operation operation) {
        operations.add(operation);
    }

    public BankAccount getAccount(String id) {
        return accounts.get(id);
    }

    public Category getCategory(String id) {
        return categories.get(id);
    }

    public List<Operation> getOperations() {
        return new ArrayList<>(operations);
    }

    public List<BankAccount> getAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public List<Category> getCategories() {
        return new ArrayList<>(categories.values());
    }

    public List<Operation> getOperationsByAccount(String accountId) {
        return operations.stream()
                .filter(op -> op.getAccount().getId().equals(accountId))
                .toList();
    }

    public List<Operation> getOperationsByCategory(String categoryId) {
        return operations.stream()
                .filter(op -> op.getCategory().getId().equals(categoryId))
                .toList();
    }

    public void removeCategory(String id) {
        Category category = categories.get(id);
        if (category == null) {
            throw new IllegalArgumentException("Category not found");
        }

        boolean categoryInUse = operations.stream()
                .anyMatch(op -> op.getCategory().getId().equals(id));
        
        if (categoryInUse) {
            throw new IllegalStateException("Cannot remove category that is used in operations");
        }

        categories.remove(id);
    }

    public void removeAccount(String id) {
        BankAccount account = accounts.get(id);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        boolean accountInUse = operations.stream()
                .anyMatch(op -> op.getAccount().getId().equals(id));
        
        if (accountInUse) {
            throw new IllegalStateException("Cannot remove account that is used in operations");
        }

        accounts.remove(id);
    }
}