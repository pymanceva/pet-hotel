package ru.dogudacha.PetHotel.room.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dogudacha.PetHotel.room.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Integer deleteCategoryById(Long id);
}
