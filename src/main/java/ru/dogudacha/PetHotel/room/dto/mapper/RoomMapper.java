package ru.dogudacha.PetHotel.room.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.dogudacha.PetHotel.room.category.dto.mapper.CategoryMapper;
import ru.dogudacha.PetHotel.room.category.service.CategoryServiceImpl;
import ru.dogudacha.PetHotel.room.dto.NewRoomDto;
import ru.dogudacha.PetHotel.room.dto.RoomDto;
import ru.dogudacha.PetHotel.room.dto.UpdateRoomDto;
import ru.dogudacha.PetHotel.room.model.Room;

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
