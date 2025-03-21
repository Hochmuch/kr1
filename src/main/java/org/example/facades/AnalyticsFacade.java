package org.example.facades;

import org.example.domains.*;
import org.example.enums.OperationType;
import org.example.services.FinancialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AnalyticsFacade {
    private final FinancialManager manager;

    @Autowired
    public AnalyticsFacade(FinancialManager manager) {
        this.manager = manager;
    }

    public double calculateBalanceDifference(LocalDateTime from, LocalDateTime to) {
        return manager.getOperations().stream()
            .filter(op -> op.getDate().isAfter(from) && op.getDate().isBefore(to))
            .mapToDouble(op -> op.getType() == OperationType.INCOME ?
                       op.getAmount() : -op.getAmount())
            .sum();
    }

    public Map<Category, Double> groupByCategory(OperationType type,
                                               LocalDateTime from,
                                               LocalDateTime to) {
        return manager.getOperations().stream()
            .filter(op -> op.getType() == type)
            .filter(op -> op.getDate().isAfter(from) && op.getDate().isBefore(to))
            .collect(Collectors.groupingBy(
                Operation::getCategory,
                Collectors.summingDouble(Operation::getAmount)
            ));
    }

    public double getTotalByCategory(String categoryId,
                                   LocalDateTime from,
                                   LocalDateTime to) {
        return manager.getOperations().stream()
            .filter(op -> op.getCategory().getId().equals(categoryId))
            .filter(op -> op.getDate().isAfter(from) && op.getDate().isBefore(to))
            .mapToDouble(Operation::getAmount)
            .sum();
    }
} 