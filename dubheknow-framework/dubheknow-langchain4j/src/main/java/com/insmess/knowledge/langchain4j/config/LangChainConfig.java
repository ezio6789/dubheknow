package com.insmess.knowledge.langchain4j.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insmess.knowledge.langchain4j.assistant.*;
import dev.langchain4j.http.client.jdk.JdkHttpClientBuilder;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class LangChainConfig {

    @Resource
    private BigModelConfigure bigModelConfigure;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OllamaEmbeddingModel.builder()
                .modelName("bge-m3:latest") // 或其他 embedding 模型
                .baseUrl("http://localhost:11434")
                .build();
    }


    @Bean
    @ConditionalOnProperty(prefix = "big-model", name = "type", havingValue = "openai")
    public OpenAiChatModel openAiChatModel() {
        return OpenAiChatModel.builder()
                .baseUrl(bigModelConfigure.getUrl())
                .apiKey(bigModelConfigure.getApiKey())
                .logRequests(true)
                .logResponses(true)
                .modelName(bigModelConfigure.getModel())
                .httpClientBuilder(
                        new JdkHttpClientBuilder()
                                .httpClientBuilder(
                                        HttpClient.newBuilder()
                                                .version(HttpClient.Version.HTTP_1_1) // ⚠️ 强制 1.1
                                                .connectTimeout(Duration.ofSeconds(10))
                                )
                                .readTimeout(Duration.ofSeconds(120))
                )
                .temperature(0.6)
                .timeout(Duration.ofSeconds(600))
                .build();
    }

    // 流式模型（用于 chat 方法的 Flux 响应）
    @Bean
    @ConditionalOnProperty(prefix = "big-model", name = "type", havingValue = "openai")
    public OpenAiStreamingChatModel openAiStreamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl(bigModelConfigure.getUrl()) // 与非流式保持一致
                .apiKey(bigModelConfigure.getApiKey()) // 同上
                .modelName(bigModelConfigure.getModel()) // 需支持流式的模型
                .httpClientBuilder(
                        new JdkHttpClientBuilder()
                                .httpClientBuilder(
                                        HttpClient.newBuilder()
                                                .version(HttpClient.Version.HTTP_1_1)
                                                .connectTimeout(Duration.ofSeconds(10))
                                )
                                .readTimeout(Duration.ofMinutes(5)) // 流式超时需更长
                )
                .temperature(0.6)
                .build();
    }



    @Bean
    @ConditionalOnProperty(prefix = "big-model", name = "type", havingValue = "ollama")
    public OllamaChatModel chatModel() {
        return OllamaChatModel.builder()
                .baseUrl(bigModelConfigure.getUrl())
                .modelName(bigModelConfigure.getModel())
                .temperature(0.6)
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "big-model", name = "type", havingValue = "ollama")
    public OllamaStreamingChatModel chatStreamModel() {
        return OllamaStreamingChatModel.builder()
                .baseUrl(bigModelConfigure.getUrl())
                .modelName(bigModelConfigure.getModel())
                .temperature(0.6)
                .timeout(Duration.ofSeconds(240))
                .build();
    }

//    @Bean
//    public SchemaAssistant schemaAssistant(OllamaChatModel model) {
//        return AiServices.create(SchemaAssistant.class, model);
//    }
//
//    @Bean
//    public DataImportAssistant dataImportAssistant(OllamaChatModel model) {
//        return AiServices.create(DataImportAssistant.class, model);
//    }
//
//    @Bean
//    public QueryAssistant queryAssistant(OllamaChatModel model) {
//        return AiServices.create(QueryAssistant.class, model);
//    }
//
//    @Bean
//    public QAAssistant qaAssistant(OllamaChatModel model) {
//        return AiServices.create(QAAssistant.class, model);
//    }
//
//    @Bean
//    public DisambiguationAssistant disambiguationAssistant(OllamaChatModel model) {
//        return AiServices.create(DisambiguationAssistant.class, model);
//    }
//
//    @Bean
//    public VisualizationAssistant visualizationAssistant(OllamaChatModel model) {
//        return AiServices.create(VisualizationAssistant.class, model);
//    }
//
//    @Bean
//    public EvaluationAssistant evaluationAssistant(OllamaChatModel model) {
//        return AiServices.create(EvaluationAssistant.class, model);
//    }
//
//    @Bean
//    public IntegrationAssistant integrationAssistant(OllamaChatModel model) {
//        return AiServices.create(IntegrationAssistant.class, model);
//    }
//
//    @Bean
//    public SimpleAssistant simpleAssistant(OllamaChatModel model) {
//        return AiServices.create(SimpleAssistant.class, model);
//    }

}
