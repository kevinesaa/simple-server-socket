package com.esaa.corp.commandOperations.models;

import java.util.ArrayList;
import java.util.List;

public class CommandArgs {

    private List<String> argsList;

    public CommandArgs() {
        this.argsList = new ArrayList<>();
    }

    public void addArgument(final String arg) {
        this.argsList.add(arg);
    }

    public String getArgument(final int index) {
        return this.argsList.get(index);
    }
}
