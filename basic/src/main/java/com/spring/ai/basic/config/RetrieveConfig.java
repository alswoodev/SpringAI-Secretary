package com.spring.ai.basic.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;

import jakarta.annotation.PostConstruct;

@Configuration
public class RetrieveConfig {
    private static final Logger log = LoggerFactory.getLogger(RetrieveConfig.class);

    @Value("classpath:documentReference.md")
    private Resource reference;

    @Autowired private VectorStore vectorStore;
    @Autowired private JdbcClient jdbcClient;

    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore){
        return RetrievalAugmentationAdvisor.builder()
            .documentRetriever(VectorStoreDocumentRetriever.builder()
                    .similarityThreshold(0.50)
                    .topK(5)
                    .vectorStore(vectorStore)
                    .build())
            .queryAugmenter(ContextualQueryAugmenter.builder()
                    .allowEmptyContext(true) //allow no context scenario
                    .build())
            .build();
    }

    @PostConstruct
    public void baseReferenceDataSetup() {
        try {
            // Initialize vector store by clearing existing data
            String sql = "TRUNCATE TABLE vector_store";
            jdbcClient.sql(sql).update();

            log.info("데이터 로딩을 시작합니다...");

            // Creaste document from whole reference file
            String content = new String(reference.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            Document document = new Document(content);

            // Split document into chunks
            TokenTextSplitter splitter = new TokenTextSplitter(800, 200, 10, 5000, true);
            List<Document> chunks = splitter.split(document);

            // Save chunks
            vectorStore.accept(chunks);
            log.info(chunks.size() + "개의 청크가 저장되었습니다.");
            log.info("데이터 로딩 완료.");

        } catch (Exception e) {
            log.error("데이터 로딩 중 오류 발생", e);
        }
    }
}
