package org.example.boardbyjdbc.service;



import lombok.RequiredArgsConstructor;
import org.example.boardbyjdbc.domain.Board;
import org.example.boardbyjdbc.dto.BoardRequestDto;
import org.example.boardbyjdbc.dto.BoardResponseDto;
import org.example.boardbyjdbc.exception.BoardNotFoundException;
import org.example.boardbyjdbc.exception.PasswordMismatchException;
import org.example.boardbyjdbc.repository.BoardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    @Autowired
    final private BoardRepository boardRepository;

    public List<BoardResponseDto> getBoardList(){
        Iterable<Board> boards = boardRepository.findAll();
        List<BoardResponseDto> boardResponseDTOlist = StreamSupport.stream(boards.spliterator(),false)
                .sorted(Comparator.comparing(Board::getId).reversed())
                .map(BoardResponseDto::from)
                .toList();

        return boardResponseDTOlist;
    }

    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException("입력한 ID와 일치하는 게시글이 존재하지 않습니다."));

        return BoardResponseDto.fromDetail(board);
    }
    @Transactional(readOnly = false)
    public Long createBoard(BoardRequestDto dto){
        Board board = new Board(null, dto.getName(), dto.getTitle(), dto.getPassword(),
                dto.getContent(), LocalDateTime.now(), LocalDateTime.now());
        return boardRepository.save(board).getId();
    }

    @Transactional(readOnly = false)
    public void updateBoard(Long id, BoardRequestDto dto){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException("입력한 ID와 일치하는 게시글이 존재하지 않습니다."));

        if (!board.isPasswordCorrect(dto.getPassword())) {
            throw new PasswordMismatchException("입력한 비밀번호가 일치하지 않습니다.");
        }

        board.update(dto.getName(), dto.getTitle(), dto.getContent());

        boardRepository.save(board);
    }

    public void deleteBoard(Long id, String password){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException("입력한 ID와 일치하는 게시글이 존재하지 않습니다."));

        if (!board.isPasswordCorrect(password)) {
            throw new PasswordMismatchException("입력한 비밀번호가 일치하지 않습니다.");
        }

        boardRepository.deleteById(id);
    }
}
