package com.community.weddingbook.Author;

import com.community.weddingbook.Board.Board;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Author")
@JsonSerialize(using = AuthorSerializer.class)
public class Author {

    @Id
    String id;

    @NotNull
    String name;

    @NotNull
    String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AuthorRole> roles;

    String ServiceAccessToken;
    String ServiceRefreshToken;

}
