package com.esaa.corp.server.util;

import com.esaa.corp.commandOperations.models.CommandArgs;
import com.esaa.corp.server.models.StartFile;
import com.esaa.corp.commons.utils.JsonParser;
import com.esaa.corp.server.models.ConfigFileModel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerStartUtil {

    private static final Logger LOGGER = Logger.getLogger(ServerStartUtil.class.getSimpleName());

    private static final int CONFIG_FILE = 0;

    public static StartFile parseConfigFile(final CommandArgs args) {

        final StartFile startFile = new StartFile();

        final String configString = args.getArgument(CONFIG_FILE);
        File configFile = new File(configString);
        final boolean exists = configFile.exists();
        final boolean isFile = configFile.isFile();
        final boolean canRead = configFile.canRead();

        if (!exists) {
            LOGGER.log(Level.SEVERE, configString + ": is not exist");
        }


        if (!isFile) {
            LOGGER.log(Level.SEVERE, configString + " is not a file");
        }


        if (!canRead) {
            LOGGER.log(Level.SEVERE, configString + " could not be read");
        }

        if (!configFile.isAbsolute()) {
            configFile = configFile.getAbsoluteFile();
        }

        if (exists && isFile && canRead) {
            startFile.setAbsoluteFile(configFile);
            InputStream fileInputStream = null;
            InputStreamReader inputStream = null;
            BufferedReader bufferedInputStream = null;

            try {
                fileInputStream = new FileInputStream(configFile);
                inputStream = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                bufferedInputStream = new BufferedReader(inputStream);
                final StringBuilder stringBuilder = new StringBuilder();
                bufferedInputStream.lines().forEach(stringBuilder::append);
                final String json = stringBuilder.toString();
                final ConfigFileModel configFileModel = JsonParser.getInstance().fromJson(json, ConfigFileModel.class);
                startFile.setConfigFileModel(configFileModel);
            }
            catch (Exception ex) {
                ex.printStackTrace();

            }
            finally {

                try {

                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();

                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return startFile;
    }

}

