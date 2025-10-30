package org.example.boardbyjdbc.dto;

import lombok.Builder;
import lombok.Data;
import org.example.boardbyjdbc.domain.Board;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class BoardResponseDto {
    private String id;
    private String name;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;

    /** 목록용 DTO 변환 (날짜: yyyy/MM/dd) */
    public static BoardResponseDto from(Board board) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return BoardResponseDto.builder()
                .id(String.valueOf(board.getId()))
                .name(board.getName())
                .title(board.getTitle())
                .content(board.getContent())
                .createdAt(board.getCreatedAt() != null ? board.getCreatedAt().format(dtf) : null)
                .updatedAt(board.getUpdatedAt() != null ? board.getUpdatedAt().format(dtf) : null)
                .build();
    }

    /** 상세보기용 DTO 변환 (날짜: yyyy/MM/dd HH:mm) */
    public static BoardResponseDto fromDetail(Board board) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return BoardResponseDto.builder()
                .id(String.valueOf(board.getId()))
                .name(board.getName())
                .title(board.getTitle())
                .content(board.getContent())
                .createdAt(board.getCreatedAt() != null ? board.getCreatedAt().format(dtf) : null)
                .updatedAt(board.getUpdatedAt() != null ? board.getUpdatedAt().format(dtf) : null)
                .build();
    }
}