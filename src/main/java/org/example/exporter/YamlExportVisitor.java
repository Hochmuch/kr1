package org.example.exporter;

import org.example.domains.BankAccount;
import org.example.domains.Category;
import org.example.domains.Operation;
import org.example.interfaces.Visitor;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class YamlExportVisitor implements Visitor {
    private final List<String> accountsYaml = new ArrayList<>();
    private final List<String> categoriesYaml = new ArrayList<>();
    private final List<String> operationsYaml = new ArrayList<>();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void visit(BankAccount account) {
        StringBuilder yaml = new StringBuilder();
        yaml.append("  - id: ").append(account.getId()).append("\n");
        yaml.append("    name: ").append(account.getName()).append("\n");
        yaml.append("    balance: ").append(account.getBalance()).append("\n");
        accountsYaml.add(yaml.toString());
    }

    @Override
    public void visit(Category category) {
        StringBuilder yaml = new StringBuilder();
        yaml.append("  - id: ").append(category.getId()).append("\n");
        yaml.append("    name: ").append(category.getName()).append("\n");
        yaml.append("    type: ").append(category.getType()).append("\n");
        categoriesYaml.add(yaml.toString());
    }

    @Override
    public void visit(Operation operation) {
        StringBuilder yaml = new StringBuilder();
        yaml.append("  - id: ").append(operation.getId()).append("\n");
        yaml.append("    accountId: ").append(operation.getAccount().getId()).append("\n");
        yaml.append("    categoryId: ").append(operation.getCategory().getId()).append("\n");
        yaml.append("    amount: ").append(operation.getAmount()).append("\n");
        yaml.append("    type: ").append(operation.getType()).append("\n");
        yaml.append("    date: ").append(operation.getDate().format(dateFormatter)).append("\n");
        yaml.append("    description: ").append(operation.getDescription()).append("\n");
        operationsYaml.add(yaml.toString());
    }

    public void exportToFile(String filePath) throws IOException {
        StringBuilder yaml = new StringBuilder();

        yaml.append("accounts:\n");
        for (String entry : accountsYaml) {
            yaml.append(entry);
        }
        yaml.append("\n");
        
        yaml.append("categories:\n");
        for (String entry : categoriesYaml) {
            yaml.append(entry);
        }
        yaml.append("\n");
        
        yaml.append("operations:\n");
        for (String entry : operationsYaml) {
            yaml.append(entry);
        }
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(yaml.toString());
        }
    }
} 