package ru.modgy.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<List<User>> findAllByRoleInAndIsActive(List<Roles> roles, Boolean isActive);

    Optional<List<User>> findAllByRoleIn(List<Roles> roles);

    Integer deleteUserById(Long userId);
}
