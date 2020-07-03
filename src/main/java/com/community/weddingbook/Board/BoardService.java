package com.community.weddingbook.Board;

import com.community.weddingbook.Board.Dto.BoardDeleteDto;
import com.community.weddingbook.Board.Dto.BoardDto;
import com.community.weddingbook.Board.Dto.BoardPutDto;
import org.springframework.http.ResponseEntity;

public interface BoardService {
    ResponseEntity saveBoard(BoardDto boardDto);
    ResponseEntity getBoardList();
    ResponseEntity getBoard(Integer id);
    ResponseEntity deleteBoard(BoardDeleteDto boardDeleteDto);
    ResponseEntity putBoard(BoardPutDto boardPutDto);


}
