package com.pickple.commerceservice.infrastructure.configuration;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.support.HttpHeaders;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {
    private final String hostUrl;
    private final String username;
    private final String password;

    public ElasticSearchConfig(@Value("${spring.data.elasticsearch.host}") String hostUrl,
                         @Value("${spring.data.elasticsearch.username}") String username,
                         @Value("${spring.data.elasticsearch.password}") String password) {
        this.hostUrl = hostUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    @SneakyThrows
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(hostUrl)
                .withConnectTimeout(Duration.ofSeconds(5))
                .withSocketTimeout(Duration.ofSeconds(3))
                .withBasicAuth(username, password)
                .withHeaders(() -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("currentTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    return headers;
                })
            .build();
    }
}
