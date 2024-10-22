package com.codemagic.catalog.admin.infrastructure.configuration.properties.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class StorageProperties implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(StorageProperties.class);

    private String filenamePattern;
    private String locationPattern;

    public StorageProperties() {}

    @Override
    public String toString() {
        return "StorageProperties {" +
                "filenamePattern='" + filenamePattern + '\'' +
                ", locationPattern='" + locationPattern + '\'' +
                '}';
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info(toString());
    }

    public String getFilenamePattern() {
        return filenamePattern;
    }

    public void setFilenamePattern(String filenamePattern) {
        this.filenamePattern = filenamePattern;
    }

    public String getLocationPattern() {
        return locationPattern;
    }

    public void setLocationPattern(String locationPattern) {
        this.locationPattern = locationPattern;
    }
}
