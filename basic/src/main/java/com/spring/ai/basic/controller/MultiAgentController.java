package com.spring.ai.basic.controller;

import org.springframework.web.bind.annotation.*;

import com.spring.ai.basic.agent.AgentOrchestrator;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin("http://localhost:5173")
public class MultiAgentController {
    private final AgentOrchestrator agentOrchestrator;
    
    public MultiAgentController(AgentOrchestrator agentOrchestrator) {
        this.agentOrchestrator = agentOrchestrator;
    }
    
    @PostMapping
    public String chat(@RequestBody Map<String, String> requestBody,
                       @RequestHeader(value = "ConversationId", required = false) String conversationId){

        String userMessage=requestBody.get("message");
        String userId = requestBody.get("userId");
        String currentConversationId = (conversationId != null) ? conversationId : UUID.randomUUID().toString();
        return agentOrchestrator.chat(userMessage, userId, currentConversationId);
    }
}