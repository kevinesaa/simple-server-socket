package com.esaa.corp.main.views;

import com.esaa.corp.commandOperations.models.Command;
import com.esaa.corp.commandOperations.models.CommandArgs;
import com.esaa.corp.commandOperations.models.ErrorCodes;
import com.esaa.corp.commandOperations.views.CommandExecute;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {



    /*
    host : string {ip, dns}
    net port: int
    printer port: {com0, com1}
    operation:{invoice,credit note}
    {

     }
    */

    /*start serverPort printerPort*/
    /*stop serverPort*/

    private static final Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());

    public static void main(final String[] args) {

        LOGGER.log(Level.INFO, "new instance");
        if (args == null || args.length <= 0) {

            LOGGER.log(Level.SEVERE, "no args found");
            LOGGER.log(Level.SEVERE, "try type " + Command.HELP.name() + " to get more info");
            System.exit(ErrorCodes.EMPTY_ARGS);
            return;
        } else {
            LOGGER.info("printing arguments");
            for (final String arg : args) {
                LOGGER.info(arg);
            }
        }

        final Command command = Command.getByName(args[0].toUpperCase());
        if (command == null) {
            LOGGER.log(Level.SEVERE, "command not found");
            LOGGER.log(Level.SEVERE, "try type " + Command.HELP.name() + " to get more info");
            System.exit(ErrorCodes.COMMAND_NOT_FOUND);
            return;
        }

        if (args.length > 1) {
            final CommandArgs commandArgs = new CommandArgs();
            for (int i = 1; i < args.length; i++) {
                commandArgs.addArgument(args[i]);
            }

            CommandExecute.getInstance().Execute(command, commandArgs);
        } else {
            CommandExecute.getInstance().Execute(command);
        }
    }

}
