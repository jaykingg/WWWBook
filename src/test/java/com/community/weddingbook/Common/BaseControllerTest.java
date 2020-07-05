package com.community.weddingbook.Common;

import com.community.weddingbook.Author.Author;
import com.community.weddingbook.Author.AuthorRole;
import com.community.weddingbook.Author.AuthorServiceImpl;
import com.community.weddingbook.Board.Board;
import com.community.weddingbook.Board.BoardServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Ignore
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected AppProperties appProperties;

    @Autowired
    protected AuthorServiceImpl authorServiceImpl;

    @Autowired
    protected BoardServiceImpl boardServiceImpl;

    @Before
    public void createTddAccount() {
        Author author = Author.builder()
                .id("PO_TEST_ID")
                .password("abc1234")
                .name("PO_TEST")
                .roles(Set.of(AuthorRole.USER))
                .build();

        authorServiceImpl.saveAuthor(author);

        IntStream.rangeClosed(1, 22).forEach(index -> {
            Board initBoards = Board.builder()
                    .build();

           // Board newBoard = postRepository.save(initPost);

        });
    }

    public String getBearerToken() throws Exception {
        return "Bearer " + getAccessToken();
    }


    public String getAccessToken() throws Exception{
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(),appProperties.getClientSecret())) //basic Auth 라는 header를 만듦.
                .param("username","PO_TEST")
                .param("password","abc1234")
                .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists()
                );

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(responseBody).get("access_token").toString();
    }

    public String getRefreshToken() throws Exception{
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(),appProperties.getClientSecret())) //basic Auth 라는 header를 만듦.
                .param("username","PO_TEST")
                .param("password","abc1234")
                .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("refresh_token").exists()
                );

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(responseBody).get("refresh_token").toString();
    }
}
