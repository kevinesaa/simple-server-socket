package com.esaa.corp.commandOperations.views.operations;

import com.esaa.corp.commandOperations.models.CommandArgs;
import com.esaa.corp.commons.anims.IndeterminateLoading;
import com.esaa.corp.fileSystem.models.MyLockFile;
import com.esaa.corp.fileSystem.views.MyFileSystemManager;
import com.esaa.corp.server.models.ConfigFileModel;
import com.esaa.corp.server.models.StartFile;
import com.esaa.corp.server.util.StartFileParser;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerStartForeground {

    private static final Logger LOGGER = Logger.getLogger(ServerStartForeground.class.getSimpleName());


    public static void executeCommand(final CommandArgs args) {
        final StartFile configFile = StartFileParser.parseConfigFile(args);
        if (configFile.getAbsoluteFile() == null || configFile.getConfigFileModel() == null) {

            LOGGER.log(Level.SEVERE, "bad format config file");

        }
        else {

            final MyFileSystemManager myFileSystemManager = MyFileSystemManager.getInstance();
            final ConfigFileModel configFileModel = configFile.getConfigFileModel();
            final int serverPort = configFileModel.getServerPort();
            final File startFile = myFileSystemManager.getStartFile(serverPort);
            final MyLockFile lockFile = new MyLockFile(startFile);
            lockFile.deleteFileIfExist();
            if (lockFile.isFileExist()) {
                LOGGER.log(Level.SEVERE, "the port: " + serverPort +" is been using");
            }
            else {
                lockFile.tryLock();
                if (lockFile.isLocked()) {
                    final File stopFile = myFileSystemManager.getStopFile(serverPort);
                    final IndeterminateLoading loadingAnim = IndeterminateLoading.build();
                    while (!stopFile.exists()) {
                        loadingAnim.show();
                    }
                    loadingAnim.hide();
                    System.out.println("adios");
                }
                else {
                    LOGGER.log(Level.SEVERE, "the port: " + serverPort +" is been using");
                }
                lockFile.unLock();
            }
        }
    }
}


