package com.community.weddingbook.Board;

import org.springframework.hateoas.MediaTypes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/board", produces = MediaTypes.HAL_JSON_VALUE)
public class BoardController {

}
