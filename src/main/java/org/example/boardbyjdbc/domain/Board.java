package org.example.boardbyjdbc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("board")
@Builder
public class Board {
    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("title")
    private String title;

    @Column("password")
    private String password;

    @Column("content")
    private String content;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 입력된 비밀번호가 게시글 비밀번호와 일치하는지 확인합니다.
     * @param inputPassword 사용자가 입력한 비밀번호
     * @return true면 일치, false면 불일치
     */
    public boolean isPasswordCorrect(String inputPassword) {
        return inputPassword != null && inputPassword.equals(this.password);
    }

    /**
     * 게시글 수정 메서드
     * - name, title, content를 새 값으로 교체
     * - updatedAt을 현재 시간으로 갱신
     */
    public void update(String name, String title, String content) {
        this.name = name;
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now(); // 수정 시점 자동 반영
    }
}
