package com.LogFile.LogFile.controller;

import com.LogFile.LogFile.dto.LogfileDTO;
import com.LogFile.LogFile.model.Logfile;
import com.LogFile.LogFile.service.ILogfileService;
import org.modelmapper.internal.bytebuddy.build.BuildLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/logfile")
public class LogfileController {

    @Autowired
    public ILogfileService iLogfileService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/severity/{severity}")
    public List<LogfileDTO> getLogsBySeverity(@PathVariable String severity) {
        return iLogfileService.getLogsBySeverity(severity);
    }

    @GetMapping("/{id}")
    public LogfileDTO getLogById(@PathVariable Long id) {
        return iLogfileService.getLogById(id);
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadLogFile(@RequestParam("file") MultipartFile file) {
        try {
            List<LogfileDTO> logEntries = parseLogFile(file);
            iLogfileService.saveLogEntries(logEntries);
            messagingTemplate.convertAndSend("/topic/logs", logEntries);
            return ResponseEntity.ok("File uploaded and parsed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }

    private List<LogfileDTO> parseLogFile(MultipartFile file) {
        List<LogfileDTO> logEntries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                LogfileDTO logfileDTO = new LogfileDTO();
                String[] parts = line.split(" ", 5);

                if (parts.length < 5) {

                    System.out.println("Skipping line: " + line);
                    continue;
                }

                logfileDTO.setDate(parts[0]);
                logfileDTO.setTime(parts[1]);
                logfileDTO.setSeverity(parts[2]);
                logfileDTO.setThread(parts[3]);
                logfileDTO.setMessage(parts[4]);
                logEntries.add(logfileDTO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logEntries;
    }



}
