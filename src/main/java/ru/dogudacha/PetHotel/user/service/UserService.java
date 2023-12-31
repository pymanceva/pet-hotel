package ru.dogudacha.PetHotel.user.service;

import ru.dogudacha.PetHotel.user.dto.UpdateUserDto;
import ru.dogudacha.PetHotel.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto addUser(Long requesterId, UserDto newUserDto);

    UserDto getUserById(Long requesterId, Long id);

    UserDto updateUser(Long requesterId, Long userId, UpdateUserDto userDto);

    Collection<UserDto> getAllUsers(Long requesterId);

    void deleteUserById(Long id, Long aLong);
}
