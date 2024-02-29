package ru.modgy.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.modgy.booking.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Integer deleteBookingById(Long id);

    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId " +
            "AND b.status <> 'STATUS_CANCELLED' "+
            "AND ((b.checkInDate < :checkInDate AND b.checkOutDate > :checkInDate) OR " +
            "(b.checkInDate >= :checkInDate AND b.checkInDate < :checkOutDate) OR " +
            "(b.checkInDate = :checkInDate AND b.checkInDate = :checkOutDate) OR " +
            "(b.checkOutDate = :checkInDate AND b.checkOutDate = :checkOutDate) OR " +
            "(b.checkInDate = :checkOutDate AND b.checkOutDate = :checkOutDate) OR " +
            "(b.checkInDate = :checkInDate AND b.checkOutDate = :checkInDate))")
    Optional<List<Booking>> findBookingsForRoomInDates(@Param("roomId") Long roomId,
                                                       @Param("checkInDate") LocalDate checkInDate,
                                                       @Param("checkOutDate") LocalDate checkOutDate);
}
