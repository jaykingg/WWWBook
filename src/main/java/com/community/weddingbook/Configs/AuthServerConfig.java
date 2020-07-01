package com.community.weddingbook.Configs;

import com.community.weddingbook.Author.AuthorServiceImpl;
import com.community.weddingbook.Common.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    AuthorServiceImpl authorServiceImpl;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AppProperties appProperties;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder)
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()");

    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(appProperties.getClientId())
                .authorizedGrantTypes("password","refresh_token")
                .scopes("read","write")
                .secret(this.passwordEncoder.encode(appProperties.getClientSecret()))
                /* TEST 를 위해 유효기간을 여유롭게 잡음. */
                .accessTokenValiditySeconds(24 * 60 * 60 * 7)
                .refreshTokenValiditySeconds(24 * 60 * 60 * 7 * 2);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(authorServiceImpl)
                .tokenStore(tokenStore);
    }


    @Primary
    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(
                "http://localhost:8080/oauth/check_token");
        tokenService.setClientId(appProperties.getClientId());
        tokenService.setClientSecret(appProperties.getClientSecret());
        return tokenService;
    }

}


