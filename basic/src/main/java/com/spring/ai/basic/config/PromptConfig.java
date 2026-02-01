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
    @Value("classpath:prompts/worker-mail.st")
    private Resource mailResource;
    @Value("classpath:prompts/worker-shopping.st")
    private Resource shoppingResource;
    @Value("classpath:prompts/worker-personal.st")
    private Resource personalResource;

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
                 "task", loadPrompt(taskResource),
                 "mail", loadPrompt(mailResource),
                 "shopping", loadPrompt(shoppingResource),
                 "personal", loadPrompt(personalResource)
         );
    }
}