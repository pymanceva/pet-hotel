package ru.dogudacha.PetHotel.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dogudacha.PetHotel.user.dto.mapper.IdToUser;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<List<User>> findAllByRoleIn(List<Roles> roles);
//    @IdToUser
//    User getById(Long id);
}
