package com.esaa.corp.printers;

import com.esaa.corp.commons.views.DataProcessor;

public class PrinterDataProcessor extends DataProcessor {

    private final String comPort;

    public PrinterDataProcessor(String comPort) {
        this.comPort = comPort;
    }
}
