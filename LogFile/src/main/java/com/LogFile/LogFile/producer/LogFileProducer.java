package com.LogFile.LogFile.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

@Component
public class LogFileProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String FILE_PATH = "/app/logs/FINEA_TRACE-2022-11-23-1.log";
    private static final String TOPIC = "log-topic";
    private long lastFilePointer = 0;

    @PostConstruct
    public void startReadingLogFile() {
        new Thread(() -> {
            while (true) {
                try {
                    readNewLogEntries();
                    Thread.sleep(5000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void readNewLogEntries() throws IOException {
        File file = new File(FILE_PATH);
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(lastFilePointer);
            String line;
            long currentPointer = lastFilePointer;

            while ((line = raf.readLine()) != null) {

                if (!line.matches("^\\d{4}-\\d{2}-\\d{2}.*")) {

                    System.out.println("Invalid line detected, repositioning file pointer...");
                    raf.seek(currentPointer);
                    break;
                }

                System.out.println("Sending line: " + line);
                kafkaTemplate.send(TOPIC, line);
                currentPointer = raf.getFilePointer();
            }
            lastFilePointer = currentPointer;
            System.out.println("Updated file pointer: " + lastFilePointer);
        }
    }

}
