package com.community.weddingbook.Board;

import com.community.weddingbook.Author.Author;
import com.community.weddingbook.Author.AuthorRepository;
import com.community.weddingbook.Board.Dto.BoardDeleteDto;
import com.community.weddingbook.Board.Dto.BoardDto;
import com.community.weddingbook.Board.Dto.BoardPutDto;
import com.community.weddingbook.Configs.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

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

    @Transactional
    @Override
    public ResponseEntity getBoardList() {

        BoardResource boardResource = new BoardResource(getBoard);
        boardResource.add(linkTo(BoardController.class).slash("list").withSelfRel());
        boardResource.add(new Link("/docs/index.html#resource-getBoard").withRel("profile"));
    }

    @Transactional
    @Override
    public ResponseEntity getBoard(Integer id) {
        /* Get Board */
        Board getBoard = this.boardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 값에 대한 게시물이 없습니다."));

        /* Self-Descriptive message(Docs-Link), HATEOAS */
        BoardResource boardResource = new BoardResource(getBoard);
        boardResource.add(linkTo(BoardController.class).slash("").withSelfRel());
        boardResource.add(new Link("/docs/index.html#resource-getBoard").withRel("profile"));

        /* Result */
        return ResponseEntity.ok(boardResource);

    }

    @Transactional
    @Override
    public ResponseEntity deleteBoard(BoardDeleteDto boardDeleteDto) {
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

        this.boardDeletedRepository.save(getBoard);
        this.boardRepository.delete(getBoard);

        /* Self-Descriptive message(Docs-Link), HATEOAS */
        BoardResource boardResource = new BoardResource(getBoard);
        boardResource.add(linkTo(BoardController.class).slash("").withSelfRel());
        boardResource.add(new Link("/docs/index.html#resource-deleteBoard").withRel("profile"));

        return ResponseEntity.ok(boardResource);

    }

    @Transactional
    @Override
    public ResponseEntity putBoard(BoardPutDto boardPutDto) {
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


}
