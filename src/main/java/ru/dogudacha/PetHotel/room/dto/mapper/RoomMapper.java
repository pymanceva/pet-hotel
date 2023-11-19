package ru.dogudacha.PetHotel.room.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.dogudacha.PetHotel.room.dto.RoomDto;
import ru.dogudacha.PetHotel.room.dto.UpdateRoomDto;
import ru.dogudacha.PetHotel.room.model.Room;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomDto toRoomDto(Room room);

    @Mapping(target = "id", ignore = true)
    Room toRoom(RoomDto roomDto);

    @Mapping(target = "id", ignore = true)
    Room toRoom(UpdateRoomDto roomDto);

    List<RoomDto> toListRoomDto(List<Room> rooms);
}
