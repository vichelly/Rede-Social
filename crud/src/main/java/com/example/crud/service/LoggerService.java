package com.example.crud.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LoggerService {
    private final String logFile;

    public LoggerService(String processId) {
        this.logFile = "log_" + processId + ".txt";
    }

    public void log(String message) {
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(LocalDateTime.now() + " - " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}