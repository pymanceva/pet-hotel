package ru.dogudacha.PetHotel.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.dogudacha.PetHotel.user.dto.NewUserDto;
import ru.dogudacha.PetHotel.user.dto.UserDto;
import ru.dogudacha.PetHotel.user.dto.mapper.UserMapper;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    final long requesterId = 1L;
    final User requester = User.builder()
            .email("requester@mail.ru")
            .lastName("requester")
            .id(requesterId)
            .role(Roles.ROLE_BOSS)
            .build();
    long userId = 2L;
    final String userFirstName = "new User FirstName";
    final String userMiddleName = "new User MiddleName";
    final String userLastName = "new User LastName";
    final String email = "newUser@mail.com";
    final String userPassword = "user_pwd";

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    void addUser_whenAddUserAdminByBoss_thenUserAdded() {
        Roles requesterRole = Roles.ROLE_BOSS;
        Roles userRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);
        NewUserDto newUserDto = new NewUserDto(userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        User newUserInput = new User(null, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        User newUser = new User(userId, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        UserDto userDto = new UserDto(userId, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userMapper.toUser(newUserDto)).thenReturn(newUserInput);
        when(userRepository.save(newUserInput)).thenReturn(newUser);
        when(userMapper.toUserDto(newUser)).thenReturn(userDto);

        UserDto addedUserDto = userService.addUser(requesterId, newUserDto);

        assertAll(
                () -> assertEquals(addedUserDto, userDto),
                () -> verify(userMapper).toUser(newUserDto),
                () -> verify(userRepository).save(newUserInput),
                () -> verify(userMapper).toUserDto(newUser)
        );
    }

//    @Test
//    void addUser_whenAddUserAdminByAdmin_thenAccessDeniedException() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        Roles userRole = Roles.ROLE_ADMIN;
//        requester.setRole(requesterRole);
//        UserDto newUserDto = new UserDto(userId, userName, email, userRole);
//        User newUser = new User(userId, userName, email, userRole);
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//
//        assertThrows(AccessDeniedException.class,
//                () -> userService.addUser(requesterId, newUserDto));
//    }
//
//    @Test
//    void addUser_whenAddUserUserByAdmin_thenUserAdded() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        Roles userRole = Roles.ROLE_USER;
//        requester.setRole(requesterRole);
//        UserDto newUserDto = new UserDto(userId, userName, email, userRole);
//        User newUser = new User(userId, userName, email, userRole);
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//        when(userRepository.save(newUser)).thenReturn(newUser);
//        when(userMapper.toUserDto(newUser)).thenReturn(newUserDto);
//
//        UserDto addedUserDto = userService.addUser(requesterId, newUserDto);
//
//        assertAll(
//                () -> assertEquals(addedUserDto, newUserDto),
//                () -> verify(userMapper).toUser(newUserDto),
//                () -> verify(userRepository).save(newUser),
//                () -> verify(userMapper).toUserDto(newUser)
//        );
//    }
//
//    @Test
//    void addUser_whenAddUserUserByUser_thenAccessDeniedException() {
//        Roles requesterRole = Roles.ROLE_USER;
//        Roles userRole = Roles.ROLE_USER;
//        requester.setRole(requesterRole);
//        UserDto newUserDto = new UserDto(userId, userName, email, userRole);
//        User newUser = new User(userId, userName, email, userRole);
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//
//        assertThrows(AccessDeniedException.class,
//                () -> userService.addUser(requesterId, newUserDto));
//    }
//
//    @Test
//    void addUser_whenAddUserUserByBoss_thenUserAdded() {
//        Roles requesterRole = Roles.ROLE_BOSS;
//        Roles userRole = Roles.ROLE_USER;
//        requester.setRole(requesterRole);
//        UserDto newUserDto = new UserDto(userId, userName, email, userRole);
//        User newUser = new User(userId, userName, email, userRole);
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//        when(userRepository.save(newUser)).thenReturn(newUser);
//        when(userMapper.toUserDto(newUser)).thenReturn(newUserDto);
//
//        UserDto addedUserDto = userService.addUser(requesterId, newUserDto);
//
//        assertAll(
//                () -> assertEquals(addedUserDto, newUserDto),
//                () -> verify(userMapper).toUser(newUserDto),
//                () -> verify(userRepository).save(newUser),
//                () -> verify(userMapper).toUserDto(newUser)
//        );
//    }
//
//    @Test
//    void getUserById_whenAdminFoundByBoss_thenReturnedUser() {
//        Roles requesterRole = Roles.ROLE_BOSS;
//        Roles userRole = Roles.ROLE_ADMIN;
//        requester.setRole(requesterRole);
//        User expectedUser = new User();
//        expectedUser.setRole(userRole);
//        UserDto expectedUserDto = new UserDto();
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
//        when(userMapper.toUserDto(expectedUser)).thenReturn(expectedUserDto);
//
//        UserDto returnedUserDto = userService.getUserById(requesterId, userId);
//
//        assertAll(
//                () -> assertEquals(expectedUserDto, returnedUserDto),
//                () -> verify(userRepository).findById(requesterId),
//                () -> verify(userRepository).findById(userId),
//                () -> verify(userMapper).toUserDto(expectedUser)
//        );
//    }
//
//    @Test
//    void getUserById_whenAdminFoundByAdmin_thenReturnedUser() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        Roles userRole = Roles.ROLE_ADMIN;
//        requester.setRole(requesterRole);
//        User expectedUser = new User();
//        expectedUser.setRole(userRole);
//        UserDto expectedUserDto = new UserDto();
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
//        when(userMapper.toUserDto(expectedUser)).thenReturn(expectedUserDto);
//
//        UserDto returnedUserDto = userService.getUserById(requesterId, userId);
//
//        assertAll(
//                () -> assertEquals(expectedUserDto, returnedUserDto),
//                () -> verify(userRepository).findById(requesterId),
//                () -> verify(userRepository).findById(userId),
//                () -> verify(userMapper).toUserDto(expectedUser)
//        );
//    }
//
//    @Test
//    void getUserById_whenUserFoundByBoss_thenReturnedUser() {
//        Roles requesterRole = Roles.ROLE_BOSS;
//        Roles userRole = Roles.ROLE_USER;
//        requester.setRole(requesterRole);
//        User expectedUser = new User();
//        expectedUser.setRole(userRole);
//        UserDto expectedUserDto = new UserDto();
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
//        when(userMapper.toUserDto(expectedUser)).thenReturn(expectedUserDto);
//
//        UserDto returnedUserDto = userService.getUserById(requesterId, userId);
//
//        assertAll(
//                () -> assertEquals(expectedUserDto, returnedUserDto),
//                () -> verify(userRepository).findById(requesterId),
//                () -> verify(userRepository).findById(userId),
//                () -> verify(userMapper).toUserDto(expectedUser)
//        );
//    }
//
//    @Test
//    void getUserById_whenUserFoundByAdmin_thenReturnedUser() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        Roles userRole = Roles.ROLE_USER;
//        requester.setRole(requesterRole);
//        User expectedUser = new User();
//        expectedUser.setRole(userRole);
//        UserDto expectedUserDto = new UserDto();
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
//        when(userMapper.toUserDto(expectedUser)).thenReturn(expectedUserDto);
//
//        UserDto returnedUserDto = userService.getUserById(requesterId, userId);
//
//        assertAll(
//                () -> assertEquals(expectedUserDto, returnedUserDto),
//                () -> verify(userRepository).findById(requesterId),
//                () -> verify(userRepository).findById(userId),
//                () -> verify(userMapper).toUserDto(expectedUser)
//        );
//    }
//
//    @Test
//    void getUserById_whenUserFoundBySelf_thenReturnedUser() {
//        Roles requesterRole = Roles.ROLE_USER;
//        requester.setRole(requesterRole);
//        userId = requesterId;
//        User expectedUser = requester;
//        UserDto expectedUserDto = new UserDto();
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
//        when(userMapper.toUserDto(expectedUser)).thenReturn(expectedUserDto);
//
//        UserDto returnedUserDto = userService.getUserById(requesterId, userId);
//
//        assertAll(
//                () -> assertEquals(expectedUserDto, returnedUserDto),
//                () -> verify(userRepository).findById(requesterId),
//                () -> verify(userRepository).findById(userId),
//                () -> verify(userMapper).toUserDto(expectedUser)
//        );
//    }
//
//    @Test
//    void getUserById_whenRequesterNotFound_thenNotFoundException() {
//        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class,
//                () -> userService.getUserById(requesterId, userId));
//    }
//
//    @Test
//    void getUserById_whenUserNotFound_thenNotFoundException() {
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class,
//                () -> userService.getUserById(requesterId, userId));
//    }
//
//    @Test
//    void getUserById_whenBossFoundByAdmin_thenNotFoundException() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        Roles userRole = Roles.ROLE_BOSS;
//        requester.setRole(requesterRole);
//        User expectedUser = User.builder().role(userRole).build();
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
//
//        assertThrows(AccessDeniedException.class,
//                () -> userService.getUserById(requesterId, userId));
//    }
//
//    @Test
//    void getUserById_whenAdminFoundByUser_thenNotFoundException() {
//        Roles requesterRole = Roles.ROLE_USER;
//        Roles userRole = Roles.ROLE_ADMIN;
//        requester.setRole(requesterRole);
//        User expectedUser = User.builder().role(userRole).build();
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
//
//        assertThrows(AccessDeniedException.class,
//                () -> userService.getUserById(requesterId, userId));
//    }
//
//    @Test
//    void updateUser_whenRequesterBossAndUserFoundAndAllNewFieldsNotNull_thenUpdateAllFieldsThanId() {
//        Roles requesterRole = Roles.ROLE_BOSS;
//        Roles oldUserRole = Roles.ROLE_USER;
//        Roles newUserRole = Roles.ROLE_FINANCIAL;
//        requester.setRole(requesterRole);
//
//        UpdateUserDto newUserDto = UpdateUserDto.builder()
//                .id(userId + 1)
//                .name("new" + userName)
//                .email("new" + email)
//                .role(newUserRole)
//                .build();
//
//        User oldUser = User.builder()
//                .id(userId)
//                .name(userName)
//                .email(email)
//                .role(oldUserRole)
//                .build();
//
//        User newUser = User.builder()
//                .id(newUserDto.getId())
//                .name(newUserDto.getName())
//                .email(newUserDto.getEmail())
//                .role(newUserDto.getRole())
//                .build();
//
//        User userAfter = User.builder()
//                .id(oldUser.getId())
//                .name(newUserDto.getName())
//                .email(newUser.getEmail())
//                .role(newUserDto.getRole())
//                .build();
//
//        UserDto userDtoAfter = UserDto.builder()
//                .id(userAfter.getId())
//                .name(userAfter.getName())
//                .email(userAfter.getEmail())
//                .role(userAfter.getRole())
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//        when(userRepository.save(newUser)).thenReturn(userAfter);
//        when(userMapper.toUserDto(userAfter)).thenReturn(userDtoAfter);
//
//        UserDto returnedUserDto = userService.updateUser(requesterId, userId, newUserDto);
//
//        verify(userRepository).save(userArgumentCaptor.capture());
//        User userForSave = userArgumentCaptor.getValue();
//
//        assertAll(
//                () -> assertEquals(userDtoAfter, returnedUserDto, "entity field test failed"),
//                () -> assertNotEquals(newUserDto.getId(), userForSave.getId(), "id field test1 failed"),
//                () -> assertEquals(oldUser.getId(), userForSave.getId(), "id field test2 failed"),
//                () -> assertEquals(newUserDto.getName(), userForSave.getName(), "name field test failed"),
//                () -> assertEquals(newUserDto.getEmail(), userForSave.getEmail(), "email field test failed"),
//                () -> assertEquals(newUserDto.getRole(), userForSave.getRole(), "role field test failed"),
//
//
//                () -> verify(userRepository).findById(requesterId),
//                () -> verify(userRepository).findById(userId),
//                () -> verify(userMapper).toUser(newUserDto),
//                () -> verify(userMapper).toUserDto(userAfter)
//        );
//
//    }
//
//    @Test
//    void updateUser_whenRequesterBossAndUserFoundAndOnlyEmailNotNull_thenUpdateEmailOnly() {
//        Roles requesterRole = Roles.ROLE_BOSS;
//        Roles oldUserRole = Roles.ROLE_USER;
//        requester.setRole(requesterRole);
//
//        UpdateUserDto newUserDto = UpdateUserDto.builder()
//                .id(userId + 1)
//                .name(null)
//                .email("new" + email)
//                .role(null)
//                .build();
//
//        User oldUser = User.builder()
//                .id(userId)
//                .name(userName)
//                .email(email)
//                .role(oldUserRole)
//                .build();
//
//        User newUser = User.builder()
//                .id(newUserDto.getId())
//                .name(newUserDto.getName())
//                .email(newUserDto.getEmail())
//                .role(newUserDto.getRole())
//                .build();
//
//        User userAfter = User.builder()
//                .id(oldUser.getId())
//                .name(oldUser.getName())
//                .email(newUserDto.getEmail())
//                .role(oldUser.getRole())
//                .build();
//
//        UserDto userDtoAfter = UserDto.builder()
//                .id(userAfter.getId())
//                .name(userAfter.getName())
//                .email(userAfter.getEmail())
//                .role(userAfter.getRole())
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//        when(userRepository.save(newUser)).thenReturn(userAfter);
//        when(userMapper.toUserDto(userAfter)).thenReturn(userDtoAfter);
//
//        UserDto returnedUserDto = userService.updateUser(requesterId, userId, newUserDto);
//        verify(userRepository).save(userArgumentCaptor.capture());
//        User userForSave = userArgumentCaptor.getValue();
//
//        assertAll(
//                () -> assertEquals(userDtoAfter, returnedUserDto, "entity field test failed"),
//                () -> assertNotEquals(newUserDto.getId(), userForSave.getId(), "id field test1 failed"),
//                () -> assertEquals(oldUser.getId(), userForSave.getId(), "id field test2 failed"),
//                () -> assertEquals(oldUser.getName(), userForSave.getName(), "name field test failed"),
//                () -> assertEquals(newUserDto.getEmail(), userForSave.getEmail(), "email field test failed"),
//                () -> assertEquals(oldUser.getRole(), userForSave.getRole(), "role field test failed"),
//
//
//                () -> verify(userRepository).findById(requesterId),
//                () -> verify(userRepository).findById(userId),
//                () -> verify(userMapper).toUser(newUserDto),
//                () -> verify(userMapper).toUserDto(userAfter)
//        );
//    }
//
//    @Test
//    void updateUser_whenRequesterAdminAndUserFoundAndAllNewFieldsNotNull_thenUpdateAllFieldsThanId() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        Roles oldUserRole = Roles.ROLE_USER;
//        Roles newUserRole = Roles.ROLE_FINANCIAL;
//        requester.setRole(requesterRole);
//
//        UpdateUserDto newUserDto = UpdateUserDto.builder()
//                .id(userId + 1)
//                .name("new" + userName)
//                .email("new" + email)
//                .role(newUserRole)
//                .build();
//
//        User oldUser = User.builder()
//                .id(userId)
//                .name(userName)
//                .email(email)
//                .role(oldUserRole)
//                .build();
//
//        User newUser = User.builder()
//                .id(newUserDto.getId())
//                .name(newUserDto.getName())
//                .email(newUserDto.getEmail())
//                .role(newUserDto.getRole())
//                .build();
//
//        User userAfter = User.builder()
//                .id(oldUser.getId())
//                .name(newUserDto.getName())
//                .email(newUser.getEmail())
//                .role(newUserDto.getRole())
//                .build();
//
//        UserDto userDtoAfter = UserDto.builder()
//                .id(userAfter.getId())
//                .name(userAfter.getName())
//                .email(userAfter.getEmail())
//                .role(userAfter.getRole())
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//        when(userRepository.save(newUser)).thenReturn(userAfter);
//        when(userMapper.toUserDto(userAfter)).thenReturn(userDtoAfter);
//
//        UserDto returnedUserDto = userService.updateUser(requesterId, userId, newUserDto);
//
//        verify(userRepository).save(userArgumentCaptor.capture());
//        User userForSave = userArgumentCaptor.getValue();
//
//        assertAll(
//                () -> assertEquals(userDtoAfter, returnedUserDto, "entity field test failed"),
//                () -> assertNotEquals(newUserDto.getId(), userForSave.getId(), "id field test1 failed"),
//                () -> assertEquals(oldUser.getId(), userForSave.getId(), "id field test2 failed"),
//                () -> assertEquals(newUserDto.getName(), userForSave.getName(), "name field test failed"),
//                () -> assertEquals(newUserDto.getEmail(), userForSave.getEmail(), "email field test failed"),
//                () -> assertEquals(newUserDto.getRole(), userForSave.getRole(), "role field test failed"),
//
//
//                () -> verify(userRepository).findById(requesterId),
//                () -> verify(userRepository).findById(userId),
//                () -> verify(userMapper).toUser(newUserDto),
//                () -> verify(userMapper).toUserDto(userAfter)
//        );
//
//    }
//
//    @Test
//    void updateUser_whenRequesterAdminAndUserFoundAndOnlyEmailNotNull_thenUpdateEmailOnly() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        Roles oldUserRole = Roles.ROLE_USER;
//        requester.setRole(requesterRole);
//
//        UpdateUserDto newUserDto = UpdateUserDto.builder()
//                .id(userId + 1)
//                .name(null)
//                .email("new" + email)
//                .role(null)
//                .build();
//
//        User oldUser = User.builder()
//                .id(userId)
//                .name(userName)
//                .email(email)
//                .role(oldUserRole)
//                .build();
//
//        User newUser = User.builder()
//                .id(newUserDto.getId())
//                .name(newUserDto.getName())
//                .email(newUserDto.getEmail())
//                .role(newUserDto.getRole())
//                .build();
//
//        User userAfter = User.builder()
//                .id(oldUser.getId())
//                .name(oldUser.getName())
//                .email(newUserDto.getEmail())
//                .role(oldUser.getRole())
//                .build();
//
//        UserDto userDtoAfter = UserDto.builder()
//                .id(userAfter.getId())
//                .name(userAfter.getName())
//                .email(userAfter.getEmail())
//                .role(userAfter.getRole())
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//        when(userRepository.save(newUser)).thenReturn(userAfter);
//        when(userMapper.toUserDto(userAfter)).thenReturn(userDtoAfter);
//
//        UserDto returnedUserDto = userService.updateUser(requesterId, userId, newUserDto);
//        verify(userRepository).save(userArgumentCaptor.capture());
//        User userForSave = userArgumentCaptor.getValue();
//
//        assertAll(
//                () -> assertEquals(userDtoAfter, returnedUserDto, "entity field test failed"),
//                () -> assertNotEquals(newUserDto.getId(), userForSave.getId(), "id field test1 failed"),
//                () -> assertEquals(oldUser.getId(), userForSave.getId(), "id field test2 failed"),
//                () -> assertEquals(oldUser.getName(), userForSave.getName(), "name field test failed"),
//                () -> assertEquals(newUserDto.getEmail(), userForSave.getEmail(), "email field test failed"),
//                () -> assertEquals(oldUser.getRole(), userForSave.getRole(), "role field test failed"),
//
//
//                () -> verify(userRepository).findById(requesterId),
//                () -> verify(userRepository).findById(userId),
//                () -> verify(userMapper).toUser(newUserDto),
//                () -> verify(userMapper).toUserDto(userAfter)
//        );
//    }
//
//    @Test
//    void updateUser_whenRequesterIsUserAndFoundAndAllNewFieldsNotNull_thenUpdateAllFieldsThanIdAndRole() {
//        Roles requesterRole = Roles.ROLE_USER;
//        Roles newUserRole = Roles.ROLE_ADMIN;
//        userId = requesterId;
//        requester.setRole(requesterRole);
//
//        UpdateUserDto newUserDto = UpdateUserDto.builder()
//                .id(requester.getId() + 1)
//                .name(requester.getName() + " new")
//                .email("new " + requester.getEmail())
//                .role(newUserRole)
//                .build();
//
//        User oldUser = User.builder()
//                .id(requester.getId())
//                .name(requester.getName())
//                .email(requester.getEmail())
//                .role(requester.getRole())
//                .build();
//
//        User newUser = User.builder()
//                .id(newUserDto.getId())
//                .name(newUserDto.getName())
//                .email(newUserDto.getEmail())
//                .role(newUserDto.getRole())
//                .build();
//
//        User userAfter = User.builder()
//                .id(oldUser.getId())
//                .name(newUserDto.getName())
//                .email(newUser.getEmail())
//                .role(oldUser.getRole())
//                .build();
//
//        UserDto userDtoAfter = UserDto.builder()
//                .id(userAfter.getId())
//                .name(userAfter.getName())
//                .email(userAfter.getEmail())
//                .role(userAfter.getRole())
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//        when(userRepository.save(newUser)).thenReturn(userAfter);
//        when(userMapper.toUserDto(userAfter)).thenReturn(userDtoAfter);
//
//        UserDto returnedUserDto = userService.updateUser(requesterId, userId, newUserDto);
//
//        verify(userRepository).save(userArgumentCaptor.capture());
//        User userForSave = userArgumentCaptor.getValue();
//
//        assertAll(
//                () -> assertEquals(userDtoAfter, returnedUserDto, "entity field test failed"),
//                () -> assertNotEquals(newUserDto.getId(), userForSave.getId(), "id field test1 failed"),
//                () -> assertEquals(oldUser.getId(), userForSave.getId(), "id field test2 failed"),
//                () -> assertEquals(newUserDto.getName(), userForSave.getName(), "name field test failed"),
//                () -> assertEquals(newUserDto.getEmail(), userForSave.getEmail(), "email field test failed"),
//                () -> assertEquals(oldUser.getRole(), userForSave.getRole(), "role field test failed"),
//
//
//                () -> verify(userRepository).findById(requesterId),
//                () -> verify(userRepository).findById(userId),
//                () -> verify(userMapper).toUser(newUserDto),
//                () -> verify(userMapper).toUserDto(userAfter)
//        );
//
//    }
//
//    @Test
//    void updateUser_whenRequesterNotFound_thenNotFoundException() {
//        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class,
//                () -> userService.updateUser(requesterId, userId, new UpdateUserDto()));
//    }
//
//    @Test
//    void updateUser_whenUserNotFound_thenNotFoundException() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        requester.setRole(requesterRole);
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userMapper.toUser(any(UpdateUserDto.class))).thenReturn(new User());
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class,
//                () -> userService.updateUser(requesterId, userId, new UpdateUserDto()));
//    }
//
//    @Test
//    void updateUser_whenAdminUpdateBoss_thenNotAccessDeniedException() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        Roles oldUserRole = Roles.ROLE_BOSS;
//        Roles newUserRole = Roles.ROLE_USER;
//
//        requester.setRole(requesterRole);
//
//        UpdateUserDto newUserDto = UpdateUserDto.builder()
//                .id(requester.getId() + 1)
//                .name(requester.getName() + " new")
//                .email("new " + requester.getEmail())
//                .role(newUserRole)
//                .build();
//
//        User oldUser = User.builder()
//                .id(userId)
//                .name(userName)
//                .email(email)
//                .role(oldUserRole)
//                .build();
//
//        User newUser = User.builder()
//                .id(newUserDto.getId())
//                .name(newUserDto.getName())
//                .email(newUserDto.getEmail())
//                .role(newUserDto.getRole())
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//
//        assertThrows(AccessDeniedException.class,
//                () -> userService.updateUser(requesterId, userId, newUserDto));
//    }
//
//    @Test
//    void updateUser_whenAdminUpdateUserToAdmin_thenNotAccessDeniedException() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        Roles oldUserRole = Roles.ROLE_USER;
//        Roles newUserRole = Roles.ROLE_ADMIN;
//
//        requester.setRole(requesterRole);
//
//        UpdateUserDto newUserDto = UpdateUserDto.builder()
//                .id(requester.getId() + 1)
//                .name(requester.getName() + " new")
//                .email("new " + requester.getEmail())
//                .role(newUserRole)
//                .build();
//
//        User oldUser = User.builder()
//                .id(userId)
//                .name(userName)
//                .email(email)
//                .role(oldUserRole)
//                .build();
//
//        User newUser = User.builder()
//                .id(newUserDto.getId())
//                .name(newUserDto.getName())
//                .email(newUserDto.getEmail())
//                .role(newUserDto.getRole())
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//
//        assertThrows(AccessDeniedException.class,
//                () -> userService.updateUser(requesterId, userId, newUserDto));
//    }
//
//
//    @Test
//    void updateUser_whenUserUpdateAdmin_thenNotAccessDeniedException() {
//        Roles requesterRole = Roles.ROLE_USER;
//        Roles oldUserRole = Roles.ROLE_ADMIN;
//        Roles newUserRole = null;
//
//        requester.setRole(requesterRole);
//
//        UpdateUserDto newUserDto = UpdateUserDto.builder()
//                .id(requester.getId() + 1)
//                .name(requester.getName() + " new")
//                .email("new " + requester.getEmail())
//                .role(newUserRole)
//                .build();
//
//        User oldUser = User.builder()
//                .id(userId)
//                .name(userName)
//                .email(email)
//                .role(oldUserRole)
//                .build();
//
//        User newUser = User.builder()
//                .id(newUserDto.getId())
//                .name(newUserDto.getName())
//                .email(newUserDto.getEmail())
//                .role(newUserDto.getRole())
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//
//        assertThrows(AccessDeniedException.class,
//                () -> userService.updateUser(requesterId, userId, newUserDto));
//    }
//
//
//    @Test
//    void updateUser_whenUserUpdateUser_thenNotAccessDeniedException() {
//        Roles requesterRole = Roles.ROLE_USER;
//        Roles oldUserRole = Roles.ROLE_USER;
//        Roles newUserRole = null;
//
//        requester.setRole(requesterRole);
//
//        UpdateUserDto newUserDto = UpdateUserDto.builder()
//                .id(requester.getId() + 1)
//                .name(requester.getName() + " new")
//                .email("new " + requester.getEmail())
//                .role(newUserRole)
//                .build();
//
//        User oldUser = User.builder()
//                .id(userId)
//                .name(userName)
//                .email(email)
//                .role(oldUserRole)
//                .build();
//
//        User newUser = User.builder()
//                .id(newUserDto.getId())
//                .name(newUserDto.getName())
//                .email(newUserDto.getEmail())
//                .role(newUserDto.getRole())
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//
//        assertThrows(AccessDeniedException.class,
//                () -> userService.updateUser(requesterId, userId, newUserDto));
//    }
//
//    @Test
//    void updateUser_whenUserUpdateFinancial_thenNotAccessDeniedException() {
//        Roles requesterRole = Roles.ROLE_USER;
//        Roles oldUserRole = Roles.ROLE_FINANCIAL;
//        Roles newUserRole = null;
//
//        requester.setRole(requesterRole);
//
//        UpdateUserDto newUserDto = UpdateUserDto.builder()
//                .id(requester.getId() + 1)
//                .name(requester.getName() + " new")
//                .email("new " + requester.getEmail())
//                .role(newUserRole)
//                .build();
//
//        User oldUser = User.builder()
//                .id(userId)
//                .name(userName)
//                .email(email)
//                .role(oldUserRole)
//                .build();
//
//        User newUser = User.builder()
//                .id(newUserDto.getId())
//                .name(newUserDto.getName())
//                .email(newUserDto.getEmail())
//                .role(newUserDto.getRole())
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
//        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
//
//        assertThrows(AccessDeniedException.class,
//                () -> userService.updateUser(requesterId, userId, newUserDto));
//    }
//
//    @Test
//    void getAllUsers_whenBossGetAllUsers_returnAllUsers() {
//        Roles requesterRole = Roles.ROLE_BOSS;
//        Roles userRole1 = Roles.ROLE_USER;
//        Roles userRole2 = Roles.ROLE_ADMIN;
//        Roles userRole3 = Roles.ROLE_FINANCIAL;
//        List<Roles> roles = Arrays.stream(Roles.values()).toList();
//        requester.setRole(requesterRole);
//
//        User user1 = new User(userId + 1, "1" + userName, "1" + email, userRole1);
//        User user2 = new User(userId + 2, "2" + userName, "2" + email, userRole2);
//        User user3 = new User(userId + 3, "3" + userName, "3" + email, userRole3);
//        List<User> userList = List.of(user1, user2, user3);
//        UserDto userDto1 = new UserDto(userId + 1, "1" + userName, "1" + email, userRole1);
//        UserDto userDto2 = new UserDto(userId + 2, "2" + userName, "2" + email, userRole2);
//        UserDto userDto3 = new UserDto(userId + 3, "3" + userName, "3" + email, userRole3);
//        List<UserDto> userDtoList = List.of(userDto1, userDto2, userDto3);
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findAllByRoleInAndIsActive(roles)).thenReturn(Optional.of(userList));
//        when(userMapper.map(userList)).thenReturn(userDtoList);
//
//        List<UserDto> returnedUsersDto = userService.getAllUsers(requesterId);
//
//        assertAll(
//                () -> assertEquals(userDtoList, returnedUsersDto),
//                () -> verify(userRepository).findAllByRoleInAndIsActive(roles),
//                () -> verify(userMapper).map(userList)
//        );
//    }
//
//    @Test
//    void getAllUsers_whenAdminGetAllUsers_returnAllUsersThanBoss() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        Roles userRole1 = Roles.ROLE_ADMIN;
//        Roles userRole2 = Roles.ROLE_BOSS;
//        Roles userRole3 = Roles.ROLE_FINANCIAL;
//        List<Roles> roles = Arrays.asList(Roles.values()).subList(requesterRole.ordinal(), Roles.values().length);
//        requester.setRole(requesterRole);
//
//        User user1 = new User(userId + 1, "1" + userName, "1" + email, userRole1);
//        User user2 = new User(userId + 2, "2" + userName, "2" + email, userRole2);
//        User user3 = new User(userId + 3, "3" + userName, "3" + email, userRole3);
//        List<User> userList = List.of(user1, user3);
//        UserDto userDto1 = new UserDto(userId + 1, "1" + userName, "1" + email, userRole1);
//        UserDto userDto2 = new UserDto(userId + 2, "2" + userName, "2" + email, userRole2);
//        UserDto userDto3 = new UserDto(userId + 3, "3" + userName, "3" + email, userRole3);
//        List<UserDto> userDtoList = List.of(userDto1, userDto3);
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findAllByRoleInAndIsActive(roles)).thenReturn(Optional.of(userList));
//        when(userMapper.map(userList)).thenReturn(userDtoList);
//
//        List<UserDto> returnedUsersDto = userService.getAllUsers(requesterId);
//
//        assertAll(
//                () -> assertEquals(userDtoList, returnedUsersDto),
//                () -> verify(userRepository).findAllByRoleInAndIsActive(roles),
//                () -> verify(userMapper).map(userList)
//        );
//    }
//
//    @Test
//    void getAllUsers_whenUserGetAllUsers_ThenAccessDeniedException() {
//        Roles requesterRole = Roles.ROLE_USER;
//        requester.setRole(requesterRole);
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//
//        assertThrows(AccessDeniedException.class,
//                () -> userService.getAllUsers(requesterId));
//    }
//
//    @Test
//    void getAllUsers_whenFinancialGetAllUsers_ThenAccessDeniedException() {
//        Roles requesterRole = Roles.ROLE_FINANCIAL;
//        requester.setRole(requesterRole);
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//
//        assertThrows(AccessDeniedException.class,
//                () -> userService.getAllUsers(requesterId));
//    }
//
//    @Test
//    void deleteUserById_whenRequesterBoss_thenUserWillDelete() {
//        Roles requesterRole = Roles.ROLE_BOSS;
//        Roles userRole = Roles.ROLE_ADMIN;
//        requester.setRole(requesterRole);
//
//        User user = User.builder()
//                .id(userId)
//                .name(userName)
//                .email(email)
//                .role(userRole)
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        userService.deleteUserById(requesterId, userId);
//
//        verify(userRepository).deleteById(longArgumentCaptor.capture());
//        Long idForDelete = longArgumentCaptor.getValue();
//        assertEquals(userId, idForDelete);
//    }
//
//    @Test
//    void deleteUserById_whenAdminDeleteUser_thenUserWillDelete() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        Roles userRole = Roles.ROLE_USER;
//        requester.setRole(requesterRole);
//
//        User user = User.builder()
//                .id(userId)
//                .name(userName)
//                .email(email)
//                .role(userRole)
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        userService.deleteUserById(requesterId, userId);
//
//        verify(userRepository).deleteById(longArgumentCaptor.capture());
//        Long idForDelete = longArgumentCaptor.getValue();
//        assertEquals(userId, idForDelete);
//    }
//
//    @Test
//    void deleteUserById_whenAdminDeleteBoss_thenAccessDenied() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        Roles userRole = Roles.ROLE_BOSS;
//        requester.setRole(requesterRole);
//
//        User user = User.builder()
//                .id(userId)
//                .name(userName)
//                .email(email)
//                .role(userRole)
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        assertThrows(AccessDeniedException.class,
//                () -> userService.deleteUserById(requesterId, userId));
//    }
//
//    @Test
//    void deleteUserById_whenUserDeleteSmb_thenAccessDenied() {
//        Roles requesterRole = Roles.ROLE_USER;
//        Roles userRole = Roles.ROLE_FINANCIAL;
//        requester.setRole(requesterRole);
//
//        User user = User.builder()
//                .id(userId)
//                .name(userName)
//                .email(email)
//                .role(userRole)
//                .build();
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        assertThrows(AccessDeniedException.class,
//                () -> userService.deleteUserById(requesterId, userId));
//    }
}