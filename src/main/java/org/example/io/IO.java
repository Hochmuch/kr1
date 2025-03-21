package org.example.io;

import org.example.config.AppConfig;
import org.example.domains.*;
import org.example.enums.*;
import org.example.facades.*;
import org.example.importer.implementation.CsvDataImporter;
import org.example.services.*;
import org.example.interfaces.*;
import org.example.decorators.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class IO {
    private final Scanner scanner;
    private final ConfigurableApplicationContext context;
    private final AccountFacade accountFacade;
    private final CategoryFacade categoryFacade;
    private final ExportFacade exportFacade;
    private final FinancialManager financialManager;

    public IO() {
        scanner = new Scanner(System.in);
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        accountFacade = context.getBean(AccountFacade.class);
        categoryFacade = context.getBean(CategoryFacade.class);
        exportFacade = context.getBean(ExportFacade.class);
        financialManager = context.getBean(FinancialManager.class);
    }

    public void run() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = readIntInput("Choose action: ");

            switch (choice) {
                case 1:
                    createSampleData();
                    break;
                case 2:
                    exportToCsv();
                    break;
                case 3:
                    importFromCsv();
                    break;
                case 4:
                    showAllData();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exit.");
                    break;
                default:
                    System.out.println("Unknown action. Try again.");
            }
        }

        context.close();
    }

    private void displayMenu() {
        System.out.println("\nAvailable actions:");
        System.out.println("1. Create data");
        System.out.println("2. Export to CSV");
        System.out.println("3. Import from CSV");
        System.out.println("4. Show data");
        System.out.println("0. Exit");
    }

    private void createSampleData() {
        BankAccount mainAccount = accountFacade.createAccount("acc0001", "Main account", 0.0);

        Category salaryCategory = categoryFacade.createCategory("cat0001", "Salary", OperationType.INCOME);
        Category rentCategory = categoryFacade.createCategory("cat0002", "Rent", OperationType.EXPENSE);
        Category miscCategory = categoryFacade.createCategory("cat0003", "Misc", OperationType.EXPENSE);

        Command createSalaryOp = new CreateOperationCommand(
                "op0001", 80000, OperationType.INCOME, salaryCategory, mainAccount,
                "February salary", financialManager
        );
        Command timedSalaryOp = new TimeMeasureDecorator(createSalaryOp, "SalaryOp creation");
        timedSalaryOp.execute();

        Command createRentOp = new CreateOperationCommand(
                "op0002", 70000, OperationType.EXPENSE, rentCategory, mainAccount,
                "Rent", financialManager
        );
        createRentOp.execute();

        Command createFoodOp = new CreateOperationCommand(
                "op0003", 500, OperationType.EXPENSE, miscCategory, mainAccount,
                "Book purchase", financialManager
        );
        createFoodOp.execute();

        System.out.println("Data created!");
    }

    private void exportToCsv() {
        String filePath = "financial_data.csv";

        try {
            exportFacade.exportToCsv(filePath);
            System.out.println("Data exported!");
        } catch (IOException e) {
            System.out.println("This went wrong: " + e.getMessage());
        }
    }

    private void importFromCsv() {
        String filePath = "financial_data.csv";

        financialManager.getAccounts().clear();
        financialManager.getCategories().clear();
        financialManager.getOperations().clear();

        try {
            CsvDataImporter importer = new CsvDataImporter(financialManager);
            importer.importData(new File(filePath));
            System.out.println("Data imported!");
        } catch (IOException e) {
            System.out.println("This went wrong: " + e.getMessage());
        }
    }

    private void showAllData() {
        System.out.println("\nAccounts:");
        if (financialManager.getAccounts().isEmpty()) {
            System.out.println("No accounts.");
        } else {
            for (BankAccount account : financialManager.getAccounts()) {
                System.out.printf("ID: %s, Name: %s, Balance: %.2f\n", account.getId(), account.getName(), account.getBalance());
            }
        }

        System.out.println("\nCategories.");
        if (financialManager.getCategories().isEmpty()) {
            System.out.println("No categories.");
        } else {
            for (Category category : financialManager.getCategories()) {
                System.out.printf("ID: %s, Name: %s, Type: %s\n", category.getId(), category.getName(), category.getType());
            }
        }

        System.out.println("\nOperations:");
        if (financialManager.getOperations().isEmpty()) {
            System.out.println("No operations.");
        } else {
            for (Operation op : financialManager.getOperations()) {
                System.out.printf("ID: %s | Type: %s | Amount: %.2f | Category: %s | Account: %s | Description: %s\n",
                        op.getId(), op.getType(), op.getAmount(),
                        op.getCategory().getName(), op.getAccount().getName(),
                        op.getDescription());
            }
        }
    }

    private int readIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer.");
            }
        }
    }

    public static void main(String[] args) {
        IO app = new IO();
        app.run();
    }
}