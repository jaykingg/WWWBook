package com.community.weddingbook.Board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findAllByOrderByCreatedAtDesc();
}
