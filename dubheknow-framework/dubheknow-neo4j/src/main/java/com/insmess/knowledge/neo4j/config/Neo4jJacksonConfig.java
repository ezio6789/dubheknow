package com.insmess.knowledge.neo4j.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.neo4j.driver.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Neo4jJacksonConfig {

    @Bean
    public SimpleModule neo4jValueModule() {
        SimpleModule module = new SimpleModule("Neo4jValueModule");
        module.addSerializer(Value.class, new Neo4jValueSerializer());
        return module;
    }
}