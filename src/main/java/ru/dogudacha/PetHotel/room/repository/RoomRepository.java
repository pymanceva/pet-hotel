package ru.dogudacha.PetHotel.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dogudacha.PetHotel.room.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query(value = "select r from Room r")
    Optional<List<Room>> getAllRooms();

    Integer deleteRoomById(Long id);
}
