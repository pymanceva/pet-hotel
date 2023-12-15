package ru.dogudacha.PetHotel.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dogudacha.PetHotel.owner.model.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
