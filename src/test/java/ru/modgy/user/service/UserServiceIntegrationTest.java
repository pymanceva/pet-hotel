package ru.modgy.user.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.modgy.user.dto.NewUserDto;
import ru.modgy.user.dto.UpdateUserDto;
import ru.modgy.user.dto.UserDto;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserServiceIntegrationTest {
    private final EntityManager em;
    private final UserService service;

    final User requesterAdmin = User.builder()
            .lastName("Кружкин")
            .firstName("admin")
            .middleName("Петрович")
            .email("admin@mail.ru")
            .password("admin_pwd")
            .role(Roles.ROLE_ADMIN)
            .isActive(true)
            .build();

    final String userFirstName = "newFirstName";
    final String userMiddleName = "newMiddleName";
    final String userLastName = "newLastName";
    final String userEmail = "newUser@mail.com";
    final String userPassword = "user_pwd";
    final Roles userRole = Roles.ROLE_USER;
    final boolean isActive = true;

    User user = User.builder()
            .firstName(userFirstName)
            .middleName(userMiddleName)
            .lastName(userLastName)
            .email(userEmail)
            .password(userPassword)
            .role(userRole)
            .isActive(isActive)
            .build();
    Roles userRole1 = Roles.ROLE_ADMIN;
    Roles userRole2 = Roles.ROLE_FINANCIAL;
    Roles userRole3 = Roles.ROLE_USER;
    User user1 = User.builder()
            .firstName("1" + user.getFirstName()).email("1" + user.getEmail()).role(userRole1).isActive(true)
            .build();
    User user2 = User.builder()
            .firstName("2" + user.getFirstName()).email("2" + user.getEmail()).role(userRole2).isActive(true)
            .build();
    User user3 = User.builder()
            .firstName("3" + user.getFirstName()).email("3" + user.getEmail()).role(userRole3).isActive(true)
            .build();
    NewUserDto newUserDto = new NewUserDto(userLastName, userFirstName, userMiddleName, userPassword, userEmail,
            userRole, isActive);

    UpdateUserDto updateUserDto = UpdateUserDto.builder()
            .firstName(userFirstName + " upd")
            .middleName(userMiddleName + " upd")
            .lastName(userLastName + " upd")
            .email(userEmail + " upd")
            .password(userPassword + " upd")
            .role(Roles.ROLE_FINANCIAL)
            .build();

    @Test
    void addUser() {
        em.persist(requesterAdmin);
        UserDto actualUser = service.addUser(requesterAdmin.getId(), newUserDto);

        assertAll(
                () -> assertNotNull(actualUser.getId()),
                () -> assertEquals(actualUser.getFirstName(), newUserDto.getFirstName()),
                () -> assertEquals(actualUser.getLastName(), newUserDto.getLastName()),
                () -> assertEquals(actualUser.getMiddleName(), newUserDto.getMiddleName()),
                () -> assertEquals(actualUser.getEmail(), newUserDto.getEmail()),
                () -> assertEquals(actualUser.getPassword(), newUserDto.getPassword()),
                () -> assertEquals(actualUser.getRole(), newUserDto.getRole()),
                () -> assertEquals(actualUser.getIsActive(), newUserDto.getIsActive())
        );
    }

    @Test
    void getUserById() {
        em.persist(requesterAdmin);
        em.persist(user);

        UserDto actualUser = service.getUserById(requesterAdmin.getId(), user.getId());

        assertAll(
                () -> assertNotNull(actualUser.getId()),
                () -> assertEquals(actualUser.getFirstName(), user.getFirstName()),
                () -> assertEquals(actualUser.getLastName(), user.getLastName()),
                () -> assertEquals(actualUser.getMiddleName(), user.getMiddleName()),
                () -> assertEquals(actualUser.getEmail(), user.getEmail()),
                () -> assertEquals(actualUser.getPassword(), user.getPassword()),
                () -> assertEquals(actualUser.getRole(), user.getRole()),
                () -> assertEquals(actualUser.getIsActive(), user.getIsActive())
        );
    }

    @Test
    void updateUser() {
        em.persist(requesterAdmin);
        em.persist(user);
        UserDto actualUser = service.updateUser(requesterAdmin.getId(), user.getId(), updateUserDto);

        assertAll(
                () -> assertNotNull(actualUser.getId()),
                () -> assertEquals(actualUser.getFirstName(), updateUserDto.getFirstName()),
                () -> assertEquals(actualUser.getLastName(), updateUserDto.getLastName()),
                () -> assertEquals(actualUser.getMiddleName(), updateUserDto.getMiddleName()),
                () -> assertEquals(actualUser.getEmail(), updateUserDto.getEmail()),
                () -> assertEquals(actualUser.getPassword(), updateUserDto.getPassword()),
                () -> assertEquals(actualUser.getRole(), updateUserDto.getRole()),
                () -> assertEquals(actualUser.getIsActive(), user.getIsActive())
        );
    }

    @Test
    void getAllUsers() {
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);

        UserDto userDto1 = UserDto.builder()
                .id(user1.getId()).firstName(user1.getFirstName()).email(user1.getEmail()).role(userRole1)
                .isActive(true)
                .build();

        UserDto userDto2 = UserDto.builder()
                .id(user2.getId()).firstName(user2.getFirstName()).email(user2.getEmail()).role(userRole2)
                .isActive(true)
                .build();

        UserDto userDto3 = UserDto.builder()
                .id(user3.getId()).firstName(user3.getFirstName()).email(user3.getEmail()).role(userRole3)
                .isActive(true)
                .build();

        Collection<UserDto> allUsers = service.getAllUsers(user1.getId(), true);

        assertAll(
                () -> assertEquals(3, allUsers.size()),
                () -> assertThat(allUsers, hasItem(userDto1)),
                () -> assertThat(allUsers, hasItem(userDto2)),
                () -> assertThat(allUsers, hasItem(userDto3))
        );
    }

    @Test
    void deleteUserById() {
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);

        UserDto userDto1 = UserDto.builder()
                .id(user1.getId()).firstName(user1.getFirstName()).email(user1.getEmail()).role(userRole1)
                .isActive(true)
                .build();

        UserDto userDto2 = UserDto.builder()
                .id(user2.getId()).firstName(user2.getFirstName()).email(user2.getEmail()).role(userRole2)
                .isActive(true)
                .build();

        UserDto userDto3 = UserDto.builder()
                .id(user3.getId()).firstName(user3.getFirstName()).email(user3.getEmail()).role(userRole3)
                .isActive(true)
                .build();

        Collection<UserDto> allUsers = service.getAllUsers(user1.getId(), true);

        assertAll(
                () -> assertEquals(3, allUsers.size()),
                () -> assertThat(allUsers, hasItem(userDto1)),
                () -> assertThat(allUsers, hasItem(userDto2)),
                () -> assertThat(allUsers, hasItem(userDto3))
        );

        service.deleteUserById(user1.getId(), user2.getId());

        Collection<UserDto> allUsersAfterDellete = service.getAllUsers(user1.getId(), true);

        assertAll(
                () -> assertEquals(2, allUsersAfterDellete.size()),
                () -> assertThat(allUsersAfterDellete, hasItem(userDto1)),
                () -> assertThat(allUsersAfterDellete, hasItem(userDto3))
        );
    }

    @Test
    void setUserState() {
        em.persist(requesterAdmin);
        em.persist(user);
        boolean falseActive = false;

        UserDto controlUser = service.getUserById(requesterAdmin.getId(), user.getId());

        assertTrue(user.getIsActive());

        UserDto resultUserDto1 = service.setUserState(requesterAdmin.getId(), user.getId(), falseActive);

        assertAll(
                () -> assertEquals(controlUser.getId(), resultUserDto1.getId()),
                () -> assertEquals(controlUser.getFirstName(), resultUserDto1.getFirstName()),
                () -> assertEquals(controlUser.getLastName(), resultUserDto1.getLastName()),
                () -> assertEquals(controlUser.getMiddleName(), resultUserDto1.getMiddleName()),
                () -> assertEquals(controlUser.getEmail(), resultUserDto1.getEmail()),
                () -> assertEquals(controlUser.getPassword(), resultUserDto1.getPassword()),
                () -> assertEquals(controlUser.getRole(), resultUserDto1.getRole()),
                () -> assertEquals(resultUserDto1.getIsActive(), falseActive)
        );

        boolean trueActive = true;

         UserDto resultUserDto2 = service.setUserState(requesterAdmin.getId(), user.getId(), trueActive);

        assertAll(
                () -> assertEquals(controlUser.getId(), resultUserDto2.getId()),
                () -> assertEquals(controlUser.getFirstName(), resultUserDto2.getFirstName()),
                () -> assertEquals(controlUser.getLastName(), resultUserDto2.getLastName()),
                () -> assertEquals(controlUser.getMiddleName(), resultUserDto2.getMiddleName()),
                () -> assertEquals(controlUser.getEmail(), resultUserDto2.getEmail()),
                () -> assertEquals(controlUser.getPassword(), resultUserDto2.getPassword()),
                () -> assertEquals(controlUser.getRole(), resultUserDto2.getRole()),
                () -> assertEquals(resultUserDto2.getIsActive(), trueActive)
        );
    }
}
