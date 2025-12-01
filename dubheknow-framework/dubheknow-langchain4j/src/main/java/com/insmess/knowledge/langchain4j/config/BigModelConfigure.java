package com.insmess.knowledge.langchain4j.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "big-model")
@Data
public class BigModelConfigure {
    private String type;

    private String url;

    private String model;

    private String apiKey;
}
