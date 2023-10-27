package ru.dogudacha.PetHotel.comment.service;

import ru.dogudacha.PetHotel.comment.dto.CommentDto;
import ru.dogudacha.PetHotel.comment.dto.NewCommentDto;
import ru.dogudacha.PetHotel.comment.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long requesterId, NewCommentDto newCommentDto);

    List<CommentDto> getAllCommentsByUserId(Long userId);

    CommentDto getCommentById(Long userId, Long commentId);

    CommentDto updateComment(Long requesterId, Long commentId, UpdateCommentDto updateCommentDto);

    void deleteComment(Long userId, Long commentId);
}