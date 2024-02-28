package ru.modgy.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.modgy.booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Integer deleteBookingById(Long id);
}
