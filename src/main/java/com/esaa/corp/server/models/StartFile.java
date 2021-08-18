package com.esaa.corp.server.models;

import java.io.File;

public class StartFile {

    private File absoluteFile;
    private ConfigFileModel configFileModel;

    public StartFile(){}

    public File getAbsoluteFile() {
        return absoluteFile;
    }

    public void setAbsoluteFile(File absoluteFile) {
        this.absoluteFile = absoluteFile;
    }

    public ConfigFileModel getConfigFileModel() {
        return configFileModel;
    }

    public void setConfigFileModel(ConfigFileModel configFileModel) {
        this.configFileModel = configFileModel;
    }
}
