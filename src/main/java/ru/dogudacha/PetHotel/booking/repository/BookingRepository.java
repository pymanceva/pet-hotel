package ru.dogudacha.PetHotel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dogudacha.PetHotel.booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Integer deleteBookingById(Long id);
}
