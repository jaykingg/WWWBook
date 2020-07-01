package com.community.weddingbook.Author;

import com.community.weddingbook.Board.Board;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Author")
public class Author {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    @NotNull @Size(max = 12)
    String name;

    @NotNull @Size(max = 12)
    String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AuthorRole> roles;

    String ServiceAccessToken;
    String ServiceRefreshToken;

    @OneToMany(targetEntity = Board.class, fetch = FetchType.LAZY, mappedBy = "author", cascade = CascadeType.ALL)
    List<Board> boards;
}
