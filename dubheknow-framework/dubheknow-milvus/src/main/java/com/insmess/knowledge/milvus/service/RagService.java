package com.insmess.knowledge.milvus.service;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RagService {
    @Resource
    private EmbeddingModel embeddingModel;
    @Resource
    private MilvusEmbeddingStore store;

    // 返回生成的向量 ID（因为不能自己传 id，只能让 store 生成）
    public String addDoc(String text, Metadata metadata) {

        TextSegment segment = TextSegment.from(text, metadata);
        Embedding embedding = embeddingModel.embed(segment).content();
        String generatedId = store.add(embedding, segment);
        return generatedId;
    }

    public List<EmbeddingMatch<TextSegment>> search(String query, int topK) {
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        // 使用 EmbeddingSearchRequest 构造搜索请求
        return store.search(EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(topK)
                .build()).matches();
    }

}
