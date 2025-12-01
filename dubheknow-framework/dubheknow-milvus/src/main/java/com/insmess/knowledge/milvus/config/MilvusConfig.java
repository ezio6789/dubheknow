package com.insmess.knowledge.milvus.config;

import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.data.segment.TextSegment;
import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilvusConfig {

    @Bean(destroyMethod = "close")
    public MilvusServiceClient milvusClient() {
        ConnectParam param = ConnectParam.newBuilder()
                .withHost("localhost")
                .withPort(19530)
                .build();
        return new MilvusServiceClient(param);
    }

    @Bean
    public EmbeddingStore<TextSegment> milvusEmbeddingStore(MilvusServiceClient milvusClient) {
        return MilvusEmbeddingStore.builder()
                .milvusClient(milvusClient)
                .collectionName("rag_docs")
                .dimension(1024)
                .indexType(IndexType.HNSW)
                .metricType(MetricType.COSINE)
                .autoFlushOnInsert(false)
                .idFieldName("id")
                .textFieldName("text")
                .metadataFieldName("metadata")
                .vectorFieldName("vector")
                .build();
    }

}
