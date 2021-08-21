package com.esaa.corp.server.views;

import com.esaa.corp.commons.views.DataProcessor;
import com.esaa.corp.fileSystem.models.MyLockFile;
import com.esaa.corp.fileSystem.views.MyFileSystemManager;

import javax.net.ServerSocketFactory;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerListener {

    private static final Logger LOGGER = Logger.getLogger(ServerListener.class.getSimpleName());

    private final ServerSocket serverSocket;
    private final DataProcessor dataProcessor;
    private final MyLockFile lockFile;
    private final File stopFile;
    private OnClientConnectListener currentClientConnect;
    private List<OnClientConnectListener> clientConnectList;

    public ServerListener(final int serverPort, final DataProcessor dataProcessor) throws IOException {
        this.dataProcessor = dataProcessor;
        final ServerSocketFactory socketFactory = ServerSocketFactory.getDefault();
        serverSocket = socketFactory.createServerSocket(serverPort);
        final MyFileSystemManager myFileSystemManager = MyFileSystemManager.getInstance();
        final File startFile = myFileSystemManager.getStartFile(serverPort);
        lockFile = new MyLockFile(startFile);
        lockFile.deleteFileIfExist();
        if (lockFile.isFileExist()) {
            throw new IOException( "the port: " + serverPort +" is been using");
        }
        //lockFile.tryLock();
        if(!lockFile.isLocked()) {
          //  throw new IOException( "the port: " + serverPort +" is been using");
        }
        stopFile = myFileSystemManager.getStopFile(serverPort);


    }

    public void runServer() {

        clientConnectList = Collections.synchronizedList(new ArrayList<>());
        final OnServerStop onServerStop = new OnServerStop();
        final Thread shutDownThread = new Thread(onServerStop);
        Runtime.getRuntime().addShutdownHook(shutDownThread);
        LOGGER.log(Level.INFO,"The server is running");
        while (!stopFile.exists() && serverSocket.isBound() && !serverSocket.isClosed()) {

            if(currentClientConnect == null) {
                lookNewAtTheList();
            }
            else {
                checkCurrentClient();
            }

            if(currentClientConnect == null && clientConnectList.isEmpty()) {
                addNewClientConnectListener();
            }
        }
        stopServer();
    }

    private void lookNewAtTheList() {

        if (!clientConnectList.isEmpty()) {

            int index = -1;
            for (int i = 0; i < clientConnectList.size(); i++) {
                if (clientConnectList.get(i).isClientConnected()) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                currentClientConnect = clientConnectList.remove(index);
            }
        }

    }

    private void checkCurrentClient() {

        if(currentClientConnect != null) {
            if (this.currentClientConnect.isClientConnected()) {
                if (this.currentClientConnect.isCompleted()) {
                    this.currentClientConnect = null;
                } else {
                    if (!this.currentClientConnect.isProcessing()) {
                        this.currentClientConnect.processRequest();
                    }
                }
            }
        }
        if (this.currentClientConnect != null && !this.currentClientConnect.isStarted()) {
            final Thread thread = new Thread(this.currentClientConnect);
            thread.start();
        }
    }

    private void addNewClientConnectListener() {
        if(!stopFile.exists() && serverSocket.isBound() && !serverSocket.isClosed()) {
            final OnClientConnectListener clientConnectListener = new OnClientConnectListener(stopFile,serverSocket, dataProcessor);
            final Thread thread = new Thread(clientConnectListener);
            clientConnectListener.setOnConnectListener(this::addNewClientConnectListener);
            clientConnectList.add(clientConnectListener);
            thread.start();
        }
    }

    private void stopServer() {
        LOGGER.log(Level.INFO,"Stopping Server");
        if(!clientConnectList.isEmpty()) {
            clientConnectList.stream().filter(Objects::nonNull).forEach(OnClientConnectListener::closeClient);
        }
        stopServerSocket();
        lockFile.unLock();
        try {
            if(currentClientConnect != null) {
                /* todo
                while (currentClientConnect.isProcessing()) {

                }

                 */
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("sout: server stopped");
        LOGGER.log(Level.INFO,"Server Stopped");
    }

    private void stopServerSocket() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class OnServerStop implements Runnable {

        @Override
        public void run() {
            stopServer();
        }
    }

}
