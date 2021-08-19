package com.esaa.corp.fileSystem.views;

import java.io.File;

public class MyFileSystemManager {



    private static MyFileSystemManager INSTANCE;
    private final File jarFile;
    private File jarDir = null;
    private File stopFile = null;
    private File startFile = null;

    private MyFileSystemManager(){
        jarFile = new File(System.getProperty("java.class.path")).getAbsoluteFile();
    }

    public static MyFileSystemManager getInstance() {
        if(INSTANCE == null){
            INSTANCE = new MyFileSystemManager();
        }

        return INSTANCE;
    }

    public File getJarFile() {
        return jarFile;
    }

    public File getJarParentDir() {
        if(jarDir == null) {
            jarDir = jarFile.getParentFile();
        }
        return jarDir;
    }

    public File getStopFile(final int port) {
        if(stopFile == null) {
            stopFile = new File(getJarParentDir(),port+"_stop.lock");
        }

        return stopFile;
    }

    public File getStartFile(final int port) {
        if(startFile == null) {
            startFile = new File(getJarParentDir(),port+"_start.lock");
        }

        return startFile;
    }
}
