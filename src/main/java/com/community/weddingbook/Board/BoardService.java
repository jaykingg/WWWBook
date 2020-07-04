package com.community.weddingbook.Board;

import com.community.weddingbook.Board.Dto.BoardDeleteDto;
import com.community.weddingbook.Board.Dto.BoardDto;
import com.community.weddingbook.Board.Dto.BoardPutDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface BoardService {
    ResponseEntity saveBoard(@RequestBody BoardDto boardDto);
    ResponseEntity getBoardList(@PageableDefault Pageable pageable, PagedResourcesAssembler<Board> assembler);
    ResponseEntity getBoard(@PathVariable Integer id);
    ResponseEntity deleteBoard(@RequestBody BoardDeleteDto boardDeleteDto);
    ResponseEntity putBoard(@RequestBody BoardPutDto boardPutDto);
    ResponseEntity saveTestBoardContent();


}
