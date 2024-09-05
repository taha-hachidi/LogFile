package com.LogFile.LogFile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogfileDTO {
    private String date;
    private String time;
    private String severity;
    private String thread;
    private String className;
    private String message;

}

