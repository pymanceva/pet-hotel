package ru.dogudacha.PetHotel.user.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dogudacha.PetHotel.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
