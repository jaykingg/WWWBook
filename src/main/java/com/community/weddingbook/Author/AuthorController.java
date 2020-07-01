package com.community.weddingbook.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/api/author", produces = MediaTypes.HAL_JSON_VALUE)
public class AuthorController {

    @Autowired
    AuthorServiceImpl authorServiceImpl;

    @GetMapping
    public ResponseEntity getAuthorID() {
        return authorServiceImpl.getTestID();
    }
}
