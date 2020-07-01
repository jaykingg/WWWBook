package com.community.weddingbook.Configs;

import com.community.weddingbook.Author.*;
import com.community.weddingbook.Common.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Set;

@Configuration
public class AppConfig {

    @Autowired
    AppProperties appProperties;

    @Autowired
    AuthorServiceImpl authorServiceImpl;

    @Autowired
    AuthorRepository authorRepository;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /* Password 암호화 Encoder */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    /* 구동 시 테스트 유저 생성, 테스트 게시물 생성 */
    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                Author author = Author.builder()
                        .id("PO_TEST_ID")
                        .password("abc1234")
                        .name("PO_TEST")
                        .roles(Set.of(AuthorRole.USER))
                        .build();

                authorServiceImpl.saveAuthor(author);

                /* 시작하자마자 Oauth Token 받기 (테스트용) */
//                HttpHeaders headers = new HttpHeaders();
//                headers.setBasicAuth(appProperties.getClientId(), appProperties.getClientSecret());
//                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//                MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
//                parameters.add("grant_type", "password");
//                parameters.add("username", "PO_TEST");
//                parameters.add("password", "abc1234");
//                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, headers);
//
//                Jackson2JsonParser parser2 = new Jackson2JsonParser();
//
//                RestTemplate restTemplate = new RestTemplate();
//                String response = restTemplate.postForObject(appProperties.getGetOauthURL(), requestEntity, String.class);
//
//                String getaccess_Token = parser2.parseMap(response).get("access_token").toString();
//                String getrefrsh_Token = parser2.parseMap(response).get("refresh_token").toString();
//
//                System.out.println("***ACCESS_TOKEN***");
//                System.out.println(getaccess_Token);
//                System.out.println("***REFRESH_TOKEN***");
//                System.out.println(getrefrsh_Token);
//
//                Optional<Author> getOptional = authorRepository.findById("PO_TEST");
//                Author newAuthor = getOptional.get();
//                newAuthor.setServiceAccessToken(getaccess_Token);
//                newAuthor.setServiceRefreshToken(getrefrsh_Token);
//                authorServiceImpl.saveAuthor(newAuthor);
            }
        };

    }

}
