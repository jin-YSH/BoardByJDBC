package org.example.boardbyjdbc.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BoardRequestDto {
    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    @Size(min = 2, max = 100, message = "제목은 2자 이상 100자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    @Size(min = 5, max = 1000, message = "내용은 5자 이상 1000자 이하로 입력해주세요.")
    private String content;
}