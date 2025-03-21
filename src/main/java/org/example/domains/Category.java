package org.example.domains;

import org.example.enums.OperationType;
import org.example.interfaces.Visitor;

public class Category {
    private String id;
    private String name;
    private OperationType type;

    public Category(String id, String name, OperationType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OperationType getType() {
        return type;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}