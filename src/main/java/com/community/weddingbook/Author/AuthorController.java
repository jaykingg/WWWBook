package com.community.weddingbook.Author;

import org.springframework.hateoas.MediaTypes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/author", produces = MediaTypes.HAL_JSON_VALUE)
public class AuthorController {
}
