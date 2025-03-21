package org.example.importer.implementation;

import org.example.domains.BankAccount;
import org.example.domains.Category;
import org.example.domains.Operation;
import org.example.enums.OperationType;
import org.example.services.FinancialManager;
import org.example.importer.DataImporter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CsvDataImporter extends DataImporter {
    
    public CsvDataImporter(FinancialManager manager) {
        super(manager);
    }
    
    @Override
    protected ImportData parseData(String content) {
        List<BankAccount> accounts = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Operation> operations = new ArrayList<>();
        
        String[] lines = content.split("\n");
        String currentSection = null;
        
        for (String line : lines) {
            line = line.trim();
            
            if (line.isEmpty()) {
                continue;
            }
            
            if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1);
                continue;
            }
            
            if (currentSection == null) {
                continue;
            }
            
            String[] values = line.split(",");
            
            switch (currentSection) {
                case "accounts":
                    if (values.length >= 3) {
                        String id = values[0].trim();
                        String name = values[1].trim();
                        double balance = Double.parseDouble(values[2].trim());
                        accounts.add(new BankAccount(id, name, balance));
                    }
                    break;
                case "categories":
                    if (values.length >= 3) {
                        String id = values[0].trim();
                        String name = values[1].trim();
                        OperationType type = OperationType.valueOf(values[2].trim());
                        categories.add(new Category(id, name, type));
                    }
                    break;
                case "operations":
                    if (values.length >= 5) {
                        String id = values[0].trim();
                        String accountId = values[1].trim();
                        String categoryId = values[2].trim();
                        double amount = Double.parseDouble(values[3].trim());
                        String description = values[4].trim();
                        
                        BankAccount account = findAccountById(accounts, accountId);
                        Category category = findCategoryById(categories, categoryId);
                        
                        if (account != null && category != null) {
                            operations.add(new Operation(id, amount, category.getType(), 
                                                      category, account, LocalDateTime.now(), 
                                                      description));
                        }
                    }
                    break;
            }
        }
        
        return new ImportData(accounts, categories, operations);
    }
    
    private BankAccount findAccountById(List<BankAccount> accounts, String id) {
        return accounts.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    private Category findCategoryById(List<Category> categories, String id) {
        return categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
} 