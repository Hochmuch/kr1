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

public class JsonExportVisitor implements Visitor {
    private final List<String> accountsJson = new ArrayList<>();
    private final List<String> categoriesJson = new ArrayList<>();
    private final List<String> operationsJson = new ArrayList<>();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void visit(BankAccount account) {
        String json = String.format(
            "{\"id\":\"%s\",\"name\":\"%s\",\"balance\":%.2f}",
            account.getId(),
            account.getName(),
            account.getBalance()
        );
        accountsJson.add(json);
    }

    @Override
    public void visit(Category category) {
        String json = String.format(
            "{\"id\":\"%s\",\"name\":\"%s\",\"type\":\"%s\"}",
            category.getId(),
            category.getName(),
            category.getType()
        );
        categoriesJson.add(json);
    }

    @Override
    public void visit(Operation operation) {
        String json = String.format(
            "{\"id\":\"%s\",\"accountId\":\"%s\",\"categoryId\":\"%s\",\"amount\":%.2f,\"type\":\"%s\",\"date\":\"%s\",\"description\":\"%s\"}",
            operation.getId(),
            operation.getAccount().getId(),
            operation.getCategory().getId(),
            operation.getAmount(),
            operation.getType(),
            operation.getDate().format(dateFormatter),
            operation.getDescription()
        );
        operationsJson.add(json);
    }

    public void exportToFile(String filePath) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        json.append("  \"accounts\": [\n");
        json.append("    ").append(String.join(",\n    ", accountsJson)).append("\n");
        json.append("  ],\n");

        json.append("  \"categories\": [\n");
        json.append("    ").append(String.join(",\n    ", categoriesJson)).append("\n");
        json.append("  ],\n");

        json.append("  \"operations\": [\n");
        json.append("    ").append(String.join(",\n    ", operationsJson)).append("\n");
        json.append("  ]\n");
        
        json.append("}");
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json.toString());
        }
    }
} 