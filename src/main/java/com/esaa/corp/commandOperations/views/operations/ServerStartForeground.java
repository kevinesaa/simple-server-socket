package com.esaa.corp.commandOperations.views.operations;

import com.esaa.corp.commandOperations.models.CommandArgs;
import com.esaa.corp.fileSystem.views.MyFileSystem;
import com.esaa.corp.server.models.ConfigFileModel;
import com.esaa.corp.server.models.StartFile;
import com.esaa.corp.server.util.ServerStartUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerStartForeground {

    private static final Logger LOGGER = Logger.getLogger(ServerStartForeground.class.getSimpleName());


    public static void executeCommand(final CommandArgs args) {
        final StartFile configFile = ServerStartUtil.parseConfigFile(args);
        if (configFile.getAbsoluteFile() == null || configFile.getConfigFileModel() == null) {

            LOGGER.log(Level.SEVERE, "bad format config file");
        } else {

            final MyFileSystem myFileSystem = MyFileSystem.getInstance();
            final ConfigFileModel configFileModel = configFile.getConfigFileModel();
            final File startFile = myFileSystem.getStartFile(configFileModel.getServerPort());
            FileOutputStream fileOutputStream = null;
            FileChannel lockChannel = null;
            FileLock lock = null;
            if (startFile.exists()) {
                startFile.delete();
            }

            if (!startFile.exists()) {
                try {
                    fileOutputStream = new FileOutputStream(startFile);
                    fileOutputStream.close();
                    lockChannel = new RandomAccessFile(startFile, "rw").getChannel();
                    lock = lockChannel.tryLock();
                    if (lock == null) {
                        System.out.println("no puede inicializar un nuevo servicio en el puerto " + configFileModel.getServerPort());
                    } else {

                        final File stopFile = myFileSystem.getStopFile(configFileModel.getServerPort());
                        while (!stopFile.exists()) {
                            System.out.println("hola");
                        }
                        System.out.println("adios");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (lock != null) {
                        lock.release();
                        lock.close();
                    }
                    if (lockChannel != null) {
                        lockChannel.close();
                    }
                    if (startFile.exists()) {
                        startFile.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
