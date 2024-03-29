package ru.modgy.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.modgy.room.category.dto.CategoryDto;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private Long id;
    private String number;
    private Double area;
    private CategoryDto categoryDto;
    private String description;
    private Boolean isVisible;
}
