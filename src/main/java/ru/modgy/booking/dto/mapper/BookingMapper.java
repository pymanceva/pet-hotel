package ru.modgy.booking.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.modgy.booking.dto.BookingDto;
import ru.modgy.booking.dto.NewBookingDto;
import ru.modgy.booking.dto.UpdateBookingDto;
import ru.modgy.booking.model.Booking;
import ru.modgy.pet.mapper.PetMapper;
import ru.modgy.room.category.dto.mapper.CategoryMapper;
import ru.modgy.room.dto.mapper.RoomMapper;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = {RoomMapper.class, CategoryMapper.class, PetMapper.class})
public interface BookingMapper {
    @Mapping(target = "daysOfBooking",
            expression = "java(calculateBookingDays(booking.getCheckInDate(), " +
                    "booking.getCheckOutDate()))")
    BookingDto toBookingDto(Booking booking);

    default Long calculateBookingDays(LocalDate checkInDate, LocalDate checkOutDate) {
        LocalDateTime start = checkInDate.atTime(LocalTime.MIN);
        LocalDateTime end = checkOutDate.atTime(LocalTime.MAX);
        return Duration.between(start, end).toDays() + 1;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "pets", ignore = true)
    @Mapping(source = "newBookingDto.isPrepaid", target = "isPrepaid", defaultValue = "false")
    @Mapping(source = "newBookingDto.price", target = "price", defaultValue = "0")
    @Mapping(source = "newBookingDto.amount", target = "amount", defaultValue = "0")
    @Mapping(source = "newBookingDto.prepaymentAmount", target = "prepaymentAmount", defaultValue = "0")
    Booking toBooking(NewBookingDto newBookingDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "pets", ignore = true)
    Booking toBooking(UpdateBookingDto updateBookingDto);

    List<BookingDto> toBookingDto(List<Booking> bookings);
}
