package org.example.interfaces;

import org.example.domains.BankAccount;
import org.example.domains.Category;
import org.example.domains.Operation;

public interface Visitor {
    void visit(BankAccount account);
    void visit(Category category);
    void visit(Operation operation);
}