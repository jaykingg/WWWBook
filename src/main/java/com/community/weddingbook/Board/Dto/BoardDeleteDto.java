package com.community.weddingbook.Board.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardDeleteDto {

    Integer id;
    String password;
}
