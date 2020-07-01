package com.community.weddingbook.Board;

import org.springframework.http.ResponseEntity;

public interface BoardService {
    ResponseEntity saveBoard(BoardDto boardDto);
    ResponseEntity getBoardList();
    ResponseEntity getBoard(Integer id);
    ResponseEntity deleteBoard(Integer id);
    ResponseEntity putBoard(BoardDto boardDto);


}
