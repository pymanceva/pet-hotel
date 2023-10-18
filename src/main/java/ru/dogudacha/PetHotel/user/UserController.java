package ru.dogudacha.PetHotel.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.dogudacha.PetHotel.user.dto.UpdateUserDto;
import ru.dogudacha.PetHotel.user.dto.UserDto;
import ru.dogudacha.PetHotel.user.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId,
                           @RequestBody @Valid UserDto userDto
    ) {

        log.info("UserController: receive POST request from requesterId={} for add new user with body={}",
                requesterId, userDto);
        return userService.addUser(requesterId, userDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId,
                               @PathVariable(value = "id") long id
    ) {
        log.info("receive GET request for return user by id={}", id);
        return userService.getUserById(requesterId, id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId,
                              @RequestBody @Valid UpdateUserDto updateUserDto,
                              @PathVariable(value = "id") Long userId
    ) {
        log.info("receive PATCH request for update user with id={}, requestBody={}", userId, updateUserDto);
        return userService.updateUser(requesterId, userId, updateUserDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getAllUsers(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId
    ) {
        log.info("receive GET request for return all users");
        return userService.getAllUsers(requesterId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId,
                               @PathVariable("id") Long id
    ) {
        log.info("receive DELETE request fo delete user with id= {}", id);
        userService.deleteUserById(requesterId, id);
    }
}
