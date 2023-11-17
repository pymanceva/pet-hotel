package ru.dogudacha.PetHotel.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.dogudacha.PetHotel.comment.controller.CommentController;
import ru.dogudacha.PetHotel.comment.dto.CommentDto;
import ru.dogudacha.PetHotel.comment.dto.NewCommentDto;
import ru.dogudacha.PetHotel.comment.dto.UpdateCommentDto;
import ru.dogudacha.PetHotel.comment.service.CommentService;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.model.Sex;
import ru.dogudacha.PetHotel.pet.model.TypeOfDiet;
import ru.dogudacha.PetHotel.user.dto.UserDto;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
public class CommentControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private final String requesterHeader = "X-PetHotel-User-Id";

    LocalDateTime dateTime = LocalDateTime.of(2023, 1, 8, 12, 30);
    String formattedDateTime = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);

    final User requesterBoss = User.builder()
            .email("boss@mail.ru")
            .name("boss")
            .id(1L)
            .role(Roles.ROLE_BOSS)
            .build();

    final UserDto requesterBossDto = UserDto.builder()
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

    final CommentDto commentDtoByBoss = CommentDto.builder()
            .id(1L)
            .text("Very good dog")
            .pet(petDto)
            .author(requesterBossDto)
            .created(dateTime)
            .build();

    final CommentDto commentDtoByAdmin = CommentDto.builder()
            .id(1L)
            .text("Very good cat")
            .pet(petDto)
            .author(requesterAdminDto)
            .created(dateTime)
            .build();

    final CommentDto commentDtoByUser = CommentDto.builder()
            .id(1L)
            .text("Very good pig")
            .pet(petDto)
            .author(requesterUserDto)
            .created(dateTime)
            .build();

    final UpdateCommentDto updateCommentDtoByBoss = UpdateCommentDto.builder()
            .text("Very very good dog")
            .build();

    final UpdateCommentDto updateCommentDtoByAdmin = UpdateCommentDto.builder()
            .text("Very very good cat")
            .build();

    final UpdateCommentDto updateCommentDtoByUser = UpdateCommentDto.builder()
            .text("Very very good pig")
            .build();

    final CommentDto updatedCommentDtoByBoss = CommentDto.builder()
            .id(1L)
            .text("Very very good dog")
            .pet(petDto)
            .author(requesterBossDto)
            .created(commentDtoByBoss.getCreated())
            .build();

    final CommentDto updatedCommentDtoByAdmin = CommentDto.builder()
            .id(1L)
            .text("Very very good cat")
            .pet(petDto)
            .author(requesterAdminDto)
            .created(commentDtoByAdmin.getCreated())
            .build();

    final CommentDto updatedCommentDtoByUser = CommentDto.builder()
            .id(1L)
            .text("Very very good pig")
            .pet(petDto)
            .author(requesterUserDto)
            .created(commentDtoByUser.getCreated())
            .build();


    @Test
    @SneakyThrows
    void addComment() {
        when(commentService.createComment(any(), any(NewCommentDto.class), any())).thenReturn(commentDtoByBoss);

        mockMvc.perform(post("/comments/pets/{petId}", petDto.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(commentDtoByBoss)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", is(commentDtoByBoss.getText())))
                .andExpect(jsonPath("$.pet.id", is(commentDtoByBoss.getPet().getId()), Long.class))
                .andExpect(jsonPath("$.author.id", is(commentDtoByBoss.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$.created", is(formattedDateTime)));

        when(commentService.createComment(any(), any(NewCommentDto.class), any())).thenReturn(commentDtoByAdmin);

        mockMvc.perform(post("/comments/pets/{petId}", petDto.getId())
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(commentDtoByAdmin)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", is(commentDtoByAdmin.getText())))
                .andExpect(jsonPath("$.pet.id", is(commentDtoByAdmin.getPet().getId()), Long.class))
                .andExpect(jsonPath("$.author.id", is(commentDtoByAdmin.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$.created", is(formattedDateTime)));

        when(commentService.createComment(any(), any(NewCommentDto.class), any())).thenReturn(commentDtoByUser);

        mockMvc.perform(post("/comments/pets/{petId}", petDto.getId())
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(commentDtoByUser)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", is(commentDtoByUser.getText())))
                .andExpect(jsonPath("$.pet.id", is(commentDtoByUser.getPet().getId()), Long.class))
                .andExpect(jsonPath("$.author.id", is(commentDtoByUser.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$.created", is(formattedDateTime)));

        mockMvc.perform(post("/comments/pets/{petId}", petDto.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(commentDtoByAdmin)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/comments/pets/{petId}", petDto.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new CommentDto())))
                .andExpect(status().isBadRequest());

        String errorNotFoundComment = String.format("Pet with id = %d not found", 0L);
        when(commentService.createComment(anyLong(), any(), eq(0L))).thenThrow(new NotFoundException(errorNotFoundComment));

        mockMvc.perform(post("/comments/pets/{petId}", 0L)
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(commentDtoByBoss)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(commentService, times(4)).createComment(anyLong(), any(NewCommentDto.class), any());
    }

    @Test
    @SneakyThrows
    void getCommentById() {
        when(commentService.getCommentById(any(), any())).thenReturn(commentDtoByBoss);

        mockMvc.perform(get("/comments/{commentId}", commentDtoByBoss.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", is(commentDtoByBoss.getText())))
                .andExpect(jsonPath("$.pet.id", is(commentDtoByBoss.getPet().getId()), Long.class))
                .andExpect(jsonPath("$.author.id", is(commentDtoByBoss.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$.created", is(formattedDateTime)));

        when(commentService.getCommentById(any(), any())).thenReturn(commentDtoByAdmin);

        mockMvc.perform(get("/comments/{commentId}", commentDtoByAdmin.getId())
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", is(commentDtoByAdmin.getText())))
                .andExpect(jsonPath("$.pet.id", is(commentDtoByAdmin.getPet().getId()), Long.class))
                .andExpect(jsonPath("$.author.id", is(commentDtoByAdmin.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$.created", is(formattedDateTime)));

        when(commentService.getCommentById(any(), any())).thenReturn(commentDtoByUser);

        mockMvc.perform(get("/comments/{commentId}", commentDtoByUser.getId())
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", is(commentDtoByUser.getText())))
                .andExpect(jsonPath("$.pet.id", is(commentDtoByUser.getPet().getId()), Long.class))
                .andExpect(jsonPath("$.author.id", is(commentDtoByUser.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$.created", is(formattedDateTime)));

        mockMvc.perform(get("/comments/{commentId}", commentDtoByBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        String errorNotFoundComment = String.format("Comment with id = %d not found", 0L);
        when(commentService.getCommentById(anyLong(), eq(0L))).thenThrow(new NotFoundException(errorNotFoundComment));

        mockMvc.perform(get("/comments/{commentId}", 0L)
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(commentService, times(4)).getCommentById(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void updateComment() {
        when(commentService.updateComment(any(), any(), any(UpdateCommentDto.class))).thenReturn(updatedCommentDtoByBoss);

        mockMvc.perform(patch("/comments/{commentId}", commentDtoByBoss.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatedCommentDtoByBoss)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", is(updateCommentDtoByBoss.getText())))
                .andExpect(jsonPath("$.pet.id", is(petDto.getId()), Long.class))
                .andExpect(jsonPath("$.author.id", is(requesterBoss.getId()), Long.class))
                .andExpect(jsonPath("$.created", is(formattedDateTime)));

        when(commentService.updateComment(any(), any(), any(UpdateCommentDto.class))).thenReturn(updatedCommentDtoByAdmin);

        mockMvc.perform(patch("/comments/{commentId}", commentDtoByAdmin.getId())
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatedCommentDtoByAdmin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", is(updateCommentDtoByAdmin.getText())))
                .andExpect(jsonPath("$.pet.id", is(petDto.getId()), Long.class))
                .andExpect(jsonPath("$.author.id", is(requesterAdmin.getId()), Long.class))
                .andExpect(jsonPath("$.created", is(formattedDateTime)));

        when(commentService.updateComment(any(), any(), any(UpdateCommentDto.class))).thenReturn(updatedCommentDtoByUser);

        mockMvc.perform(patch("/comments/{commentId}", commentDtoByUser.getId())
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatedCommentDtoByUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", is(updateCommentDtoByUser.getText())))
                .andExpect(jsonPath("$.pet.id", is(petDto.getId()), Long.class))
                .andExpect(jsonPath("$.author.id", is(requesterUser.getId()), Long.class))
                .andExpect(jsonPath("$.created", is(formattedDateTime)));

        mockMvc.perform(patch("/comments/{commentId}", commentDtoByBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        String errorNotFoundComment = String.format("Comment with id = %d not found", 0L);
        when(commentService.updateComment(anyLong(), eq(0L), any())).thenThrow(new NotFoundException(errorNotFoundComment));

        mockMvc.perform(patch("/comments/{commentId}", 0L)
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatedCommentDtoByUser)))
                .andDo(print())
                .andExpect(status().isNotFound());

        String error = "Only the author of the comment can change it.";
        when(commentService.updateComment(anyLong(), any(), any())).thenThrow(new AccessDeniedException(error));

        mockMvc.perform(patch("/comments/{commentId}", commentDtoByUser.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatedCommentDtoByUser)))
                .andDo(print())
                .andExpect(status().isForbidden());

        verify(commentService, times(5)).updateComment(anyLong(), any(), any());
    }

    @Test
    @SneakyThrows
    void getAllComments() {
        when(commentService.getAllCommentsByPetId(any(), any()))
                .thenReturn(List.of(commentDtoByBoss, commentDtoByAdmin, commentDtoByUser));

        mockMvc.perform(get("/comments/pets/{petId}", petDto.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].text", is(commentDtoByBoss.getText())))
                .andExpect(jsonPath("$[0].pet.id", is(commentDtoByBoss.getPet().getId()), Long.class))
                .andExpect(jsonPath("$[0].author.id", is(commentDtoByBoss.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$[0].created", is(formattedDateTime)))
                .andExpect(jsonPath("$[1].id", notNullValue()))
                .andExpect(jsonPath("$[1].text", is(commentDtoByAdmin.getText())))
                .andExpect(jsonPath("$[1].pet.id", is(commentDtoByAdmin.getPet().getId()), Long.class))
                .andExpect(jsonPath("$[1].author.id", is(commentDtoByAdmin.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$[1].created", is(formattedDateTime)))
                .andExpect(jsonPath("$[2].id", notNullValue()))
                .andExpect(jsonPath("$[2].text", is(commentDtoByUser.getText())))
                .andExpect(jsonPath("$[2].pet.id", is(commentDtoByUser.getPet().getId()), Long.class))
                .andExpect(jsonPath("$[2].author.id", is(commentDtoByUser.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$[2].created", is(formattedDateTime)));

        mockMvc.perform(get("/comments/pets/{petId}", petDto.getId())
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].text", is(commentDtoByBoss.getText())))
                .andExpect(jsonPath("$[0].pet.id", is(commentDtoByBoss.getPet().getId()), Long.class))
                .andExpect(jsonPath("$[0].author.id", is(commentDtoByBoss.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$[0].created", is(formattedDateTime)))
                .andExpect(jsonPath("$[1].id", notNullValue()))
                .andExpect(jsonPath("$[1].text", is(commentDtoByAdmin.getText())))
                .andExpect(jsonPath("$[1].pet.id", is(commentDtoByAdmin.getPet().getId()), Long.class))
                .andExpect(jsonPath("$[1].author.id", is(commentDtoByAdmin.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$[1].created", is(formattedDateTime)))
                .andExpect(jsonPath("$[2].id", notNullValue()))
                .andExpect(jsonPath("$[2].text", is(commentDtoByUser.getText())))
                .andExpect(jsonPath("$[2].pet.id", is(commentDtoByUser.getPet().getId()), Long.class))
                .andExpect(jsonPath("$[2].author.id", is(commentDtoByUser.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$[2].created", is(formattedDateTime)));

        mockMvc.perform(get("/comments/pets/{petId}", petDto.getId())
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].text", is(commentDtoByBoss.getText())))
                .andExpect(jsonPath("$[0].pet.id", is(commentDtoByBoss.getPet().getId()), Long.class))
                .andExpect(jsonPath("$[0].author.id", is(commentDtoByBoss.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$[0].created", is(formattedDateTime)))
                .andExpect(jsonPath("$[1].id", notNullValue()))
                .andExpect(jsonPath("$[1].text", is(commentDtoByAdmin.getText())))
                .andExpect(jsonPath("$[1].pet.id", is(commentDtoByAdmin.getPet().getId()), Long.class))
                .andExpect(jsonPath("$[1].author.id", is(commentDtoByAdmin.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$[1].created", is(formattedDateTime)))
                .andExpect(jsonPath("$[2].id", notNullValue()))
                .andExpect(jsonPath("$[2].text", is(commentDtoByUser.getText())))
                .andExpect(jsonPath("$[2].pet.id", is(commentDtoByUser.getPet().getId()), Long.class))
                .andExpect(jsonPath("$[2].author.id", is(commentDtoByUser.getAuthor().getId()), Long.class))
                .andExpect(jsonPath("$[2].created", is(formattedDateTime)));

        mockMvc.perform(get("/comments/pets/{petId}", petDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        String errorNotFoundComment = String.format("Pet with id = %d not found", 0L);
        when(commentService.getAllCommentsByPetId(anyLong(), eq(0L))).thenThrow(new NotFoundException(errorNotFoundComment));

        mockMvc.perform(get("/comments/pets/{petId}", 0L)
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(commentService, times(4)).getAllCommentsByPetId(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void deleteComment() {
        doNothing().when(commentService).deleteComment(anyLong(), anyLong());

        mockMvc.perform(delete("/comments/{commentId}", commentDtoByBoss.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/comments/{commentId}", commentDtoByAdmin.getId())
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/comments/{commentId}", commentDtoByUser.getId())
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/comments/{commentId}", commentDtoByUser.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/comments/{commentId}", commentDtoByBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        String errorNotFoundComment = String.format("Comment with id = %d not found", 0L);
        doThrow(new NotFoundException(errorNotFoundComment)).when(commentService).deleteComment(anyLong(), anyLong());

        mockMvc.perform(delete("/comments/{commentId}", 0L)
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(commentService, times(5)).deleteComment(anyLong(), any());
    }
}
