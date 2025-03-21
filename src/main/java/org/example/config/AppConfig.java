package org.example.config;

import org.example.facades.*;
import org.example.services.FinancialManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig {

    @Bean
    public FinancialManager financialManager() {
        return new FinancialManager();
    }

    @Bean
    public AccountFacade accountFacade(FinancialManager financialManager) {
        return new AccountFacade(financialManager);
    }

    @Bean
    public CategoryFacade categoryFacade(FinancialManager financialManager) {
        return new CategoryFacade(financialManager);
    }

    @Bean
    public AnalyticsFacade analyticsFacade(FinancialManager financialManager) {
        return new AnalyticsFacade(financialManager);
    }

    @Bean
    public ExportFacade exportFacade(FinancialManager financialManager) {
        return new ExportFacade(financialManager);
    }
} 