package org.example.services;

import org.example.domains.*;
import org.example.enums.OperationType;
import org.example.factories.OperationFactory;
import org.example.interfaces.Command;

public class CreateOperationCommand implements Command {
    private final String id;
    private final double amount;
    private final OperationType type;
    private final Category category;
    private final BankAccount account;
    private final String description;
    private final FinancialManager manager;

    public CreateOperationCommand(String id, double amount, OperationType type,
                                  Category category, BankAccount account,
                                  String description, FinancialManager manager) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.account = account;
        this.description = description;
        this.manager = manager;
    }

    @Override
    public void execute() {
        Operation operation = OperationFactory.createOperation(
                id, amount, type, category, account, description
        );
        manager.addOperation(operation);
    }
}