package ru.dogudacha.PetHotel.user.service.api;

import ru.dogudacha.PetHotel.user.dto.UpdateUserDto;
import ru.dogudacha.PetHotel.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto addUser(UserDto newUserDto);

    UserDto getUserById(Long id);

    UserDto updateUser(long userId, UpdateUserDto userDto);

    Collection<UserDto> getAllUsers();

    void deleteUserById(Long id);
}
