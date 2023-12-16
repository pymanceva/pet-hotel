package ru.dogudacha.PetHotel.comment.service;

import ru.dogudacha.PetHotel.comment.dto.CommentDto;
import ru.dogudacha.PetHotel.comment.dto.NewCommentDto;
import ru.dogudacha.PetHotel.comment.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long requesterId, NewCommentDto newCommentDto, Long petId);

    List<CommentDto> getAllCommentsByPetId(Long requesterId, Long petId);

    CommentDto getCommentById(Long requesterId, Long commentId);

    CommentDto updateComment(Long requesterId, Long commentId, UpdateCommentDto updateCommentDto);

    void deleteComment(Long requesterId, Long commentId);
}