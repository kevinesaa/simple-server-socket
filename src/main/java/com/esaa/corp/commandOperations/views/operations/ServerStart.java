package com.esaa.corp.commandOperations.views.operations;

import com.esaa.corp.commandOperations.models.Command;
import com.esaa.corp.commandOperations.models.CommandArgs;

import java.io.File;
import java.io.IOException;


public class ServerStart {



    public static void executeCommand(final CommandArgs args) {

        final File file = new File(System.getProperty("java.class.path"));
        final String jarFile = file.getAbsoluteFile().toString();
        final String command = "java -jar \""+ jarFile +"\" "+ Command.START_FOREGROUND +" args";

        try {

            Runtime.getRuntime().exec(command);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
