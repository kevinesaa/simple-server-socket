package com.esaa.corp.server.views;

import com.esaa.corp.commons.views.DataProcessor;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OnClientConnectListener implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(OnClientConnectListener.class.getSimpleName());

    private final File stopFile;
    private final ServerSocket serverSocket;
    private final DataProcessor dataProcessor;
    private ClientRequestProcessor clientProcessor;
    private boolean clientConnected;
    private boolean isStarted;
    private OnConnectListener onConnectListener;

    OnClientConnectListener(final File stopFile,final ServerSocket serverSocket,final DataProcessor dataProcessor ) {
        this.stopFile = stopFile;
        this.serverSocket = serverSocket;
        this.dataProcessor = dataProcessor;
        this.clientConnected = false;
        this.isStarted = false;
    }

    @Override
    public void run() {
        try {
            setStarted(true);
            final Socket client = serverSocket.accept();
            setClientConnected(true);
            clientProcessor = new ClientRequestProcessor(stopFile,client,dataProcessor);
            if(onConnectListener != null) {
                onConnectListener.onConnect();
            }
            LOGGER.log(Level.INFO,"new client connected");

        }
        catch (IOException e) {
            if(clientProcessor != null) {

                if(!clientProcessor.isCompleted()) {
                    setStarted(false);
                    setClientConnected(false);
                }
            }

            if(!stopFile.exists() && serverSocket.isBound() && !serverSocket.isClosed()) {
                LOGGER.log(Level.WARNING,"fail at new client connection",e);
            }

            closeClient();
        }
    }

    void closeClient() {
        if(clientProcessor != null) {
            clientProcessor.close();
            clientProcessor = null;
        }
    }

    synchronized boolean isStarted() {
        synchronized (this) {
            return isStarted;
        }
    }

    void setStarted(final boolean started) {
        synchronized (this) {
            this.isStarted = started;
        }
    }

    synchronized boolean isClientConnected() {
        synchronized (this) {
            return clientProcessor!=null && clientConnected;
        }
    }

    void setClientConnected(final boolean clientConnected) {
        synchronized (this) {
            this.clientConnected = clientConnected;
        }
    }

    synchronized void processRequest(){
        synchronized (this) {
            LOGGER.log(Level.INFO,"new processing thread");
            if(clientProcessor != null) {
                final Thread thread = new Thread(clientProcessor);
                clientProcessor.setProcessing(true);
                thread.start();
            }
        }
    }

    synchronized boolean isCompleted() {
        synchronized (this) {
            return clientProcessor != null && clientProcessor.isCompleted();
        }
    }

    synchronized boolean isProcessing() {
        synchronized (this) {
            return clientProcessor != null && clientProcessor.isProcessing();
        }
    }

    void setOnConnectListener (OnConnectListener onConnectListener){
        this.onConnectListener = onConnectListener;
    }

    interface OnConnectListener {

         void onConnect() ;
    }
}
