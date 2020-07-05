package com.community.weddingbook.Board;

import com.community.weddingbook.Author.Author;
import com.community.weddingbook.Author.AuthorRepository;
import com.community.weddingbook.Author.AuthorRole;
import com.community.weddingbook.Author.AuthorServiceImpl;
import com.community.weddingbook.Board.Dto.BoardDeleteDto;
import com.community.weddingbook.Board.Dto.BoardDto;
import com.community.weddingbook.Board.Dto.BoardPutDto;
import com.community.weddingbook.Common.BaseControllerTest;
import com.community.weddingbook.Common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoardControllerTest extends BaseControllerTest {

    @Autowired
    AuthorServiceImpl authorServiceImpl;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BoardRepository boardRepository;

    @Before
    public void createTestAuthor() {
        Author testAuthor= Author.builder()
                .id("PO_TEST_ID")
                .name("PO")
                .password("1234")
                .roles(Set.of(AuthorRole.USER))
                .build();

        this.authorServiceImpl.saveAuthor(testAuthor);
    }

    @Test
    @TestDescription("잘못된 토큰을 가지고 자원에 접근할 경우 테스트")
    public void wroungAccessTokenTest() throws Exception {
        this.mockMvc.perform(post("/api/board/makeCases")
                .header(HttpHeaders.AUTHORIZATION, "Bearer wroungToken"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }


    @Test
    @TestDescription("게시글 저장 테스트")
    public void saveBoardTest() throws Exception {

        BoardDto testBoardDto = BoardDto.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .password("1234abcd")
                .build();

        this.mockMvc.perform(post("/api/board")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(testBoardDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("content").exists())
                .andExpect(jsonPath("password").exists())
                .andExpect(jsonPath("_links").exists())
                ;
    }

    @Test
    @TestDescription("게시글 리스트 조회 테스트")
    public void getBoardListTest() throws Exception {
        /* 테스트 유저 가져오기 */
        Optional<Author> optionalTestAuthor = authorRepository.findById("PO_TEST_ID");
        Author testAuthor = optionalTestAuthor.get();

        /* TEST 게시글 생성 */
        Board testBoard = Board.builder()
                .author(testAuthor)
                .title("테스트 제목")
                .content("테스트 내용")
                .password("1234abcd")
                .build();

        /* 저장 */
        boardRepository.save(testBoard);

        this.mockMvc.perform(get("/api/board/list")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.boardList[0].title").exists())
                .andExpect(jsonPath("_embedded.boardList[0].content").exists())
                .andExpect(jsonPath("_links").exists())
        ;
    }

    @Test
    @TestDescription("게시글 상세 조회 테스트")
    public void getBoardTest() throws Exception {
        /* 테스트 유저 가져오기 */
        Optional<Author> optionalTestAuthor = authorRepository.findById("PO_TEST_ID");
        Author testAuthor = optionalTestAuthor.get();

        /* TEST 게시글 생성 */
        Board testBoard = Board.builder()
                .author(testAuthor)
                .title("테스트 제목")
                .content("테스트 내용")
                .password("1234abcd")
                .build();

        /* 저장 */
        Board resultBoard = boardRepository.save(testBoard);

        this.mockMvc.perform(get("/api/board/{id}",resultBoard.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("content").exists())
        ;


    }

    @Test
    @TestDescription("게시글 삭제 테스트")
    public void deleteBoardTest() throws Exception {
        /* 테스트 유저 가져오기 */
        Optional<Author> optionalTestAuthor = authorRepository.findById("PO_TEST_ID");
        Author testAuthor = optionalTestAuthor.get();

        /* TEST 게시글 생성 */
        Board testBoard = Board.builder()
                .author(testAuthor)
                .title("테스트 제목")
                .content("테스트 내용")
                .password("1234abcd")
                .build();

        /* 저장 */
        Board resultBoard = boardRepository.save(testBoard);

        BoardDeleteDto testBoardDeleteDto = BoardDeleteDto.builder()
                .id(resultBoard.getId())
                .password("1234abcd")
                .build();

        this.mockMvc.perform(delete("/api/board")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(testBoardDeleteDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("content").exists())
        ;
    }

    @Test
    @TestDescription("게시글 수정 테스트")
    public void putBoardTest() throws Exception {
        /* 테스트 유저 가져오기 */
        Optional<Author> optionalTestAuthor = authorRepository.findById("PO_TEST_ID");
        Author testAuthor = optionalTestAuthor.get();

        /* TEST 게시글 생성 */
        Board testBoard = Board.builder()
                .author(testAuthor)
                .title("테스트 제목")
                .content("테스트 내용")
                .password("1234abcd")
                .build();

        /* 저장 */
        Board resultBoard = boardRepository.save(testBoard);

        /* DTO 세팅 */
        BoardPutDto testBoardPutDto = BoardPutDto.builder()
                .id(resultBoard.getId())
                .title("변경 테스트 제목")
                .content("변경 테스트 내용")
                .password("1234abcd")
                .build();

        this.mockMvc.perform(put("/api/board")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(testBoardPutDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("content").exists())
        ;
    }

    @Test
    @TestDescription("테스트 게시글 생성 테스트")
    public void saveTestBoardContentTest() throws Exception {
        this.mockMvc.perform(post("/api/board/makeCases")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @TestDescription("저장 시 제목 조건을 만족하지 못할 경우 테스트")
    public void whenSaveWrongTitleTest() throws Exception {
        /* 테스트 게시글 생성 */
        BoardDto testBoardDto = BoardDto.builder()
                .title("12345678910123456789012345678")
                .content("테스트 내용")
                .password("1234abcd")
                .build();

        this.mockMvc.perform(post("/api/board")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(testBoardDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("저장 시 내용 조건을 만족하지 못할 경우 테스트")
    public void whenSaveWrongConentTest() throws Exception {
        /* 테스트 게시글 생성 */
        BoardDto testBoardDto = BoardDto.builder()
                .title("테스트 제목")
                .content("fksdlfnsdklfjksldfnwfnwnwdfdfdlkjakljkfljdlkfjdkjflkdjfkljflkdjflkjflkdjfkjfkldjfkdajfldjflkdfjkdalfjadklfjaklfjaklfjalkfjalkdfjafdljfklfjlakjfakadjflkajfkla" +
                        "fksdlfnsdklfjksldfnwfnwnwdfdfdlkjakljkfljdlkfjdkjflkdjfkljflkdjflkjflkdjfkjfkldjfkdajfldjflkdfjkdalfjadklfjaklfjaklfjalkfjalkdfjafdljfklfjlakjfakadjflkajfkla")
                .password("1234abcd")
                .build();

        this.mockMvc.perform(post("/api/board")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(testBoardDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("저장 시 비밀번호 조건을 만족하지 못할 경우 테스트")
    public void whenSaveWrongPasswordTest() throws Exception {
        /* 테스트 게시글 생성 */
        BoardDto testBoardDto = BoardDto.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .password("123445567")
                .build();

        this.mockMvc.perform(post("/api/board")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(testBoardDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("삭제 시 비밀번호가 다를 경우 테스트")
    public void whenDeleteWrongPasswordTest() throws Exception {
        /* 테스트 유저 가져오기 */
        Optional<Author> optionalTestAuthor = authorRepository.findById("PO_TEST_ID");
        Author testAuthor = optionalTestAuthor.get();

        /* TEST 게시글 생성 */
        Board testBoard = Board.builder()
                .author(testAuthor)
                .title("테스트 제목")
                .content("테스트 내용")
                .password("1234abcd")
                .build();

        /* 저장 */
        Board resultBoard = boardRepository.save(testBoard);

        BoardDeleteDto testBoardDeleteDto = BoardDeleteDto.builder()
                .id(resultBoard.getId())
                .password("1234abcdefg")
                .build();

        this.mockMvc.perform(delete("/api/board")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(testBoardDeleteDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;

    }

    @Test
    @TestDescription("수정 시 비밀번호가 다를 경우 테스트")
    public void whenModifyWrongPasswordTest() throws Exception {
        /* 테스트 유저 가져오기 */
        Optional<Author> optionalTestAuthor = authorRepository.findById("PO_TEST_ID");
        Author testAuthor = optionalTestAuthor.get();

        /* TEST 게시글 생성 */
        Board testBoard = Board.builder()
                .author(testAuthor)
                .title("테스트 제목")
                .content("테스트 내용")
                .password("1234abcd")
                .build();

        /* 저장 */
        Board resultBoard = boardRepository.save(testBoard);

        /* DTO 세팅 */
        BoardPutDto testBoardPutDto = BoardPutDto.builder()
                .id(resultBoard.getId())
                .title("변경 테스트 제목")
                .content("변경 테스트 내용")
                .password("1234abcdefg")
                .build();

        this.mockMvc.perform(put("/api/board")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(testBoardPutDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    public String getAccessToken() throws Exception{
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(),appProperties.getClientSecret()))
                .param("username","PO_TEST_ID")
                .param("password","1234")
                .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists()
                );

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(responseBody).get("access_token").toString();
    }

    public String getBearerToken() throws Exception {
        return "Bearer " + getAccessToken();
    }



}