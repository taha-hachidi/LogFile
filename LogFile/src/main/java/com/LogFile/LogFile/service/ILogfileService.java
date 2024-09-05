package com.LogFile.LogFile.service;

import com.LogFile.LogFile.dto.LogfileDTO;
import java.util.List;

public interface ILogfileService {
    List<LogfileDTO> getLogsBySeverity(String severity);
    void saveLogEntries(List<LogfileDTO> logfiles);
    LogfileDTO getLogById(Long id);
}
