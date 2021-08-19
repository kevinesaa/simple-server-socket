package com.esaa.corp.commandOperations.views.operations;

import com.esaa.corp.commandOperations.models.Command;
import com.esaa.corp.commandOperations.models.CommandArgs;
import com.esaa.corp.fileSystem.views.MyFileSystemManager;
import com.esaa.corp.server.models.StartFile;
import com.esaa.corp.server.util.ServerStartUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerStart {

    private static final Logger LOGGER = Logger.getLogger(ServerStart.class.getSimpleName());


    public static void executeCommand(final CommandArgs args) {

        final StartFile configFile = ServerStartUtil.parseConfigFile(args);
        if (configFile.getAbsoluteFile() == null || configFile.getConfigFileModel() == null) {

            LOGGER.log(Level.SEVERE, "bad format config file");
        }
        else {

            final MyFileSystemManager myFileSystemManager = MyFileSystemManager.getInstance();
            final String jarFile = myFileSystemManager.getJarFile().getAbsolutePath();
            final String configPath = configFile.getAbsoluteFile().getAbsolutePath();
            final String command = "java -jar \"" + jarFile + "\" " + Command.START_FOREGROUND + " \"" + configPath + "\"";

            try {

                Runtime.getRuntime().exec(command);
            }
            catch (IOException e) {
                LOGGER.log(Level.SEVERE, "fail to run process: "+command,e);
            }

        }

    }
}
