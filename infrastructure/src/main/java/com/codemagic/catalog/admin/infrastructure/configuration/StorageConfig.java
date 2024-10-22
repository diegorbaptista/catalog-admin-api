package com.codemagic.catalog.admin.infrastructure.configuration;

import com.codemagic.catalog.admin.infrastructure.configuration.properties.google.GoogleStorageProperties;
import com.codemagic.catalog.admin.infrastructure.configuration.properties.storage.StorageProperties;
import com.codemagic.catalog.admin.infrastructure.services.StorageService;
import com.codemagic.catalog.admin.infrastructure.services.impl.GoogleStorageService;
import com.codemagic.catalog.admin.infrastructure.services.impl.LocalStorageService;
import com.google.cloud.storage.Storage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean
    @ConfigurationProperties("storage.catalog-media")
    public StorageProperties properties() {
        return new StorageProperties();
    }

    @Bean(name = "storageService")
    @Profile({"development", "production"})
    public StorageService googleStorageService(
            final GoogleStorageProperties props,
            final Storage storage) {
        return new GoogleStorageService(props.getBucket(), storage);
    }

    @Bean(name = "storageService")
    @ConditionalOnMissingBean
    public StorageService localStorageService() {
        return new LocalStorageService();
    }

}
