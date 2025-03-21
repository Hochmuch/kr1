package org.example.importer.implementation;

import org.example.domains.BankAccount;
import org.example.domains.Category;
import org.example.domains.Operation;
import org.example.enums.OperationType;
import org.example.services.FinancialManager;
import org.example.importer.DataImporter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlDataImporter extends DataImporter {
    
    public YamlDataImporter(FinancialManager manager) {
        super(manager);
    }
    
    @Override
    protected ImportData parseData(String content) {
        List<BankAccount> accounts = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Operation> operations = new ArrayList<>();
        
        String[] lines = content.split("\n");
        String currentSection = null;
        Map<String, String> currentItem = null;
        List<Map<String, String>> accountItems = new ArrayList<>();
        List<Map<String, String>> categoryItems = new ArrayList<>();
        List<Map<String, String>> operationItems = new ArrayList<>();
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            
            if (line.isEmpty()) {
                continue;
            }
            
            if (!line.startsWith(" ") && line.endsWith(":")) {
                currentSection = line.substring(0, line.length() - 1);
                continue;
            }
            
            if (line.startsWith("-")) {
                if (currentItem != null) {
                    if ("accounts".equals(currentSection)) {
                        accountItems.add(currentItem);
                    } else if ("categories".equals(currentSection)) {
                        categoryItems.add(currentItem);
                    } else if ("operations".equals(currentSection)) {
                        operationItems.add(currentItem);
                    }
                }
                
                currentItem = new HashMap<>();
                continue;
            }
            
            if (line.contains(":")) {
                String[] parts = line.split(":", 2);
                String key = parts[0].trim();
                String value = parts[1].trim();
                
                if (currentItem != null) {
                    currentItem.put(key, value);
                }
            }
        }
        
        if (currentItem != null) {
            if ("accounts".equals(currentSection)) {
                accountItems.add(currentItem);
            } else if ("categories".equals(currentSection)) {
                categoryItems.add(currentItem);
            } else if ("operations".equals(currentSection)) {
                operationItems.add(currentItem);
            }
        }
        
        accounts = parseAccounts(accountItems);
        categories = parseCategories(categoryItems);
        operations = parseOperations(operationItems, accounts, categories);
        
        return new ImportData(accounts, categories, operations);
    }
    
    private List<BankAccount> parseAccounts(List<Map<String, String>> items) {
        List<BankAccount> accounts = new ArrayList<>();
        
        for (Map<String, String> item : items) {
            String id = item.get("id");
            String name = item.get("name");
            String balanceStr = item.get("balance");
            
            if (id != null && name != null) {
                double balance = 0.0;
                if (balanceStr != null) {
                    try {
                        balance = Double.parseDouble(balanceStr);
                    } catch (NumberFormatException e) {
                        // оставляем нулевой баланс
                    }
                }
                accounts.add(new BankAccount(id, name, balance));
            }
        }
        
        return accounts;
    }
    
    private List<Category> parseCategories(List<Map<String, String>> items) {
        List<Category> categories = new ArrayList<>();
        
        for (Map<String, String> item : items) {
            String id = item.get("id");
            String name = item.get("name");
            String typeStr = item.get("type");
            
            if (id != null && name != null && typeStr != null) {
                OperationType type = OperationType.valueOf(typeStr);
                categories.add(new Category(id, name, type));
            }
        }
        
        return categories;
    }
    
    private List<Operation> parseOperations(List<Map<String, String>> items, 
                                          List<BankAccount> accounts, 
                                          List<Category> categories) {
        List<Operation> operations = new ArrayList<>();
        
        for (Map<String, String> item : items) {
            String id = item.get("id");
            String accountId = item.get("accountId");
            String categoryId = item.get("categoryId");
            String amountStr = item.get("amount");
            String description = item.get("description");
            
            if (id != null && accountId != null && categoryId != null && 
                amountStr != null && description != null) {
                
                double amount = Double.parseDouble(amountStr);
                BankAccount account = findAccountById(accounts, accountId);
                Category category = findCategoryById(categories, categoryId);
                
                if (account != null && category != null) {
                    operations.add(new Operation(id, amount, category.getType(), 
                                               category, account, LocalDateTime.now(), 
                                               description));
                }
            }
        }
        
        return operations;
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