package com.community.weddingbook.Author;


import org.springframework.http.ResponseEntity;

public interface AuthorService {
    Author saveAuthor(Author account);

    ResponseEntity getTestID();
}
