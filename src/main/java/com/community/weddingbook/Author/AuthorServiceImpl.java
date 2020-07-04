package com.community.weddingbook.Author;

import com.community.weddingbook.Common.AppProperties;
import com.community.weddingbook.Configs.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService, UserDetailsService {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AppProperties appProperties;

    @Autowired
    AppConfig appConfig;


    /* Security */
    /**************************************************************************************/
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Author author = authorRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException(id));

        return new User(author.getId(),author.getPassword(),authorities(author.getRoles()));
    }

    private Collection<? extends GrantedAuthority> authorities(Set<AuthorRole> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toSet());
    }
    /**************************************************************************************/

    @Override
    public Author saveAuthor(Author author) {
        author.setPassword(this.passwordEncoder.encode(author.getPassword()));
        return this.authorRepository.save(author);
    }

    @Override
    public ResponseEntity getTestID() {

        Author testAuthor= Author.builder()
                .id("PO_TEST_ID")
                .name("PO")
                .password("1234")
                .roles(Set.of(AuthorRole.USER))
                .build();


        this.saveAuthor(testAuthor);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(appProperties.getClientId(),appProperties.getClientSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type","password");
        parameters.add("username", "PO_TEST_ID");
        parameters.add("password", "1234");
        HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(parameters,headers);

        Jackson2JsonParser parser2 = new Jackson2JsonParser();
        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.postForObject("http://localhost:8080/oauth/token",requestEntity,String.class);

        System.out.println(response);

        String getaccess_Token = parser2.parseMap(response).get("access_token").toString();

        testAuthor.setServiceAccessToken(getaccess_Token);

        /* 마지막으로 최종 저장  */
        Author resultAuthor = this.saveAuthor(testAuthor);


        return ResponseEntity.ok(resultAuthor);
    }
}
