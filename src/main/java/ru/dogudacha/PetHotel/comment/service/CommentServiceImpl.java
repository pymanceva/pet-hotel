package ru.dogudacha.PetHotel.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.comment.dto.CommentDto;
import ru.dogudacha.PetHotel.comment.dto.NewCommentDto;
import ru.dogudacha.PetHotel.comment.dto.UpdateCommentDto;
import ru.dogudacha.PetHotel.comment.mapper.CommentMapper;
import ru.dogudacha.PetHotel.comment.model.Comment;
import ru.dogudacha.PetHotel.comment.repository.CommentRepository;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.pet.repository.PetRepository;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentDto createComment(Long requesterId, NewCommentDto newCommentDto, Long petId) {
        User user = findUserById(requesterId);
        Pet pet = findPetById(petId);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setPet(pet);
        comment.setCreated(LocalDateTime.now());
        comment.setText(newCommentDto.getText());
        Comment sevedComment = commentRepository.save(comment);
        return commentMapper.toCommentDto(sevedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsByPetId(Long requesterId, Long petId) {
        findUserById(requesterId);
        findPetById(petId);
        List<Comment> comments = commentRepository.findByPetId(petId);
        return comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long requesterId, Long commentId) {
        findUserById(requesterId);
        Comment comment = getCommentIfExists(commentId);
        return commentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long requesterId, Long commentId, UpdateCommentDto updateCommentDto) {
        findUserById(requesterId);
        Comment comment = getCommentIfExists(commentId);
        if (!comment.getAuthor().getId().equals(requesterId)) {
            throw new AccessDeniedException("Only the author of the comment can change it.");
        }
        if (Objects.nonNull(updateCommentDto.getText()) && !updateCommentDto.getText().isBlank()) {
            comment.setText(updateCommentDto.getText());
        }

        return commentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long requesterId, Long commentId) {
        User user = findUserById(requesterId);
        Comment comment = getCommentIfExists(commentId);
        if (comment.getAuthor().getId().equals(user.getId()) || user.getRole().ordinal() == 0) {
            commentRepository.deleteById(commentId);
        } else {
            throw new AccessDeniedException("Only the author of the comment or boss can delete it.");
        }
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id = %d not found", userId)));
    }


    private Pet findPetById(long petId) {
        return petRepository.findById(petId).orElseThrow(() ->
                new NotFoundException(String.format("Pet with id = %d not found", petId)));
    }

    private Comment getCommentIfExists(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Comment with id = %d not found", commentId)));
    }


}
