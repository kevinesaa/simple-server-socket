package com.esaa.corp.commandOperations.views.operations;

import com.esaa.corp.commandOperations.models.CommandArgs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class ServerStartForeground {


    public static void executeCommand(final CommandArgs args) {
        File lockFile = new File("server.lock");
        FileChannel lockChannel = null;
        FileLock lock = null;
        try {

            if (lockFile.exists()) {
                lockFile.delete();
            }
            final FileOutputStream fileOutputStream = new FileOutputStream(lockFile);
            fileOutputStream.close();
            lockChannel = new RandomAccessFile(lockFile, "rw").getChannel();
            lock = lockChannel.tryLock();
            if (lock == null) {
                System.out.println("no puede inicializar un nuevo servicio");
            } else {
                System.out.println("server started");
                while (lock.isValid()) {
                    System.out.println("hola");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("on lock file deleted 1");

        try {
            if (lock != null) {
                lock.release();
                lock.close();
            }
            if (lockChannel != null) {
                lockChannel.close();
            }
            lockFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("on lock file deleted 2");
    }

}
