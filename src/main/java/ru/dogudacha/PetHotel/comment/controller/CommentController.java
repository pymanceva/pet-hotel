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


@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentController {
    private static final String USER_ID = "X-PetHotel-User-Id";
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestHeader(USER_ID) Long requesterId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("Получен POST-запрос к эндпоинту: '/users/{userId}/comments' на добавление комментария " +
                "пользователем с ID={}", requesterId);
        return commentService.createComment(requesterId, newCommentDto);
    }

    @GetMapping
    public List<CommentDto> getAllCommentsByUserId(@RequestHeader(USER_ID) Long requesterId) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{userId}/comments' на получение всех комментариев " +
                "пользователя с ID={}", requesterId);
        return commentService.getAllCommentsByUserId(requesterId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@RequestHeader(USER_ID) Long requesterId,
                                     @PathVariable Long commentId) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{userId}/comments/{commentId}' на получение " +
                "комментария с ID={}", commentId);
        return commentService.getCommentById(requesterId, commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@RequestHeader(USER_ID) Long requesterId,
                                    @PathVariable Long commentId,
                                    @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        log.info("Получен PATCH-запрос к эндпоинту: '/users/{userId}/comments/{commentId}/events/{eventId}' на изменение комментария " +
                "с ID={} пользователем с ID={}", commentId, requesterId);
        return commentService.updateComment(requesterId, commentId, updateCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@RequestHeader(USER_ID) Long requesterId,
                              @PathVariable Long commentId) {
        log.info("Получен DELETE-запрос к эндпоинту: '/users/{userId}/comments/{commentId}' на удаление комментария " +
                "с ID={} пользователем с ID={}", commentId, requesterId);
        commentService.deleteComment(requesterId, commentId);
    }
}
