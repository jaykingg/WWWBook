package com.community.weddingbook.Board;

import com.community.weddingbook.Author.Author;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Board")
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull @Size(max = 20)
    String title;

    @Size(max = 20)
    String content;

    @ManyToOne(targetEntity = Author.class, fetch = FetchType.EAGER) @NotNull
    Author author;

    @Pattern(regexp="[a-zA-Z1-9]{6,20}", message = "비밀번호는 영어와 숫자로 포함해서 6~12자리 이내로 입력해주세요.")
    String password;

}
