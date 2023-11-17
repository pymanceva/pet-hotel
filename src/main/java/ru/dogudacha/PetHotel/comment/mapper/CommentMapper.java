package ru.dogudacha.PetHotel.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.dogudacha.PetHotel.comment.dto.CommentDto;
import ru.dogudacha.PetHotel.comment.dto.NewCommentDto;
import ru.dogudacha.PetHotel.comment.model.Comment;
import ru.dogudacha.PetHotel.pet.mapper.PetMapper;
import ru.dogudacha.PetHotel.pet.mapper.ToPet;
import ru.dogudacha.PetHotel.user.dto.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = {PetMapper.class, UserMapper.class})
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    Comment toComment(NewCommentDto newCommentDto);

    @Mapping(source = "comment.author", target = "author")
    @Mapping(source = "comment.pet", target = "pet", qualifiedBy = ToPet.class)
    CommentDto toCommentDto(Comment comment);

}
