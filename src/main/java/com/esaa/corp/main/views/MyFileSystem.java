package com.esaa.corp.main.views;

import java.io.File;

public class MyFileSystem {



    private static MyFileSystem INSTANCE;
    private final File jarFile;
    private File jarDir = null;
    private File stopFile = null;
    private File startFile = null;

    private MyFileSystem(){
        jarFile = new File(System.getProperty("java.class.path")).getAbsoluteFile();
    }

    public static MyFileSystem getInstance() {
        if(INSTANCE == null){
            INSTANCE = new MyFileSystem();
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
