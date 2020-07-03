package com.community.weddingbook.Board;

import com.community.weddingbook.Board.Dto.BoardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/board", produces = MediaTypes.HAL_JSON_VALUE)
public class BoardController {

    @Autowired
    BoardServiceImpl boardServiceImpl;

    @PostMapping
    public ResponseEntity saveBoard(@RequestBody BoardDto boardDto) throws Exception {
        return boardServiceImpl.saveBoard(boardDto);
    }


}
