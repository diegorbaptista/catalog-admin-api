package com.codemagic.catalog.admin.infrastructure.configuration;

import com.codemagic.catalog.admin.infrastructure.configuration.properties.amqp.QueueProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    @ConfigurationProperties("amqp.queues.video-created")
    public QueueProperties videoCreatedQueueProps() {
        return new QueueProperties();
    }

    @Bean
    @ConfigurationProperties("amqp.queues.video-encoded")
    public QueueProperties videoEncodedQueueProps() {
        return new QueueProperties();
    }
}
