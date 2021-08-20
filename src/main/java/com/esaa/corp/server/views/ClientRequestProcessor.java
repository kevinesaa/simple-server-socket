package com.esaa.corp.server.views;

import com.esaa.corp.commons.views.DataProcessor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRequestProcessor implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ClientRequestProcessor.class.getSimpleName());

    private final File stopFile;
    private Socket clientSocket;
    private final DataProcessor dataProcessor;
    private boolean isCompleted;
    private boolean isProcessing;

    private InputStream inputStream;
    private OutputStream outputStream;

    public ClientRequestProcessor(final File stopFile, final Socket clientSocket, final DataProcessor dataProcessor) {
        this.stopFile = stopFile;
        this.clientSocket = clientSocket;
        this.dataProcessor = dataProcessor;
        this.isCompleted = false;
        this.isProcessing = false;
    }

    @Override
    public void run() {

        LOGGER.log(Level.INFO,"starting processing client request");
        setProcessing(true);
        readClientRequest();
        setOutputStream();
        processRequest();
        int i=0;
        final int stop = 1000;
        while (i++ < stop) {}
        responseRequest();
        close();
        setCompleted(true);
        setProcessing(false);
        LOGGER.log(Level.INFO,"processing client completed");
    }

    public void close() {
        closeInputStream();
        closeOutputStream();
        closeSocket();
    }

    private void readClientRequest() {
        LOGGER.log(Level.INFO,"reading client request");
        try {
            inputStream = clientSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processRequest() {
        LOGGER.log(Level.INFO,"processing client request");
    }

    private void responseRequest() {
        LOGGER.log(Level.INFO,"responding client request");
    }

    private void setOutputStream() {
        LOGGER.log(Level.INFO,"setting client response");
        try {
            outputStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeInputStream() {
        if(inputStream != null) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeOutputStream() {
        if(outputStream != null) {
            try {
                outputStream.close();
                outputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeSocket() {
        if(clientSocket != null) {
            try {
                clientSocket.close();
                clientSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized boolean isProcessing() {
        synchronized (this) {
            return isProcessing;
        }
    }

    synchronized void setProcessing(boolean processing) {

        synchronized (this) {
            isProcessing = processing;
        }
    }

    synchronized boolean isCompleted() {
        synchronized (this) {
            return isCompleted;
        }
    }

    synchronized void setCompleted(boolean completed) {
        synchronized (this) {
            isCompleted = completed;
        }
    }


}
