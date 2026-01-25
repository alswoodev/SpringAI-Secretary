package com.spring.ai.basic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Configuration
public class PromptConfig {

    @Value("classpath:prompts/worker-task.st")
    private Resource taskResource;

    private String loadPrompt(Resource resource) {
        try {
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("프롬프트 파일을 읽을 수 없습니다.", e);
        }
    }

    @Bean(name="workerPrompts")
    public Map<String, String> workerPrompts(){
         return Map.of(
                 "task", loadPrompt(taskResource)
         );
    }
}