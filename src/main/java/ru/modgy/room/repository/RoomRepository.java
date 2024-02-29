package ru.modgy.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.modgy.room.model.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r " +
            "WHERE (r.isVisible = :isVisible)")
    Optional<List<Room>> getAllRooms(@Param("isVisible") Boolean isVisible);

    @Query("SELECT r FROM Room r WHERE r.category.id = :categoryId AND " +
            "r.id NOT IN (SELECT b.room.id FROM Booking b WHERE b.room.category.id = :categoryId AND " +
            "((b.checkInDate <= :checkInDate AND b.checkOutDate >= :checkInDate) OR " +
            "(b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkOutDate) OR " +
            "(b.checkInDate >= :checkInDate AND b.checkOutDate <= :checkOutDate)))")
    Optional<List<Room>> findAvailableRoomsByCategoryInDates(@Param("categoryId") Long categoryId,
                                                    @Param("checkInDate") LocalDate checkInDate,
                                                    @Param("checkOutDate") LocalDate checkOutDate);

    Integer deleteRoomById(Long id);
}
