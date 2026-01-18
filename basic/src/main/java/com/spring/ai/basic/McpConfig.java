package com.spring.ai.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.time.Duration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.McpJsonMapper;

@Configuration
public class McpConfig {

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(McpSyncClient.class)
    public McpSyncClient mcpClient(Environment env) {
        ServerParameters stdioParams;

        // Environment에서 환경변수 읽기
        Map<String, String> envs = new HashMap<>();
        
        // 공통 환경변수
        String slackBotToken = env.getProperty("SLACK_BOT_TOKEN");
        String slackTeamId = env.getProperty("SLACK_TEAM_ID");
        String slackChannelIds = env.getProperty("SLACK_CHANNEL_IDS");
        
        if (slackBotToken != null) {
            envs.put("SLACK_BOT_TOKEN", slackBotToken);
        }
        if (slackTeamId != null) {
            envs.put("SLACK_TEAM_ID", slackTeamId);
        }
        if (slackChannelIds != null) {
            envs.put("SLACK_CHANNEL_IDS", slackChannelIds);
        }

        if (isWindows()) {
            // Windows: cmd.exe /c npx approach
            var winArgs = new ArrayList<>(Arrays.asList(
                "/c", "npx", "-y", "@modelcontextprotocol/server-slack"));
            
            stdioParams = ServerParameters.builder("cmd.exe")
                    .args(winArgs)
                    .env(envs)
                    .build();
        } else {
            // Linux/Mac: direct npx approach
            stdioParams = ServerParameters.builder("npx")
                    .args("-y", "@modelcontextprotocol/server-slack")
                    .env(envs)
                    .build();
        }

        McpSyncClient mcpClient = McpClient.sync(new StdioClientTransport(stdioParams, McpJsonMapper.createDefault()))
                .requestTimeout(Duration.ofSeconds(10))
                .build();

        mcpClient.initialize();
        
        //If mcpClient is connected successfully, you can uncomment the following line to see the available tools.
        //System.out.println(mcpClient.listTools());

        return mcpClient;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}