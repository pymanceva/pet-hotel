package ru.dogudacha.PetHotel.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.dogudacha.PetHotel.comment.dto.CommentDto;
import ru.dogudacha.PetHotel.comment.dto.NewCommentDto;
import ru.dogudacha.PetHotel.comment.model.Comment;
import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.model.Pet;

import java.util.ArrayList;


@Mapper(componentModel = "spring")
public interface CommentMapper {

//    CommentDto toCommentDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    Comment toComment(NewCommentDto newCommentDto);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "pet.id", target = "petId")
    CommentDto toCommentDto(Comment comment);

//    public Comment toComment(NewCommentDto newCommentDto, User user, Event event) {
//        return Comment.builder()
//                .text(newCommentDto.getText())
//                .event(event)
//                .author(user)
//                .build();
//    }
//
//    public CommentDto toCommentDto(Comment comment) {
//        return CommentDto.builder()
//                .id(comment.getId())
//                .text(comment.getText())
//                .event(EventMapper.toEventShortDto(comment.getEvent()))
//                .author(UserMapper.toUserShortDto(comment.getAuthor()))
//                .created(comment.getCreated())
//                .build();
//    }
//
//    public List<CommentDto> toCommentDto(Iterable<Comment> comments) {
//        List<CommentDto> result = new ArrayList<>();
//
//        for (Comment comment : comments) {
//            result.add(toCommentDto(comment));
//        }
//
//        return result;
//    }
}
