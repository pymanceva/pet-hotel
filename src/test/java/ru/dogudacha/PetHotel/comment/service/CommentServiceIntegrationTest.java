package ru.dogudacha.PetHotel.comment.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.comment.dto.CommentDto;
import ru.dogudacha.PetHotel.comment.dto.NewCommentDto;
import ru.dogudacha.PetHotel.comment.dto.UpdateCommentDto;
import ru.dogudacha.PetHotel.comment.model.Comment;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.pet.model.Sex;
import ru.dogudacha.PetHotel.pet.model.TypeOfDiet;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class CommentServiceIntegrationTest {
    private final EntityManager em;
    private final CommentService service;

    final User requesterAdmin = User.builder()
            .email("admin@mail.ru")
            .name("admin")
            .role(Roles.ROLE_ADMIN)
            .build();

    final Pet pet = Pet.builder()
            .typeOfPet("Dog")
            .breed("Spaniel")
            .sex(Sex.FEMALE)
            .age(2)
            .weight(7)
            .diet(TypeOfDiet.READY_INDUSTRIAL_FOOD)
            .isTakesMedications(false)
            .isContact(true)
            .isPhotographed(true)
            .build();

    final NewCommentDto newCommentDto = NewCommentDto.builder()
            .text("Very good dog")
            .build();

    final Comment comment = Comment.builder()
            .text("Very good dog")
            .pet(pet)
            .author(requesterAdmin)
            .build();

    final Comment comment2 = Comment.builder()
            .text("Play with ball")
            .pet(pet)
            .author(requesterAdmin)
            .build();

    final UpdateCommentDto updateCommentDto = UpdateCommentDto.builder()
            .text("Very very good dog")
            .build();

    @Test
    void createComment() {
        em.persist(requesterAdmin);
        em.persist(pet);

        CommentDto actualComment = service.createComment(requesterAdmin.getId(), newCommentDto, pet.getId());

        assertThat(actualComment.getId(), notNullValue());
        assertThat(actualComment.getText(), equalTo(newCommentDto.getText()));
        assertThat(actualComment.getPet().getId(), equalTo(pet.getId()));
        assertThat(actualComment.getAuthor().getId(), equalTo(requesterAdmin.getId()));
        assertThat(actualComment.getCreated(), notNullValue());
    }

    @Test
    void updateComment() {
        em.persist(requesterAdmin);
        em.persist(pet);
        em.persist(comment);

        CommentDto actualComment = service.updateComment(requesterAdmin.getId(), comment.getId(), updateCommentDto);

        assertThat(actualComment.getId(), notNullValue());
        assertThat(actualComment.getText(), equalTo(updateCommentDto.getText()));
        assertThat(actualComment.getPet().getId(), equalTo(pet.getId()));
        assertThat(actualComment.getAuthor().getId(), equalTo(requesterAdmin.getId()));
        assertThat(actualComment.getCreated(), notNullValue());
    }

    @Test
    void getCommentById() {
        em.persist(requesterAdmin);
        em.persist(pet);
        em.persist(comment);

        CommentDto actualComment = service.getCommentById(requesterAdmin.getId(), comment.getId());

        assertThat(actualComment.getId(), notNullValue());
        assertThat(actualComment.getText(), equalTo(comment.getText()));
        assertThat(actualComment.getPet().getId(), equalTo(pet.getId()));
        assertThat(actualComment.getAuthor().getId(), equalTo(requesterAdmin.getId()));
        assertThat(actualComment.getCreated(), notNullValue());
    }

    @Test
    void getComments() {
        em.persist(requesterAdmin);
        em.persist(pet);
        em.persist(comment);
        em.persist(comment2);

        List<CommentDto> actualComments = service.getAllCommentsByPetId(requesterAdmin.getId(), pet.getId());

        assertThat(actualComments, hasSize(2));
        assertThat(actualComments.get(0).getId(), notNullValue());
        assertThat(actualComments.get(0).getText(), equalTo(comment.getText()));
        assertThat(actualComments.get(0).getPet().getId(), equalTo(pet.getId()));
        assertThat(actualComments.get(0).getAuthor().getId(), equalTo(requesterAdmin.getId()));
        assertThat(actualComments.get(0).getCreated(), notNullValue());

        assertThat(actualComments.get(1).getId(), notNullValue());
        assertThat(actualComments.get(1).getText(), equalTo(comment2.getText()));
        assertThat(actualComments.get(1).getPet().getId(), equalTo(pet.getId()));
        assertThat(actualComments.get(1).getAuthor().getId(), equalTo(requesterAdmin.getId()));
        assertThat(actualComments.get(1).getCreated(), notNullValue());
    }

    @Test
    void deleteCommentById() {
        em.persist(requesterAdmin);
        em.persist(pet);
        em.persist(comment);

        service.deleteComment(requesterAdmin.getId(), comment.getId());

        String error = String.format("Comment with id = %d not found", comment.getId());
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.getCommentById(requesterAdmin.getId(), comment.getId())
        );

        assertEquals(error, exception.getMessage());
    }
}
