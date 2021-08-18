package com.esaa.corp.commandOperations.views.operations;

import com.esaa.corp.commandOperations.models.CommandArgs;

import java.io.File;

public class ServerStop {

    public static void executeCommand(final CommandArgs args){
        System.out.println("Stop server");
        try {
            File lockFile = new File("server.lock");
            if (lockFile.exists()) {
                if(lockFile.delete()) {
                    System.out.println("delete lockfile by command");
                }
                else {
                    System.out.println("can not delete the lockfile by command");
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
