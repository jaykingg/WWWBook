package com.community.weddingbook.Board;

import com.community.weddingbook.Author.Author;
import com.community.weddingbook.Common.BaseTimeEntity;
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
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Size(min = 1, max = 20, message = "제목은 1~20자리 이내로 입력해주세요.") @NotNull
    String title;

    @Size(max = 200, message = "내용은 200자리 이내로 입력해주세요.") @NotNull
    String content;

    @ManyToOne(fetch = FetchType.EAGER) @NotNull
    Author author;

    @Size(min=6)
    @Pattern(regexp="(?=.*[a-zA-Z])(?=.*\\d).{6,12}", message = "비밀번호는 영어와 숫자로 포함해서 6~12자리 이내로 입력해주세요.")
    String password;

}
