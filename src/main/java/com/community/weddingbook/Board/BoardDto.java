package com.community.weddingbook.Board;

import com.community.weddingbook.Author.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull @Size(max = 20)
    String title;

    @Size(max = 20)
    String content;

    @Pattern(regexp="[a-zA-Z1-9]{6,20}", message = "비밀번호는 영어와 숫자로 포함해서 6~12자리 이내로 입력해주세요.")
    String password;
}
