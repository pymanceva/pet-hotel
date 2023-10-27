package ru.dogudacha.PetHotel.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dogudacha.PetHotel.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthorId(Long userId);
}

