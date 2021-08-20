package com.esaa.corp.commandOperations.views.operations;

import com.esaa.corp.commandOperations.models.CommandArgs;
import com.esaa.corp.commons.views.DataProcessor;
import com.esaa.corp.printers.PrinterDataProcessor;
import com.esaa.corp.server.models.ConfigFileModel;
import com.esaa.corp.server.models.StartFile;
import com.esaa.corp.server.util.StartFileParser;
import com.esaa.corp.server.views.ServerListener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerStartForeground {

    private static final Logger LOGGER = Logger.getLogger(ServerStartForeground.class.getSimpleName());


    public static void executeCommand(final CommandArgs args) {
        final StartFile configFile = StartFileParser.parseConfigFile(args);
        if (configFile.getAbsoluteFile() == null || configFile.getConfigFileModel() == null) {

            LOGGER.log(Level.SEVERE, "bad format config file");

        }
        else {

            final ConfigFileModel configFileModel = configFile.getConfigFileModel();

            //(port >= 0 && port <= 65535) valid ports
            /* todo check printer port ???
            final String[] portNames = SerialPortList.getPortNames();
            SerialPort serialPort = new SerialPort(configFile.getConfigFileModel().getPrinterPort());
            serialPort.isOpened();
            */

            try {
                final DataProcessor dataProcessor = new PrinterDataProcessor(configFileModel.getPrinterPort());
                final ServerListener server = new ServerListener(configFileModel.getServerPort(),dataProcessor);
                server.runServer();
            }
            catch (IOException | RuntimeException e) {
                LOGGER.log(Level.SEVERE, "Fail to start server listener",e);
            }
        }
    }
}


