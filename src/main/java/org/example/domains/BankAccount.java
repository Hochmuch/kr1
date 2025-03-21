package org.example.domains;

import org.example.enums.OperationType;
import org.example.interfaces.Visitor;

public class BankAccount {
    private String id;
    private String name;
    private double balance;

    public BankAccount(String id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public void updateBalance(Operation operation) {
        if (operation.getType() == OperationType.INCOME) {
            this.balance += operation.getAmount();
        } else {
            this.balance -= operation.getAmount();
        }
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getBalance() { return balance; }
}