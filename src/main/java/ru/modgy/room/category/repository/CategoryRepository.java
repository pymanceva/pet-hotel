package ru.modgy.room.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.modgy.room.category.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    int deleteCategoryById(Long id);
    int countAllByName(String name);
    @Query("SELECT c FROM Category c " +
            "ORDER BY c.name")
    Optional<List<Category>> findAllOrderByNameAsc();
}
