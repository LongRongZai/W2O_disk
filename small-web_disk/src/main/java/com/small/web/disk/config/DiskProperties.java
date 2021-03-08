package com.small.web.disk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "disk")
@Data
public class DiskProperties {
    private List<String> loginInterceptorExcludePath;
    private String viewUrl;
    private String attachSavePath;
}