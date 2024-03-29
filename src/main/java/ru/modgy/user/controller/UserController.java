package ru.modgy.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.modgy.user.dto.NewUserDto;
import ru.modgy.user.dto.UpdateUserDto;
import ru.modgy.user.dto.UserDto;
import ru.modgy.user.service.UserService;

import java.util.Collection;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private static final String USER_ID = "X-PetHotel-User-Id";

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestHeader(value = USER_ID) Long requesterId,
                           @RequestBody @Valid NewUserDto userDto

    ) {
        log.info("UserController: POST/addUser, requesterId={}, user={}", requesterId, userDto);
        return userService.addUser(requesterId, userDto);
    }

    @GetMapping
    public Collection<UserDto> getAllUsers(@RequestHeader(value = USER_ID) Long requesterId,
                                           @RequestParam(value = "isActive", required = false) Boolean isActive
    ) {
        log.info("UserController: GET/getAllUsers, requesterId={}, isActive={}", requesterId, isActive);
        return userService.getAllUsers(requesterId, isActive);
    }


    @GetMapping("/{id}")
    public UserDto getUserById(@RequestHeader(value = USER_ID) Long requesterId,
                               @PathVariable(value = "id") long userId
    ) {
        log.info("UserController: GET/getUserById, requesterId={}, userId={}", requesterId, userId);
        return userService.getUserById(requesterId, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@RequestHeader(value = USER_ID) Long requesterId,
                               @PathVariable("id") Long userId
    ) {
        log.info("UserController: DELETE/deleteUserById, requesterId={}, userId={}", requesterId, userId);
        userService.deleteUserById(requesterId, userId);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestHeader(value = USER_ID) Long requesterId,
                              @PathVariable(value = "id") Long userId,
                              @RequestBody @Valid UpdateUserDto updateUserDto
    ) {
        log.info("UserController: PATCH/updateUser, requesterId={}, userId={}, requestBody={}",
                requesterId, userId, updateUserDto);
        return userService.updateUser(requesterId, userId, updateUserDto);
    }

    @PatchMapping("/{id}/state")
    public UserDto setUserState(@RequestHeader(value = USER_ID) Long requesterId,
                                @PathVariable(value = "id") Long userId,
                                @RequestParam(value = "isActive", defaultValue = "true") Boolean isActive
    ) {
        log.info("UserController: PATCH/setUserState, requesterId={}, userId={}, isActive={}",
                requesterId, userId, isActive);
        return userService.setUserState(requesterId, userId, isActive);
    }
}
