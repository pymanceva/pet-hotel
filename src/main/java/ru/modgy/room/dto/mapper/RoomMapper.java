package ru.modgy.room.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.modgy.room.category.dto.mapper.CategoryMapper;
import ru.modgy.room.category.service.CategoryServiceImpl;
import ru.modgy.room.dto.NewRoomDto;
import ru.modgy.room.dto.RoomDto;
import ru.modgy.room.dto.UpdateRoomDto;
import ru.modgy.room.model.Room;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, CategoryServiceImpl.class})
public interface RoomMapper {
    @Mapping(target = "categoryDto", source = "category")
    RoomDto toRoomDto(Room room);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "categoryDto")
    Room toRoom(RoomDto roomDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Room toRoom(NewRoomDto newRoomDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "isVisible", ignore = true)
    Room toRoom(UpdateRoomDto roomDto);

    List<RoomDto> toListRoomDto(List<Room> rooms);
}
