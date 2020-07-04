package com.community.weddingbook.Board;

import com.community.weddingbook.Author.Author;
import com.community.weddingbook.Author.AuthorRepository;
import com.community.weddingbook.Board.Dto.BoardDeleteDto;
import com.community.weddingbook.Board.Dto.BoardDto;
import com.community.weddingbook.Board.Dto.BoardPutDto;
import com.community.weddingbook.Configs.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    AppConfig appConfig;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardDeletedRepository boardDeletedRepository;

    /** 점검 완료 **/
    @Transactional
    @Override
    public ResponseEntity saveBoard(BoardDto boardDto){
        /* Get Data connected User */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nowUserId = authentication.getName();

        Author getAuthor = this.authorRepository.findById(nowUserId)
                .orElseThrow(() -> new UsernameNotFoundException("계정 정보가 잘못됨"));

        /* Save */
        Board beforeBoard = this.appConfig.modelMapper().map(boardDto, Board.class);
        beforeBoard.setAuthor(getAuthor);
        Board afterBoard = boardRepository.save(beforeBoard);

        /* Self-Descriptive message(Docs-Link), HATEOAS */
        ControllerLinkBuilder selfLinkBuilder = linkTo(BoardController.class).slash("");
        URI createdUri = selfLinkBuilder.toUri();

        BoardResource boardResource = new BoardResource(afterBoard);
        boardResource.add(linkTo(BoardController.class).slash("").withSelfRel());
        boardResource.add(new Link("/docs/index.html#resource-postBoard").withRel("profile"));

        /* Result */
        return ResponseEntity.created(createdUri).body(boardResource);
    }

    /** 점검 완료 **/
    @Transactional
    @Override
    public ResponseEntity getBoardList(@PageableDefault Pageable pageable, PagedResourcesAssembler<Board> assembler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nowUserId = authentication.getName();

        List<Board> boardList = this.boardRepository.findAllByOrderByCreatedAtDesc();
        if(boardList.isEmpty()) {
            ResponseEntity.notFound().build();
        }

        int pageStart = (int) pageable.getOffset();
        int pageEnd = (pageStart + pageable.getPageSize()) > boardList.size() ? boardList.size() : (pageStart + pageable.getPageSize());
        Page<Board> boardsPages = new PageImpl<>(boardList.subList(pageStart, pageEnd), pageable, boardList.size());

        var pagedResources = assembler.toResource(boardsPages);

        pagedResources.add(linkTo(BoardController.class).slash("list").withSelfRel());
        pagedResources.add(linkTo(BoardController.class).slash("").withRel("[POST]게시물 추가"));
        pagedResources.add(linkTo(BoardController.class).slash("list").withRel("[GET]게시물 전체 조회"));
        pagedResources.add(linkTo(BoardController.class).slash("").withRel("[GET]게시물 상세 조회"));
        pagedResources.add(linkTo(BoardController.class).slash("").withRel("[PUT]게시물 수정"));
        pagedResources.add(linkTo(BoardController.class).slash("").withRel("[DELETE]게시물 삭제"));
        pagedResources.add(new Link("/docs/index.html#resource-getBoardList").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    /** 점검 완료 **/
    @Transactional
    @Override
    public ResponseEntity getBoard(@PathVariable Integer id) {
        /* Get Board */
        Board getBoard = this.boardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 값에 대한 게시물이 없습니다."));

        /* Self-Descriptive message(Docs-Link), HATEOAS */
        BoardResource boardResource = new BoardResource(getBoard);
        boardResource.add(linkTo(BoardController.class).slash("").withSelfRel());
        boardResource.add(new Link("/docs/index.html#resource-getBoard").withRel("profile"));

        /* Result */
        return ResponseEntity.ok(boardResource);

    }

    /** 점검 완료 **/
    @Transactional
    @Override
    public ResponseEntity deleteBoard(@RequestBody BoardDeleteDto boardDeleteDto) {
        /* Get Data connected User */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nowUserId = authentication.getName();

        Board getBoard = this.boardRepository.findById(boardDeleteDto.getId()).orElseThrow(()-> new IllegalArgumentException("해당 값에 대한 게시물이 없습니다."));

        /* Get and Exceptions */
        if(!getBoard.getAuthor().getId().equals(nowUserId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(!getBoard.getPassword().equals(boardDeleteDto.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        BoardDeleted newBoardDeleted = this.appConfig.modelMapper().map(getBoard,BoardDeleted.class);

        this.boardDeletedRepository.save(newBoardDeleted);
        this.boardRepository.delete(getBoard);

        /* Self-Descriptive message(Docs-Link), HATEOAS */
        BoardResource boardResource = new BoardResource(getBoard);
        boardResource.add(linkTo(BoardController.class).slash("").withSelfRel());
        boardResource.add(new Link("/docs/index.html#resource-deleteBoard").withRel("profile"));

        return ResponseEntity.ok(boardResource);

    }

    /** 점검 완료 **/
    @Transactional
    @Override
    public ResponseEntity putBoard(@RequestBody BoardPutDto boardPutDto) {
        /* Get Data connected User */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nowUserId = authentication.getName();

        /* Get and Exceptions */
        Board getBoard = this.boardRepository.findById(boardPutDto.getId()).orElseThrow(()-> new IllegalArgumentException("해당 값에 대한 게시물이 없습니다."));
        if(!getBoard.getAuthor().getId().equals(nowUserId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(!getBoard.getPassword().equals(boardPutDto.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        getBoard.setTitle(boardPutDto.getTitle());
        getBoard.setContent(boardPutDto.getContent());

        Board resultBoard = this.boardRepository.save(getBoard);

        /* Self-Descriptive message(Docs-Link), HATEOAS */
        BoardResource boardResource = new BoardResource(resultBoard);
        boardResource.add(linkTo(BoardController.class).slash("").withSelfRel());
        boardResource.add(new Link("/docs/index.html#resource-putBoard").withRel("profile"));

        return ResponseEntity.ok(boardResource);
    }

    /** 점검 완료 **/
    @Transactional
    @Override
    public ResponseEntity saveTestBoardContent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nowUserId = authentication.getName();
        Author getAuthor = this.authorRepository.findById(nowUserId)
                .orElseThrow(() -> new UsernameNotFoundException("계정 정보가 잘못됨"));

        IntStream.rangeClosed(1, 40).forEach(index -> {
            Board testBoard = Board.builder()
                    .author(getAuthor)
                    .title("테스트 제목"+index)
                    .content("테스트 내용")
                    .password("1234abcd")
                    .build();

            this.boardRepository.save(testBoard);


        });

        return ResponseEntity.ok("40개의 테스트 게시물이 생성되었습니다.");
    }
}
