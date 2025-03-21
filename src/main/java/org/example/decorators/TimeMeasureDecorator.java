package org.example.decorators;

import org.example.interfaces.Command;

public class TimeMeasureDecorator implements Command {
    private final Command command;
    private final String operationName;

    public TimeMeasureDecorator(Command command, String operationName) {
        this.command = command;
        this.operationName = operationName;
    }

    @Override
    public void execute() {
        long startTime = System.currentTimeMillis();
        command.execute();
        long endTime = System.currentTimeMillis();
        System.out.printf("Operation '%s' took %d ms%n", operationName, endTime - startTime);
    }
}