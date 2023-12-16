package ru.dogudacha.PetHotel.comment.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.dogudacha.PetHotel.comment.dto.CommentDto;
import ru.dogudacha.PetHotel.comment.dto.NewCommentDto;
import ru.dogudacha.PetHotel.comment.dto.UpdateCommentDto;
import ru.dogudacha.PetHotel.comment.mapper.CommentMapper;
import ru.dogudacha.PetHotel.comment.model.Comment;
import ru.dogudacha.PetHotel.comment.repository.CommentRepository;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.pet.model.Sex;
import ru.dogudacha.PetHotel.pet.model.TypeOfDiet;
import ru.dogudacha.PetHotel.pet.repository.PetRepository;
import ru.dogudacha.PetHotel.user.dto.UserDto;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentServiceImpTest {
    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PetRepository mockPetRepository;

    @Mock
    private CommentRepository mockCommentRepository;

    @Mock
    private CommentMapper mockCommentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    final User requesterBoss = User.builder()
            .email("boss@mail.ru")
            .name("boss")
            .id(1L)
            .role(Roles.ROLE_BOSS)
            .build();

    final UserDto requesterBossDto = UserDto.builder()
            .id(1L)
            .email("boss@mail.ru")
            .name("boss")
            .id(1L)
            .role(Roles.ROLE_BOSS)
            .build();

    final User requesterAdmin = User.builder()
            .email("admin@mail.ru")
            .name("admin")
            .id(2L)
            .role(Roles.ROLE_ADMIN)
            .build();

    final UserDto requesterAdminDto = UserDto.builder()
            .id(1L)
            .email("admin@mail.ru")
            .name("admin")
            .id(2L)
            .role(Roles.ROLE_ADMIN)
            .build();

    final User requesterUser = User.builder()
            .email("user@mail.ru")
            .name("user")
            .id(3L)
            .role(Roles.ROLE_USER)
            .build();

    final UserDto requesterUserDto = UserDto.builder()
            .id(1L)
            .email("user@mail.ru")
            .name("user")
            .id(3L)
            .role(Roles.ROLE_USER)
            .build();

    final PetDto petDto = PetDto.builder()
            .id(1L)
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

    final Pet pet = Pet.builder()
            .id(1L)
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

    final Comment comment = Comment.builder()
            .id(1L)
            .text("Very good dog")
            .pet(pet)
            .author(requesterAdmin)
            .created(LocalDateTime.now())
            .build();

    final Comment comment2 = Comment.builder()
            .id(2L)
            .text("Very sleepy")
            .pet(pet)
            .author(requesterAdmin)
            .created(LocalDateTime.now())
            .build();

    final CommentDto commentDtoForAdmin = CommentDto.builder()
            .id(1L)
            .text("Very good dog")
            .pet(petDto)
            .author(requesterAdminDto)
            .created(comment.getCreated())
            .build();

    final CommentDto commentDtoForBoss = CommentDto.builder()
            .id(1L)
            .text("Very good dog")
            .pet(petDto)
            .author(requesterBossDto)
            .created(comment.getCreated())
            .build();

    final CommentDto commentDtoForBoss2 = CommentDto.builder()
            .id(2L)
            .text("Very sleepy")
            .pet(petDto)
            .author(requesterBossDto)
            .created(comment2.getCreated())
            .build();

    final CommentDto commentDtoForUser = CommentDto.builder()
            .id(1L)
            .text("Very good dog")
            .pet(petDto)
            .author(requesterUserDto)
            .created(comment.getCreated())
            .build();

    final NewCommentDto newCommentDto = NewCommentDto.builder()
            .text("Very good dog")
            .build();

    final UpdateCommentDto updateCommentDto = UpdateCommentDto.builder()
            .text("Very very good dog")
            .build();

    final CommentDto updatedCommentDto = CommentDto.builder()
            .id(1L)
            .text("Very very good dog")
            .pet(petDto)
            .author(requesterAdminDto)
            .created(comment.getCreated())
            .build();

    @Test
    void addComment_whenAddCommentByAdmin_thenCommentAdded() {
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockPetRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        when(mockCommentRepository.save(any())).thenReturn(comment);
        when(mockCommentMapper.toCommentDto(comment)).thenReturn(commentDtoForAdmin);

        CommentDto actualCommentDto = commentService.createComment(requesterAdmin.getId(), newCommentDto, pet.getId());

        assertNotNull(actualCommentDto);
        assertThat(actualCommentDto.getId(), equalTo(comment.getId()));
        assertThat(actualCommentDto.getText(), equalTo(comment.getText()));
        assertThat(actualCommentDto.getPet().getId(), equalTo(pet.getId()));
        assertThat(actualCommentDto.getAuthor().getId(), equalTo(requesterAdmin.getId()));
        assertThat(actualCommentDto.getCreated(), equalTo(comment.getCreated()));
        verify(mockCommentRepository, times(1)).save(any());
    }

    @Test
    void addComment_whenAddCommentByBoss_thenCommentAdded() {
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        when(mockCommentRepository.save(any())).thenReturn(comment);
        when(mockCommentMapper.toCommentDto(comment)).thenReturn(commentDtoForBoss);

        CommentDto actualCommentDto = commentService.createComment(requesterBoss.getId(), newCommentDto, pet.getId());

        assertNotNull(actualCommentDto);
        assertThat(actualCommentDto.getId(), equalTo(comment.getId()));
        assertThat(actualCommentDto.getText(), equalTo(comment.getText()));
        assertThat(actualCommentDto.getPet().getId(), equalTo(pet.getId()));
        assertThat(actualCommentDto.getAuthor().getId(), equalTo(requesterBoss.getId()));
        assertThat(actualCommentDto.getCreated(), equalTo(comment.getCreated()));
        verify(mockCommentRepository, times(1)).save(any());
    }

    @Test
    void addComment_whenAddCommentByUser_thenCommentAdded() {
        when(mockUserRepository.findById(requesterUser.getId())).thenReturn(Optional.of(requesterUser));
        when(mockPetRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        when(mockCommentRepository.save(any())).thenReturn(comment);
        when(mockCommentMapper.toCommentDto(comment)).thenReturn(commentDtoForUser);

        CommentDto actualCommentDto = commentService.createComment(requesterUser.getId(), newCommentDto, pet.getId());

        assertNotNull(actualCommentDto);
        assertThat(actualCommentDto.getId(), equalTo(comment.getId()));
        assertThat(actualCommentDto.getText(), equalTo(comment.getText()));
        assertThat(actualCommentDto.getPet().getId(), equalTo(pet.getId()));
        assertThat(actualCommentDto.getAuthor().getId(), equalTo(requesterUser.getId()));
        assertThat(actualCommentDto.getCreated(), equalTo(comment.getCreated()));
        verify(mockCommentRepository, times(1)).save(any());
    }

    @Test
    void addComment_whenUserNotFound_thenNotFoundExceptionThrown() {
        long userNotFoundId = 0L;
        String error = String.format("User with id = %d not found", userNotFoundId);
        when(mockUserRepository.findById(userNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> commentService.createComment(userNotFoundId, newCommentDto, pet.getId())
        );

        assertEquals(error, exception.getMessage());
        verify(mockCommentRepository, times(0)).save(any());
    }

    @Test
    void addComment_whenPetNotFound_thenNotFoundExceptionThrown() {
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        long petNotFoundId = 0L;
        String error = String.format("Pet with id = %d not found", petNotFoundId);
        when(mockPetRepository.findById(petNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> commentService.createComment(requesterAdmin.getId(), newCommentDto, petNotFoundId)
        );

        assertEquals(error, exception.getMessage());
        verify(mockCommentRepository, times(0)).save(any());
    }

    @Test
    void getCommentById_whenGetCommentByBoss_thenReturnCommentDto() {
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        when(mockCommentMapper.toCommentDto(comment)).thenReturn(commentDtoForBoss);
        when(mockCommentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        CommentDto actualCommentDto = commentService.getCommentById(requesterBoss.getId(), comment.getId());

        assertNotNull(actualCommentDto);
        assertThat(actualCommentDto.getId(), equalTo(comment.getId()));
        assertThat(actualCommentDto.getText(), equalTo(comment.getText()));
        assertThat(actualCommentDto.getPet().getId(), equalTo(pet.getId()));
        assertThat(actualCommentDto.getAuthor().getId(), equalTo(requesterBoss.getId()));
        assertThat(actualCommentDto.getCreated(), equalTo(comment.getCreated()));
    }

    @Test
    void getCommentById_whenGetCommentByAdmin_thenReturnCommentDto() {
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        when(mockCommentMapper.toCommentDto(comment)).thenReturn(commentDtoForAdmin);
        when(mockCommentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        CommentDto actualCommentDto = commentService.getCommentById(requesterAdmin.getId(), comment.getId());

        assertNotNull(actualCommentDto);
        assertThat(actualCommentDto.getId(), equalTo(comment.getId()));
        assertThat(actualCommentDto.getText(), equalTo(comment.getText()));
        assertThat(actualCommentDto.getPet().getId(), equalTo(pet.getId()));
        assertThat(actualCommentDto.getAuthor().getId(), equalTo(requesterAdmin.getId()));
        assertThat(actualCommentDto.getCreated(), equalTo(comment.getCreated()));
    }

    @Test
    void getCommentById_whenGetCommentByUser_thenReturnCommentDto() {
        when(mockUserRepository.findById(requesterUser.getId())).thenReturn(Optional.of(requesterUser));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        when(mockCommentMapper.toCommentDto(comment)).thenReturn(commentDtoForUser);
        when(mockCommentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        CommentDto actualCommentDto = commentService.getCommentById(requesterUser.getId(), comment.getId());

        assertNotNull(actualCommentDto);
        assertThat(actualCommentDto.getId(), equalTo(comment.getId()));
        assertThat(actualCommentDto.getText(), equalTo(comment.getText()));
        assertThat(actualCommentDto.getPet().getId(), equalTo(pet.getId()));
        assertThat(actualCommentDto.getAuthor().getId(), equalTo(requesterUser.getId()));
        assertThat(actualCommentDto.getCreated(), equalTo(comment.getCreated()));
    }


    @Test
    void getCommentById_whenUserNotFound_thenNotFoundExceptionThrown() {
        long userNotFoundId = 0L;
        String error = String.format("User with id = %d not found", userNotFoundId);
        when(mockUserRepository.findById(userNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> commentService.getCommentById(userNotFoundId, comment.getId())
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void getCommentById_whenCommentNotFound_thenNotFoundExceptionThrown() {
        long commentNotFoundId = 0L;
        String error = String.format("Comment with id = %d not found", commentNotFoundId);
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockCommentRepository.findById(commentNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> commentService.getCommentById(requesterBoss.getId(), commentNotFoundId)
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void updateComment_whenUpdateCommentByAdmin_thenReturnUpdateCommentDto() {
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockCommentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(mockCommentRepository.save(any())).thenReturn(updatedCommentDto);
        when(mockCommentMapper.toCommentDto(any())).thenReturn(updatedCommentDto);


        CommentDto actualCommentDto = commentService.updateComment(requesterAdmin.getId(), comment.getId(), updateCommentDto);

        assertNotNull(actualCommentDto);
        assertThat(actualCommentDto.getId(), equalTo(comment.getId()));
        assertThat(actualCommentDto.getText(), equalTo(updateCommentDto.getText()));
        assertThat(actualCommentDto.getPet().getId(), equalTo(pet.getId()));
        assertThat(actualCommentDto.getAuthor().getId(), equalTo(requesterAdmin.getId()));
        assertThat(actualCommentDto.getCreated(), equalTo(comment.getCreated()));
    }

    @Test
    void updateComment_whenUpdateCommentByBoss_thenReturnUpdateCommentDto() {
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockCommentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        String error = "Only the author of the comment can change it.";

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> commentService.updateComment(requesterBoss.getId(), comment.getId(), updateCommentDto)
        );

        assertEquals(error, exception.getMessage());
        verify(mockCommentRepository, times(0)).save(any());
    }

    @Test
    void updateComment_whenUserNotFound_thenNotFoundExceptionThrown() {
        long userNotFoundId = 0L;
        String error = String.format("User with id = %d not found", userNotFoundId);
        when(mockUserRepository.findById(userNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> commentService.updateComment(userNotFoundId, comment.getId(), updateCommentDto)
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void updateComment_whenCommentNotFound_thenNotFoundExceptionThrown() {
        long commentNotFoundId = 0L;
        String error = String.format("Comment with id = %d not found", commentNotFoundId);
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockUserRepository.findById(commentNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> commentService.updateComment(requesterBoss.getId(), commentNotFoundId, updateCommentDto)
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void updateComment_whenCommentCreatedByUserAndRequesterIsBoss_thenAccessDeniedExceptionThrown() {
        String error = "Only the author of the comment can change it.";
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockCommentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> commentService.updateComment(requesterBoss.getId(), comment.getId(), updateCommentDto)
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void getComments_whenGetCommentsByBoss_thenReturnListOfCommentDto() {
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        when(mockCommentMapper.toCommentDto(comment)).thenReturn(commentDtoForBoss);
        when(mockCommentMapper.toCommentDto(comment2)).thenReturn(commentDtoForBoss2);
        when(mockCommentRepository.findByPetId(pet.getId())).thenReturn(List.of(comment, comment2));

        List<CommentDto> actualCommentDtos = commentService.getAllCommentsByPetId(requesterBoss.getId(), pet.getId());

        assertNotNull(actualCommentDtos);
        assertThat(actualCommentDtos.size(), equalTo(2));
        assertThat(actualCommentDtos.get(0).getId(), equalTo(comment.getId()));
        assertThat(actualCommentDtos.get(0).getText(), equalTo(comment.getText()));
        assertThat(actualCommentDtos.get(0).getPet().getId(), equalTo(pet.getId()));
        assertThat(actualCommentDtos.get(0).getAuthor().getId(), equalTo(requesterBoss.getId()));
        assertThat(actualCommentDtos.get(0).getCreated(), equalTo(comment.getCreated()));
        assertThat(actualCommentDtos.get(1).getId(), equalTo(comment2.getId()));
        assertThat(actualCommentDtos.get(1).getText(), equalTo(comment2.getText()));
        assertThat(actualCommentDtos.get(1).getPet().getId(), equalTo(pet.getId()));
        assertThat(actualCommentDtos.get(1).getAuthor().getId(), equalTo(requesterBoss.getId()));
        assertThat(actualCommentDtos.get(1).getCreated(), equalTo(comment2.getCreated()));
    }

    @Test
    void getComments_whenGetCommentsByAdmin_thenReturnListOfCommentDto() {
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        when(mockCommentMapper.toCommentDto(comment)).thenReturn(commentDtoForBoss);
        when(mockCommentMapper.toCommentDto(comment2)).thenReturn(commentDtoForBoss2);
        when(mockCommentRepository.findByPetId(pet.getId())).thenReturn(List.of(comment, comment2));

        List<CommentDto> actualCommentDtos = commentService.getAllCommentsByPetId(requesterAdmin.getId(), pet.getId());

        assertNotNull(actualCommentDtos);
        assertThat(actualCommentDtos.size(), equalTo(2));
        assertThat(actualCommentDtos.get(0).getId(), equalTo(comment.getId()));
        assertThat(actualCommentDtos.get(0).getText(), equalTo(comment.getText()));
        assertThat(actualCommentDtos.get(0).getPet().getId(), equalTo(pet.getId()));
        assertThat(actualCommentDtos.get(0).getAuthor().getId(), equalTo(requesterBoss.getId()));
        assertThat(actualCommentDtos.get(0).getCreated(), equalTo(comment.getCreated()));
        assertThat(actualCommentDtos.get(1).getId(), equalTo(comment2.getId()));
        assertThat(actualCommentDtos.get(1).getText(), equalTo(comment2.getText()));
        assertThat(actualCommentDtos.get(1).getPet().getId(), equalTo(pet.getId()));
        assertThat(actualCommentDtos.get(1).getAuthor().getId(), equalTo(requesterBoss.getId()));
        assertThat(actualCommentDtos.get(1).getCreated(), equalTo(comment2.getCreated()));
    }

    @Test
    void getComments_whenGetCommentsByUser_thenReturnListOfCommentDto() {
        when(mockUserRepository.findById(requesterUser.getId())).thenReturn(Optional.of(requesterUser));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        when(mockCommentMapper.toCommentDto(comment)).thenReturn(commentDtoForBoss);
        when(mockCommentMapper.toCommentDto(comment2)).thenReturn(commentDtoForBoss2);
        when(mockCommentRepository.findByPetId(pet.getId())).thenReturn(List.of(comment, comment2));

        List<CommentDto> actualCommentDtos = commentService.getAllCommentsByPetId(requesterUser.getId(), pet.getId());

        assertNotNull(actualCommentDtos);
        assertThat(actualCommentDtos.size(), equalTo(2));
        assertThat(actualCommentDtos.get(0).getId(), equalTo(comment.getId()));
        assertThat(actualCommentDtos.get(0).getText(), equalTo(comment.getText()));
        assertThat(actualCommentDtos.get(0).getPet().getId(), equalTo(pet.getId()));
        assertThat(actualCommentDtos.get(0).getAuthor().getId(), equalTo(requesterBoss.getId()));
        assertThat(actualCommentDtos.get(0).getCreated(), equalTo(comment.getCreated()));
        assertThat(actualCommentDtos.get(1).getId(), equalTo(comment2.getId()));
        assertThat(actualCommentDtos.get(1).getText(), equalTo(comment2.getText()));
        assertThat(actualCommentDtos.get(1).getPet().getId(), equalTo(pet.getId()));
        assertThat(actualCommentDtos.get(1).getAuthor().getId(), equalTo(requesterBoss.getId()));
        assertThat(actualCommentDtos.get(1).getCreated(), equalTo(comment2.getCreated()));
    }

    @Test
    void getComments_whenUserNotFound_thenNotFoundExceptionThrown() {
        long userNotFoundId = 0L;
        String error = String.format("User with id = %d not found", userNotFoundId);
        when(mockUserRepository.findById(userNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> commentService.getAllCommentsByPetId(userNotFoundId, pet.getId())
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void getComments_whenPetNotFound_thenNotFoundExceptionThrown() {
        long petNotFoundId = 0L;
        String error = String.format("Pet with id = %d not found", petNotFoundId);
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetRepository.findById(petNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> commentService.getAllCommentsByPetId(requesterBoss.getId(), petNotFoundId)
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void deleteCommentById_whenCommentByAdminAndDeleteCommentByBoss_thenDeletedComment() {
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockCommentRepository.findById(any())).thenReturn(Optional.of(comment));
        doNothing().when(mockCommentRepository).deleteById(any());

        commentService.deleteComment(requesterBoss.getId(), comment.getId());

        verify(mockCommentRepository, times(1)).deleteById(any());
    }

    @Test
    void deleteCommentById_whenCommentByAdminAndDeleteCommentByAdmin_thenDeletedComment() {
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockCommentRepository.findById(any())).thenReturn(Optional.of(comment));
        doNothing().when(mockCommentRepository).deleteById(any());

        commentService.deleteComment(requesterAdmin.getId(), comment.getId());

        verify(mockCommentRepository, times(1)).deleteById(any());
    }

    @Test
    void deleteCommentById_whenCommentByAdminAndDeleteCommentByUser_thenAccessDeniedExceptionThrown() {
        String error = "Only the author of the comment or boss can delete it.";
        when(mockUserRepository.findById(requesterUser.getId())).thenReturn(Optional.of(requesterUser));
        when(mockCommentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> commentService.deleteComment(requesterUser.getId(), comment.getId())
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void deleteComment_whenUserNotFound_thenNotFoundExceptionThrown() {
        long userNotFoundId = 0L;
        String error = String.format("User with id = %d not found", userNotFoundId);
        when(mockUserRepository.findById(userNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> commentService.deleteComment(userNotFoundId, comment.getId())
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void deleteComment_whenCommentNotFound_thenNotFoundExceptionThrown() {
        long commentNotFoundId = 0L;
        String error = String.format("Comment with id = %d not found", commentNotFoundId);
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockUserRepository.findById(commentNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> commentService.deleteComment(requesterBoss.getId(), commentNotFoundId)
        );

        assertEquals(error, exception.getMessage());
    }


}
