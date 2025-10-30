package org.example.boardbyjdbc.repository;

import org.example.boardbyjdbc.domain.Board;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {

    // 제목으로 검색
    List<Board> findByTitleContaining(String keyword);

    // 작성자로 검색
    List<Board> findByNameContaining(String name);
}
