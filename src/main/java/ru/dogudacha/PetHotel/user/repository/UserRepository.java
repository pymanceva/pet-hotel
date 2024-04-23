package ru.dogudacha.PetHotel.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<List<User>> findAllByRoleInAndIsActive(List<Roles> roles, Boolean isActive);

    Optional<List<User>> findAllByRoleIn(List<Roles> roles);

    Integer deleteUserById(Long userId);
}
