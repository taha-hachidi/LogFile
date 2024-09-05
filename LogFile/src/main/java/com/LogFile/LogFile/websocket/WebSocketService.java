package com.LogFile.LogFile.websocket;


import com.LogFile.LogFile.dto.LogfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendLogUpdate(LogfileDTO logfileDTO) {
        messagingTemplate.convertAndSend("/topic/logs", logfileDTO);
    }
}

