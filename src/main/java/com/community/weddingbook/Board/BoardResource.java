package com.community.weddingbook.Board;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class BoardResource extends Resource<Board> {
    public BoardResource(Board content, Link... links) {
        super(content, links);
        /* Self-Descriptive message(Docs-Link), HATEOAS */
        add(linkTo(BoardController.class).slash("").withRel("[POST]게시물 추가"));
        add(linkTo(BoardController.class).slash("list").withRel("[GET]게시물 전체 조회"));
        add(linkTo(BoardController.class).slash("").withRel("[GET]게시물 상세 조회"));
        add(linkTo(BoardController.class).slash("").withRel("[PUT]게시물 수정"));
        add(linkTo(BoardController.class).slash("").withRel("[DELETE]게시물 삭제"));

    }
}
