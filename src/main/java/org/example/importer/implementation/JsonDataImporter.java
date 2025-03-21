package org.example.importer.implementation;

import org.example.domains.BankAccount;
import org.example.domains.Category;
import org.example.domains.Operation;
import org.example.enums.OperationType;
import org.example.services.FinancialManager;
import org.example.importer.DataImporter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

public class JsonDataImporter extends DataImporter {
    
    public JsonDataImporter(FinancialManager manager) {
        super(manager);
    }
    
    @Override
    protected ImportData parseData(String content) {
        content = content.replaceAll("\\s+", "");
        
        List<BankAccount> accounts = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Operation> operations = new ArrayList<>();
        
        int accountsStart = content.indexOf("\"accounts\":[");
        int categoriesStart = content.indexOf("\"categories\":[");
        int operationsStart = content.indexOf("\"operations\":[");
        
        if (accountsStart >= 0) {
            String accountsJson = extractArrayContent(content, accountsStart + "\"accounts\":[".length());
            accounts = parseAccounts(accountsJson);
        }
        
        if (categoriesStart >= 0) {
            String categoriesJson = extractArrayContent(content, categoriesStart + "\"categories\":[".length());
            categories = parseCategories(categoriesJson);
        }
        
        if (operationsStart >= 0) {
            String operationsJson = extractArrayContent(content, operationsStart + "\"operations\":[".length());
            operations = parseOperations(operationsJson, accounts, categories);
        }
        
        return new ImportData(accounts, categories, operations);
    }
    
    private String extractArrayContent(String json, int startIndex) {
        int bracketCount = 1;
        int endIndex = startIndex;
        
        while (bracketCount > 0 && endIndex < json.length()) {
            char c = json.charAt(endIndex);
            if (c == '[') bracketCount++;
            else if (c == ']') bracketCount--;
            endIndex++;
        }
        
        return json.substring(startIndex, endIndex - 1);
    }
    
    private List<BankAccount> parseAccounts(String json) {
        List<BankAccount> accounts = new ArrayList<>();
        List<String> objects = splitJsonArray(json);
        
        for (String obj : objects) {
            Map<String, String> fields = parseJsonObject(obj);
            String id = fields.get("id");
            String name = fields.get("name");
            String balanceStr = fields.get("balance");
            
            if (id != null && name != null) {
                double balance = 0.0;
                if (balanceStr != null) {
                    try {
                        balance = Double.parseDouble(balanceStr);
                    } catch (NumberFormatException e) {
                    }
                }
                accounts.add(new BankAccount(id, name, balance));
            }
        }
        
        return accounts;
    }
    
    private List<Category> parseCategories(String json) {
        List<Category> categories = new ArrayList<>();
        List<String> objects = splitJsonArray(json);
        
        for (String obj : objects) {
            Map<String, String> fields = parseJsonObject(obj);
            String id = fields.get("id");
            String name = fields.get("name");
            String typeStr = fields.get("type");
            
            if (id != null && name != null && typeStr != null) {
                OperationType type = OperationType.valueOf(typeStr);
                categories.add(new Category(id, name, type));
            }
        }
        
        return categories;
    }
    
    private List<Operation> parseOperations(String json, List<BankAccount> accounts, List<Category> categories) {
        List<Operation> operations = new ArrayList<>();
        List<String> objects = splitJsonArray(json);
        
        for (String obj : objects) {
            Map<String, String> fields = parseJsonObject(obj);
            String id = fields.get("id");
            String accountId = fields.get("accountId");
            String categoryId = fields.get("categoryId");
            String amountStr = fields.get("amount");
            String description = fields.get("description");
            
            if (id != null && accountId != null && categoryId != null && amountStr != null && description != null) {
                double amount = Double.parseDouble(amountStr);
                BankAccount account = findAccountById(accounts, accountId);
                Category category = findCategoryById(categories, categoryId);
                
                if (account != null && category != null) {
                    operations.add(new Operation(id, amount, category.getType(), category, 
                                               account, LocalDateTime.now(), description));
                }
            }
        }
        
        return operations;
    }
    
    private List<String> splitJsonArray(String json) {
        List<String> result = new ArrayList<>();
        int start = 0;
        int bracketCount = 0;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '{') {
                if (bracketCount == 0) {
                    start = i;
                }
                bracketCount++;
            } else if (c == '}') {
                bracketCount--;
                if (bracketCount == 0) {
                    result.add(json.substring(start, i + 1));
                }
            }
        }
        
        return result;
    }
    
    private Map<String, String> parseJsonObject(String json) {
        Map<String, String> result = new HashMap<>();
        
        json = json.substring(1, json.length() - 1);
        
        String[] pairs = json.split(",");
        
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].replace("\"", "");
                String value = keyValue[1].replace("\"", "");
                result.put(key, value);
            }
        }
        
        return result;
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