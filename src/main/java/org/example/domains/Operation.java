package org.example.domains;

import java.time.LocalDateTime;

import org.example.enums.OperationType;
import org.example.interfaces.Visitor;

public class Operation {
    private String id;
    private double amount;
    private OperationType type;
    private Category category;
    private BankAccount bankAccount;
    private LocalDateTime date;
    private String description;

    public Operation(String id, double amount, OperationType type, Category category,
                     BankAccount bankAccount, LocalDateTime date, String description) {
        this.id = id;
        this.type = type;
        this.bankAccount = bankAccount;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;

        bankAccount.updateBalance(this);
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public OperationType getType() {
        return type;
    }

    public Category getCategory() {
        return category;
    }

    public BankAccount getAccount() {
        return bankAccount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}