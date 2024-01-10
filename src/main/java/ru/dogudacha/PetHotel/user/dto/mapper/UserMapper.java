package ru.dogudacha.PetHotel.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.dogudacha.PetHotel.user.dto.NewUserDto;
import ru.dogudacha.PetHotel.user.dto.UpdateUserDto;
import ru.dogudacha.PetHotel.user.dto.UserDto;
import ru.dogudacha.PetHotel.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    User toUser(NewUserDto newUserDto);

    @Mapping(target = "id", ignore = true)
    User toUser(UpdateUserDto updateUserDto);

    List<UserDto> map(List<User> users);
}
