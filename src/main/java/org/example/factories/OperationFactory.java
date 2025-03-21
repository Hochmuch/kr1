package org.example.factories;

import org.example.domains.*;
import org.example.enums.OperationType;
import java.time.LocalDateTime;

public class OperationFactory {
    public static Operation createOperation(String id, double amount, OperationType type, 
                                         Category category, BankAccount bankAccount,
                                         String description) {
        if (category.getType() != type) {
            throw new IllegalArgumentException("Category type doesn't match operation type");
        }
        
        return new Operation(id, amount, type, category, bankAccount,
                           LocalDateTime.now(), description);
    }
}