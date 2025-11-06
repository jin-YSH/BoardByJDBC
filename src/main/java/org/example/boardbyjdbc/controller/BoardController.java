package org.example.boardbyjdbc.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.boardbyjdbc.dto.BoardRequestDto;
import org.example.boardbyjdbc.dto.BoardResponseDto;
import org.example.boardbyjdbc.exception.PasswordMismatchException;
import org.example.boardbyjdbc.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    final BoardService boardService;

    @GetMapping("/list")
    public String list(Model model) {
        List<BoardResponseDto> boardlist = boardService.getBoardList();
        model.addAttribute("boardlist", boardlist);
        return "board/list";
    }

    @GetMapping("/view")
    public String view(Model model, @RequestParam("id") Long id) {
        BoardResponseDto searchBoard = boardService.getBoard(id);
        model.addAttribute("board", searchBoard);
        return "board/view";
    }

    /**
     * 글 작성 폼
     */
    @GetMapping("/writeform")
    public String writeForm(Model model) {
        model.addAttribute("board", new BoardRequestDto()); // 폼용 DTO는 board
        return "board/writeform";
    }

    @PostMapping("/write")
    public String write(@Valid BoardRequestDto board,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("board", board); // 폼 유지
            model.addAttribute("errorMessage", "입력하신 내용을 다시 확인해주세요."); // ✅ 공통 에러 메시지 추가
            return "board/writeform";
        }

        Long id = boardService.createBoard(board);
        redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 등록되었습니다!");
        return "redirect:/view?id=" + id;
    }

    /**
     * 글 수정 폼
     */
    @GetMapping("/updateform")
    public String updateForm(Model model, @RequestParam("id") Long id) {
        BoardResponseDto searchBoardDTO = boardService.getBoard(id);

        // ResponseDTO → RequestDTO 변환
        BoardRequestDto boardRequestDTO = new BoardRequestDto();
        boardRequestDTO.setName(searchBoardDTO.getName());
        boardRequestDTO.setTitle(searchBoardDTO.getTitle());
        boardRequestDTO.setContent(searchBoardDTO.getContent());
        // password는 입력받아야 하므로 미리 채우지 않음

        model.addAttribute("board", boardRequestDTO); // 폼용 DTO는 board
        model.addAttribute("boardId", id);            // 폼과 관련 없는 값은 boardId 유지
        return "board/updateform";
    }

    @PostMapping("/update")
    public String update(
            @Valid BoardRequestDto board,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestParam("id") Long id,
            Model model) {

        // 1️⃣ 유효성 검사 실패 시
        if (bindingResult.hasErrors()) {
            model.addAttribute("boardId", id);
            model.addAttribute("board", board);
            model.addAttribute("errorMessage", "입력하신 내용을 다시 확인해주세요.");
            return "board/updateform";
        }


        try {
            boardService.updateBoard(id, board);
            redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 수정되었습니다!");
            return "redirect:/view?id=" + id;

        } catch (PasswordMismatchException e) {
            model.addAttribute("boardId", id);
            model.addAttribute("board", board);
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "board/updateform";

        } catch (Exception e) {
            model.addAttribute("boardId", id);
            model.addAttribute("board", board);
            model.addAttribute("errorMessage", "수정 중 오류가 발생했습니다.");
            return "board/updateform";
        }
    }


    /**
     * 글 삭제 폼
     */
    @GetMapping("/deleteform")
    public String deleteForm(Model model, @RequestParam("id") Long id) {
        model.addAttribute("boardId", id); // 삭제 폼에는 폼용 DTO 필요 없음
        return "board/deleteform";
    }

    @PostMapping("/delete")
    public String delete(RedirectAttributes redirectAttributes,
                         @RequestParam("id") Long id,
                         @RequestParam("password") String password,
                         Model model){
        try{
            boardService.deleteBoard(id, password);
            redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 삭제되었습니다!");
            return "redirect:/list";
        }catch(PasswordMismatchException e){
            model.addAttribute("boardId", id);
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "board/deleteform";
        }catch(Exception e){
            model.addAttribute("boardId", id);
            model.addAttribute("errorMessage", "삭제 중 오류가 발생했습니다.");
            return "board/deleteform";
        }
    }
}