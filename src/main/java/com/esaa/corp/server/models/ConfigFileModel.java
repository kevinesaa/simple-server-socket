package com.esaa.corp.server.models;

public class ConfigFileModel {

    private String printer_port;
    private int server_port;

    public ConfigFileModel() {
    }

    public ConfigFileModel(String printerPort, int serverPort) {
        this.printer_port = printerPort;
        this.server_port = serverPort;
    }

    public String getPrinterPort() {
        return printer_port;
    }

    public void setPrinterPort(String printerPort) {
        this.printer_port = printerPort;
    }

    public int getServerPort() {
        return server_port;
    }

    public void setServerPort(int serverPort) {
        this.server_port = serverPort;
    }
}
