package com.esaa.corp.commandOperations.models;

public enum Command {

    HELP,
    START,
    STOP,
    LIST_COM_PORTS;


    public static Command getByName(final String name){
        Command command = null;
        try {
            command = Command.valueOf(name);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return command;
    }
}
