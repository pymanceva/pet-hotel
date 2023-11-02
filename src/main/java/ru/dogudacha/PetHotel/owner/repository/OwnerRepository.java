package ru.dogudacha.PetHotel.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dogudacha.PetHotel.owner.model.Owner;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
//    Optional<List<User>> findAllByRoleIn(List<Roles> roles);
}
