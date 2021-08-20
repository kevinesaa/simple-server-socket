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

public class ServerStop {

    private static final Logger LOGGER = Logger.getLogger(ServerStop.class.getSimpleName());

    public static void executeCommand(final CommandArgs args) {

        LOGGER.log(Level.INFO, "Stopping server");
        final StartFile configFile = StartFileParser.parseConfigFile(args);
        if (configFile.getAbsoluteFile() == null || configFile.getConfigFileModel() == null) {

            LOGGER.log(Level.SEVERE, "bad format config file");
        }
        else {

            final ConfigFileModel configFileModel = configFile.getConfigFileModel();
            final int serverPort = configFileModel.getServerPort();
            final File stopFile = MyFileSystemManager.getInstance().getStopFile(serverPort);
            final MyLockFile lockFile = new MyLockFile(stopFile);
            lockFile.deleteFileIfExist();
            if(lockFile.isFileExist()) {
                LOGGER.log(Level.WARNING, "Is there another instance stopping the server at port: "+ serverPort);
            }
            else {
                lockFile.tryLock();
                if(lockFile.isLocked()) {
                    final File startFile = MyFileSystemManager.getInstance().getStartFile(serverPort);
                    System.out.println("waiting to stop");
                    final IndeterminateLoading loadingAnim = IndeterminateLoading.build();
                    do {
                        loadingAnim.show();
                    } while (startFile.exists());
                    loadingAnim.hide();
                    System.out.println("stopped");
                }
                lockFile.unLock();
            }
        }
    }
}
