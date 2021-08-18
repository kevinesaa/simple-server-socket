package com.esaa.corp.commandOperations.views.operations;

import com.esaa.corp.commandOperations.models.Command;
import com.esaa.corp.commandOperations.models.CommandArgs;
import com.esaa.corp.commons.utils.JsonParser;
import com.esaa.corp.main.views.MyFileSystem;
import com.esaa.corp.server.models.ConfigFileModel;
import com.esaa.corp.server.models.StartFile;
import com.esaa.corp.server.util.ServerStartUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

            try {
                final MyFileSystem myFileSystem = MyFileSystem.getInstance();
                final String jarFile = myFileSystem.getJarFile().getAbsolutePath();
                final String configPath = configFile.getAbsoluteFile().getAbsolutePath();
                final String command = "java -jar \"" + jarFile + "\" " + Command.START_FOREGROUND + " \"" + configPath + "\"";

                Runtime.getRuntime().exec(command);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
