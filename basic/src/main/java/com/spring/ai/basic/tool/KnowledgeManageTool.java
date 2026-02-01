package com.spring.ai.basic.tool;

import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.spring.ai.basic.dto.KnowledgeManageToolDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class KnowledgeManageTool {

    private final VectorStore vectorStore;
    private static final double UPDATE_THRESHOLD = 0.7;

    public KnowledgeManageTool(@Autowired VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Tool(description = "사용자 제공 지식을 벡터화하여 저장하거나 기존 지식을 업데이트한다")
    public KnowledgeManageToolDTO.Response upsert(KnowledgeManageToolDTO.Request request) {
        
        //Input validation
        if (request == null ||
            request.userId() == null ||
            request.texts() == null ||
            request.texts().isEmpty()) {
            return new KnowledgeManageToolDTO.Response(false);
        }

        String userId = request.userId();

        for (String chunk : request.texts()) {

            if (chunk == null || chunk.isBlank()) {
                continue;
            }

            try {
                //Similarity search
                SearchRequest searchRequest = SearchRequest.builder()
                        .query(chunk)
                        .topK(1)
                        .filterExpression("user_id == '" + userId + "'")
                        .build();

                List<Document> similarDocs = vectorStore.similaritySearch(searchRequest);

                if (!similarDocs.isEmpty()
                    && similarDocs.get(0).getScore() != null
                    && similarDocs.get(0).getScore() >= UPDATE_THRESHOLD) {

                    //Update
                    Document existing = similarDocs.get(0);
                    vectorStore.delete(List.of(existing.getId()));

                    Document merged = new Document(
                            chunk,
                            Map.of(
                                    "user_id", userId,
                                    "source", "user_provided",
                                    "created_at", LocalDate.now().toString()
                            )
                    );
                    vectorStore.add(List.of(merged));

                } else {
                    //Insert
                    Document doc = new Document(
                            chunk,
                            Map.of(
                                    "user_id", userId,
                                    "source", "user_provided",
                                    "created_at", LocalDate.now().toString()
                            )
                    );
                    vectorStore.add(List.of(doc));
                }

            } catch (Exception e) {
                return new KnowledgeManageToolDTO.Response(false);
            }
        }

        return new KnowledgeManageToolDTO.Response(true);
    }
}
