package com.spring.ai.basic.agent;

public interface Agent{
    /**
     * Process user message and generate a response
     * @param userMessage User input message
     * @param userId User ID (PK of User entity)
     * @param conversationId Conversation ID for maintaining context
     * @return generated response
     */
    String process(String userMessage, String userId, String conversationId);
}