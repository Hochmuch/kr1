// src/main/java/org/example/importer/DataImporter.java
package org.example.importer;

import org.example.domains.BankAccount;
import org.example.domains.Category;
import org.example.domains.Operation;
import org.example.services.FinancialManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public abstract class DataImporter {
    protected final FinancialManager manager;
    
    public DataImporter(FinancialManager manager) {
        this.manager = manager;
    }

    public final void importData(File file) throws IOException {
        String content = readFile(file);

        ImportData data = parseData(content);

        validateData(data);

        saveData(data);
    }

    protected String readFile(File file) throws IOException {
        return Files.readString(file.toPath());
    }
    
    protected abstract ImportData parseData(String content);
    
    protected void validateData(ImportData data) {
        if (data.accounts == null || data.categories == null || data.operations == null) {
            throw new IllegalArgumentException("Invalid data format: missing required sections");
        }
    }
    
    protected void saveData(ImportData data) {
        for (BankAccount account : data.accounts) {
            manager.addAccount(account);
        }
        
        for (Category category : data.categories) {
            manager.addCategory(category);
        }
        
        for (Operation operation : data.operations) {
            manager.addOperation(operation);
        }
    }
    
    protected static class ImportData {
        List<BankAccount> accounts;
        List<Category> categories;
        List<Operation> operations;
        
        public ImportData(List<BankAccount> accounts, List<Category> categories, List<Operation> operations) {
            this.accounts = accounts;
            this.categories = categories;
            this.operations = operations;
        }
    }
}