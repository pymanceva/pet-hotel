package ru.dogudacha.PetHotel.user.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dogudacha.PetHotel.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select u from User u")
    Optional<List<User>> getAllUsers();
}
