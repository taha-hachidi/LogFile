package com.LogFile.LogFile.consumer;

import com.LogFile.LogFile.dto.LogfileDTO;
import com.LogFile.LogFile.service.ILogfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogConsumer {

    @Autowired
    private ILogfileService logfileService;

    @KafkaListener(topics = "log-topic", groupId = "log-group")
    public void consume(String message) {
        System.out.println("Received message: " + message);
        LogfileDTO logfileDTO = parseLogEntry(message);
        if (logfileDTO != null) {
            logfileService.saveLogEntries(List.of(logfileDTO));
        }
    }

    private LogfileDTO parseLogEntry(String line) {
        String[] parts = line.split(" ", 5);
        if (parts.length < 5) {
            return null;
        }
        LogfileDTO logfileDTO = new LogfileDTO();
        logfileDTO.setDate(parts[0]);
        logfileDTO.setTime(parts[1]);
        logfileDTO.setSeverity(parts[2]);
        logfileDTO.setThread(parts[3]);
        logfileDTO.setMessage(parts[4].trim());
        return logfileDTO;
    }

}
