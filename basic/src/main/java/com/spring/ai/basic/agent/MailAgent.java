package com.spring.ai.basic.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.modelcontextprotocol.client.McpSyncClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

@Component
public class MailAgent implements Agent{

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final String systemPrompt;

    public MailAgent(ChatClient.Builder builder,
                    @Qualifier("workerPrompts") Map<String,String> prompts,
                    ChatMemory chatMemory,
                    McpSyncClient mcpClient) {
        this.chatClient = builder.defaultToolCallbacks(new SyncMcpToolCallbackProvider.Builder().addMcpClient(mcpClient).build()).build();
        this.systemPrompt = prompts.get("mail");
        this.chatMemory = chatMemory;
    }

    public String process(String userMessage, String userId, String conversationId) {
        // Current date in "yyyy-MM-dd (E요일)" format
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd (E요일)", Locale.KOREAN));
        // Inject variables into the system prompt
        PromptTemplate template = new PromptTemplate(systemPrompt);
        String finalPrompt = template.render(Map.of("current_date", currentDate, "user_id", userId));

        // Agent = ChatClient + Prompt + Advisor
        try{ 
            String response = chatClient.prompt()
                .system(finalPrompt)
                .user(userMessage)
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error processing user message: " + e.getMessage());
            return "요청처리오류";
        }
    }
}