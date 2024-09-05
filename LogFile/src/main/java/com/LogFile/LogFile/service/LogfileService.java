package com.LogFile.LogFile.service;

import com.LogFile.LogFile.dto.LogfileDTO;
import com.LogFile.LogFile.model.Logfile;
import com.LogFile.LogFile.repository.LogfileRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.LogFile.LogFile.websocket.WebSocketService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class LogfileService implements ILogfileService {

    private static final Logger log = LoggerFactory.getLogger(LogfileService.class);

    @Autowired
    private LogfileRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private EmailService emailService;

    @Value("${alert.email.to}")
    private String alertEmailTo;
    @Override
    public List<LogfileDTO> getLogsBySeverity(String severity) {
        List<Logfile> logfiles = repository.findBySeverityOrderByDateDescTimeDesc(severity);
        return logfiles.stream()
                .map(logEntry -> modelMapper.map(logEntry, LogfileDTO.class))
                .collect(Collectors.toList());
    }



    @Override
    @Transactional
    public void saveLogEntries(List<LogfileDTO> logfiles) {
        log.info("Received {} log entries to save.", logfiles.size());
        List<Logfile> logfileEntities = logfiles.stream()
                .map(logfileDTO -> {
                    Logfile logEntity = modelMapper.map(logfileDTO, Logfile.class);
                    log.info("Mapped LogfileDTO to Logfile: {}", logEntity);


                    if (!repository.existsByTime(logEntity.getTime())) {

                        if ("GRAVE".equalsIgnoreCase(logfileDTO.getSeverity()) || "AVERTISSEMENT".equalsIgnoreCase(logfileDTO.getSeverity())) {
                            String subject = "Log Alert: " + logfileDTO.getSeverity();
                            String message = String.format("Date: %s\nTime: %s\nSeverity: %s\nMessage: %s",
                                    logfileDTO.getDate(), logfileDTO.getTime(), logfileDTO.getSeverity(), logfileDTO.getMessage());
                            emailService.sendErrorNotification(alertEmailTo, subject, message);
                        }
                        repository.save(logEntity);
                    } else {
                        log.info("Log entry already exists: {}", logEntity);
                    }

                    return logEntity;
                })
                .collect(Collectors.toList());
        log.info("Saved {} log entries to the database.", logfileEntities.size());

        logfiles.forEach(webSocketService::sendLogUpdate);
    }




    @Override
    public LogfileDTO getLogById(Long id) {
        Optional<Logfile> logfile = repository.findById(id);
        LogfileDTO logfileDTO = modelMapper.map(logfile,LogfileDTO.class);
        return logfileDTO;
    }
}
