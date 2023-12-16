package ru.dogudacha.PetHotel.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dogudacha.PetHotel.comment.dto.CommentDto;
import ru.dogudacha.PetHotel.comment.dto.NewCommentDto;
import ru.dogudacha.PetHotel.comment.dto.UpdateCommentDto;
import ru.dogudacha.PetHotel.comment.service.CommentService;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentController {
    private static final String USER_ID = "X-PetHotel-User-Id";
    private final CommentService commentService;

    @PostMapping("/pets/{petId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@RequestHeader(USER_ID) Long requesterId,
                                 @RequestBody @Valid NewCommentDto newCommentDto,
                                 @PathVariable Long petId) {
        log.info("CommentController: POST/addComment, requesterId={}", requesterId);
        return commentService.createComment(requesterId, newCommentDto, petId);
    }

    @GetMapping("/pets/{petId}")
    public List<CommentDto> getAllCommentsByPetId(@RequestHeader(USER_ID) Long requesterId,
                                                  @PathVariable Long petId) {
        log.info("CommentController: GET/getCommentsByPetId, requesterId={}, petId={}", requesterId, petId);
        return commentService.getAllCommentsByPetId(requesterId, petId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@RequestHeader(USER_ID) Long requesterId,
                                     @PathVariable Long commentId) {
        log.info("CommentController: GET/getCommentById, requesterId={}, commentId={}", requesterId, commentId);
        return commentService.getCommentById(requesterId, commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@RequestHeader(USER_ID) Long requesterId,
                                    @RequestBody @Valid UpdateCommentDto updateCommentDto,
                                    @PathVariable Long commentId) {
        log.info("PetController: PATCH/updateComment, requesterId={}, commentId={}", requesterId, commentId);
        return commentService.updateComment(requesterId, commentId, updateCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@RequestHeader(USER_ID) Long requesterId,
                              @PathVariable Long commentId) {
        log.info("CommentController: DELETE/deleteComment, requesterId= {}, commentId={}", requesterId, commentId);
        commentService.deleteComment(requesterId, commentId);
    }
}
