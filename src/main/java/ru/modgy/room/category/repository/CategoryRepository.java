package ru.modgy.room.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.modgy.room.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    int deleteCategoryById(Long id);
    int countAllByName(String name);
}
