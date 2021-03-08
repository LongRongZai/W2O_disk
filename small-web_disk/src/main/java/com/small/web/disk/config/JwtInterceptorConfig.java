package com.small.web.disk.config;

import com.small.web.disk.utils.JwtAuthenticationInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lehr
 */
@Configuration
@EnableConfigurationProperties(DiskProperties.class)
public class JwtInterceptorConfig implements WebMvcConfigurer {
    private final DiskProperties diskProperties;

    public JwtInterceptorConfig(DiskProperties diskProperties) {
        this.diskProperties = diskProperties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //默认拦截所有路径
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(diskProperties.getLoginInterceptorExcludePath().toArray(new String[]{}));//白名单URL;
    }

    @Bean
    public JwtAuthenticationInterceptor authenticationInterceptor() {
        return new JwtAuthenticationInterceptor();
    }
}