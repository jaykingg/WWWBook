package com.community.weddingbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableAspectJAutoProxy
@EnableConfigurationProperties
@SpringBootApplication
public class WeddingbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeddingbookApplication.class, args);
    }

}
