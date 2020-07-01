package com.community.weddingbook.Common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties(prefix = "wedding-book")
@Getter
@Setter
public class AppProperties {

    @NotEmpty
    @Value("${wedding-book.client-id}")
    private String clientId;

    @NotEmpty
    @Value("${wedding-book.client-secret}")
    private String clientSecret;

    @NotEmpty
    @Value("${wedding-book.get-oauth-u-r-l}")
    private String getOauthURL;
}
