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
        String gmailToken = env.getProperty("GMAIL_ACCESS_TOKEN");

        
        if (gmailToken != null) {
            envs.put("GMAIL_ACCESS_TOKEN", gmailToken);
        }

        if (isWindows()) {
            // Windows: cmd.exe /c npx approach
            var winArgs = new ArrayList<>(Arrays.asList(
                "/c", "npx", "-y", "@infograb/gmail-mcp-server")); 
            
            stdioParams = ServerParameters.builder("cmd.exe")
                    .args(winArgs)
                    .env(envs)
                    .build();
        } else {
            // Linux/Mac: direct npx approach
            stdioParams = ServerParameters.builder("npx")
                    .args("@infograb/gmail-mcp-server@1.0.0")
                    .env(envs)
                    .build();
        }

        McpSyncClient mcpClient = McpClient.sync(new StdioClientTransport(stdioParams, McpJsonMapper.createDefault()))
                .requestTimeout(Duration.ofSeconds(10))
                .build();

        mcpClient.initialize();
        
        //If mcpClient is connected successfully, you can uncomment the following line to see the available tools.
        System.out.println(mcpClient.listTools());

        return mcpClient;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}