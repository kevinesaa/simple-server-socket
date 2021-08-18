package com.esaa.corp.commandOperations.views;

import com.esaa.corp.commandOperations.models.Command;
import com.esaa.corp.commandOperations.models.CommandArgs;
import com.esaa.corp.commandOperations.models.RouteSingleCommand;
import com.esaa.corp.commandOperations.models.RouteWithArgsCommand;
import com.esaa.corp.commandOperations.views.operations.Help;
import com.esaa.corp.commandOperations.views.operations.ServerStart;
import com.esaa.corp.commandOperations.views.operations.ServerStartForeground;
import com.esaa.corp.commandOperations.views.operations.ServerStop;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandExecute {

    private static final Logger LOGGER = Logger.getLogger(CommandExecute.class.getSimpleName());

    private static CommandExecute INSTANCE;

    private Map<Command, RouteSingleCommand> singleCommandRoutes;
    private Map<Command, RouteWithArgsCommand> commandRoutesWithArguments;

    private CommandExecute(){}

    public static CommandExecute getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CommandExecute();
        }

        return INSTANCE;
    }

    public void Execute(final Command command) {
        if(singleCommandRoutes == null) {
            initSingleRoute();
        }
        final RouteSingleCommand singleCommandRoute = singleCommandRoutes.get(command);
        if(singleCommandRoute == null) {
            LOGGER.log(Level.WARNING,"single command not mapping yet");
            return;
        }
        singleCommandRoute.execute();
    }

    public void Execute(final Command command, final CommandArgs commandArgs) {
        if(commandRoutesWithArguments == null) {
            initRouteWithArgs();
        }
        final RouteWithArgsCommand routeWithArgsCommand = commandRoutesWithArguments.get(command);
        if(routeWithArgsCommand == null) {
            LOGGER.log(Level.WARNING,"Command with args not mapping yet");
            return;
        }
        routeWithArgsCommand.execute(commandArgs);
    }

    private void initSingleRoute() {
        singleCommandRoutes = new HashMap<>();

        singleCommandRoutes.put(Command.HELP, Help::executeCommand);
    }

    private void initRouteWithArgs(){
        commandRoutesWithArguments = new HashMap<>();

        commandRoutesWithArguments.put(Command.START, ServerStart::executeCommand);
        commandRoutesWithArguments.put(Command.START_FOREGROUND, ServerStartForeground::executeCommand);
        commandRoutesWithArguments.put(Command.STOP, ServerStop::executeCommand);

    }
}
