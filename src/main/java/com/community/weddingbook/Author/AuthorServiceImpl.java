package com.community.weddingbook.Author;

import com.community.weddingbook.Common.AppProperties;
import com.community.weddingbook.Configs.RestTemplateLoggingRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
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

        RestTemplate restTemplate;
        HttpHeaders headers;
        Jackson2JsonParser parser2;

        Author newauthor = Author.builder()
                .id("PO_TD_VAL")
                .name("PO")
                .password("abc1234")
                .roles(Set.of(AuthorRole.USER))
                .build();

        Author afterSaveAuthor = this.saveAuthor(newauthor);

        headers = new HttpHeaders();
        headers.setBasicAuth(appProperties.getClientId(),appProperties.getClientSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type","password");
        parameters.add("username",newauthor.getId());
        parameters.add("password","abc1234");
        HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(parameters,headers);

        parser2 = new Jackson2JsonParser();

        restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(appProperties.getGetOauthURL(),requestEntity,String.class);
        restTemplate.setInterceptors(Arrays.asList(new RestTemplateLoggingRequestInterceptor()));

        String getaccess_Token = parser2.parseMap(response).get("access_token").toString();
        String getrefrsh_Token = parser2.parseMap(response).get("refresh_token").toString();


        /* Token 제대로 읽었는지 확인 */
        if(getaccess_Token == null || getrefrsh_Token == null ) {
            authorRepository.delete(afterSaveAuthor);
            return ResponseEntity.notFound().build();
        }
        afterSaveAuthor.setServiceAccessToken(getaccess_Token);
        afterSaveAuthor.setServiceRefreshToken(getrefrsh_Token);


        /* 마지막으로 최종 저장  */
        this.authorRepository.save(afterSaveAuthor);


        return ResponseEntity.ok(afterSaveAuthor);
    }
}
