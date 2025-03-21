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

public class CsvExportVisitor implements Visitor {
    private final List<String> accountsCsv = new ArrayList<>();
    private final List<String> categoriesCsv = new ArrayList<>();
    private final List<String> operationsCsv = new ArrayList<>();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void visit(BankAccount account) {
        String csv = String.format("%s,%s,%.2f",
            account.getId(),
            account.getName(),
            account.getBalance()
        );
        accountsCsv.add(csv);
    }

    @Override
    public void visit(Category category) {
        String csv = String.format("%s,%s,%s",
            category.getId(),
            category.getName(),
            category.getType()
        );
        categoriesCsv.add(csv);
    }

    @Override
    public void visit(Operation operation) {
        String csv = String.format("%s,%s,%s,%.2f,%s,%s,%s",
            operation.getId(),
            operation.getAccount().getId(),
            operation.getCategory().getId(),
            operation.getAmount(),
            operation.getType(),
            operation.getDate().format(dateFormatter),
            operation.getDescription()
        );
        operationsCsv.add(csv);
    }

    public void exportToFile(String filePath) throws IOException {
        StringBuilder csv = new StringBuilder();

        csv.append("[accounts]\n");
        for (String line : accountsCsv) {
            csv.append(line).append("\n");
        }
        csv.append("\n");

        csv.append("[categories]\n");
        for (String line : categoriesCsv) {
            csv.append(line).append("\n");
        }
        csv.append("\n");

        csv.append("[operations]\n");
        for (String line : operationsCsv) {
            csv.append(line).append("\n");
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(csv.toString());
        }
    }
} 