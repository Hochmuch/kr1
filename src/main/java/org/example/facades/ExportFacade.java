package org.example.facades;

import org.example.domains.BankAccount;
import org.example.domains.Category;
import org.example.domains.Operation;
import org.example.exporter.CsvExportVisitor;
import org.example.exporter.JsonExportVisitor;
import org.example.exporter.YamlExportVisitor;
import org.example.services.FinancialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ExportFacade {
    private final FinancialManager manager;
    
    @Autowired
    public ExportFacade(FinancialManager manager) {
        this.manager = manager;
    }
    
    public void exportToJson(String filePath) throws IOException {
        JsonExportVisitor visitor = new JsonExportVisitor();
        visitAllEntities(visitor);
        visitor.exportToFile(filePath);
    }
    
    public void exportToCsv(String filePath) throws IOException {
        CsvExportVisitor visitor = new CsvExportVisitor();
        visitAllEntities(visitor);
        visitor.exportToFile(filePath);
    }
    
    public void exportToYaml(String filePath) throws IOException {
        YamlExportVisitor visitor = new YamlExportVisitor();
        visitAllEntities(visitor);
        visitor.exportToFile(filePath);
    }

    
    private void visitAllEntities(org.example.interfaces.Visitor visitor) {
        for (BankAccount account : manager.getAccounts()) {
            account.accept(visitor);
        }

        for (Category category : manager.getCategories()) {
            category.accept(visitor);
        }

        for (Operation operation : manager.getOperations()) {
            operation.accept(visitor);
        }
    }
} 