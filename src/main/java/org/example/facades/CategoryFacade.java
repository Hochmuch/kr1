package org.example.facades;

import org.example.domains.Category;
import org.example.enums.OperationType;
import org.example.services.FinancialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryFacade {
    private final FinancialManager manager;

    @Autowired
    public CategoryFacade(FinancialManager manager) {
        this.manager = manager;
    }

    public Category createCategory(String id, String name, OperationType type) {
        Category category = new Category(id, name, type);
        manager.addCategory(category);
        return category;
    }
} 