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

public class ServerStop {

    private static final Logger LOGGER = Logger.getLogger(ServerStop.class.getSimpleName());

    public static void executeCommand(final CommandArgs args) {
        System.out.println("Stop server");
        final StartFile configFile = ServerStartUtil.parseConfigFile(args);
        if (configFile.getAbsoluteFile() == null || configFile.getConfigFileModel() == null) {

            LOGGER.log(Level.SEVERE, "bad format config file");
        } else {

            final ConfigFileModel configFileModel = configFile.getConfigFileModel();
            final File stopFile = MyFileSystem.getInstance().getStopFile(configFileModel.getServerPort());
            FileOutputStream fileOutputStream = null;
            FileChannel lockChannel = null;
            FileLock lock = null;
            if (stopFile.exists()) {

                stopFile.delete();
            }
            if (!stopFile.exists()) {
                try {
                    fileOutputStream = new FileOutputStream(stopFile);
                    fileOutputStream.close();
                    lockChannel = new RandomAccessFile(stopFile, "rw").getChannel();
                    lock = lockChannel.tryLock();
                    if (lock == null) {
                        System.out.println("another instance is already stopping the service at port: " + configFileModel.getServerPort());
                    }
                    else {
                        final File startFile = MyFileSystem.getInstance().getStartFile(configFileModel.getServerPort());
                        while (startFile.exists()){
                            System.out.println("stopping");
                        }
                        System.out.println("stop");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    if(lock != null){
                        lock.release();
                        lock.close();
                    }
                    if (lockChannel != null) {
                        lockChannel.close();
                    }
                    if (stopFile.exists()) {

                        stopFile.delete();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }
}
