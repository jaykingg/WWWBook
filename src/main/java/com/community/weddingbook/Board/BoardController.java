package com.community.weddingbook.Board;

import com.community.weddingbook.Board.Dto.BoardDeleteDto;
import com.community.weddingbook.Board.Dto.BoardDto;
import com.community.weddingbook.Board.Dto.BoardPutDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/board", produces = MediaTypes.HAL_JSON_VALUE)
public class BoardController {

    @Autowired
    BoardServiceImpl boardServiceImpl;

    @PostMapping
    public ResponseEntity saveBoard(@RequestBody BoardDto boardDto, Errors errors) throws Exception {
        return boardServiceImpl.saveBoard(boardDto);
    }

    @GetMapping("/list")
    public ResponseEntity getBoardList(@PageableDefault Pageable pageable, PagedResourcesAssembler<Board> assembler) {
        return boardServiceImpl.getBoardList(pageable,assembler);
    }

    @GetMapping("/{id}")
    public ResponseEntity getBoard(@PathVariable Integer id) {
        return boardServiceImpl.getBoard(id);
    }

    @DeleteMapping
    public ResponseEntity deleteBoard(@RequestBody BoardDeleteDto boardDeleteDto) {
        return boardServiceImpl.deleteBoard(boardDeleteDto);
    }

    @PutMapping
    public ResponseEntity putBoard(@RequestBody BoardPutDto boardPutDto) {
        return boardServiceImpl.putBoard(boardPutDto);
    }

    @PostMapping("/makeCases")
    public ResponseEntity saveTestBoardContent() {
        return boardServiceImpl.saveTestBoardContent();
    }



}
