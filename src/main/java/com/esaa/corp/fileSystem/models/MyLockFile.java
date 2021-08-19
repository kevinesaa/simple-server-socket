package com.esaa.corp.fileSystem.models;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLockFile {

    private static final Logger LOGGER = Logger.getLogger(MyLockFile.class.getSimpleName());

    private final File file;
    private FileChannel lockChannel = null;
    private FileLock lock = null;

    public MyLockFile(final File file) throws IllegalArgumentException{
        if(file == null) {
            throw new IllegalArgumentException("File can't be null");
        }
        this.file = file;
    }

    public boolean isLocked() {
        return lock != null;
    }

    public boolean isFileExist(){
        return  file.exists();
    }

    public void deleteFileIfExist() {

        if(isFileExist()){
            try {
                file.delete();
            }
            catch (RuntimeException ex){
                LOGGER.log(Level.INFO, "fail to delete file: "+file.getAbsolutePath(),ex);
            }

        }
    }

    public void tryLock() {
        try {
            final FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.close();
            lockChannel = new RandomAccessFile(file, "rw").getChannel();
            final FileLock lock = lockChannel.tryLock();
            if(lock == null) {
                throw new IOException("fail to generate new lock");
            }
            this.lock = lock;
        }
        catch (IOException ex) {
            LOGGER.log(Level.INFO, "fail to generate lock",ex);
        }
    }

    public void unLock() {

        closeLockFile();

        closeLockChannel();

        deleteFileIfExist();
    }



    private void closeLockFile() {
        try {
            if(lock != null){
                lock.release();
                lock.close();
                lock = null;
            }
        }
        catch (IOException ex){
            LOGGER.log(Level.WARNING, "fail to release lock file",ex);
        }
    }

    private void closeLockChannel() {
        try {
            if (lockChannel != null) {
                lockChannel.close();
                lockChannel = null;
            }
        }
        catch (IOException ex){
            LOGGER.log(Level.WARNING, "fail to release lock channel",ex);
        }
    }
}
