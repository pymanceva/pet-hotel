package ru.dogudacha.PetHotel.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.dogudacha.PetHotel.room.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r " +
            "WHERE (r.isVisible = :isVisible)")
    Optional<List<Room>> getAllRooms(@Param("isVisible") Boolean isVisible);

    Integer deleteRoomById(Long id);
}
