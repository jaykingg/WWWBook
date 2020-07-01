package com.community.weddingbook.Configs;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "resource_id";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers("/api/**","/oauth/**")
                .and()
                .anonymous()
                .and()
                .authorizeRequests()
                    .antMatchers("/api").permitAll()
                    .antMatchers("/oauth/token").permitAll()
                    .antMatchers("/oauth/check_token").permitAll()
                    .antMatchers("/api/account/login").permitAll()
                    .antMatchers("/api/account/refresh").permitAll()
                    .antMatchers("/api/account/checkToken").permitAll()
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .anyRequest()
                        .authenticated()
                .and()
                .formLogin().disable()
                .cors()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

}
