package ru.modgy.booking.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.modgy.booking.dto.BookingDto;
import ru.modgy.booking.dto.NewBookingDto;
import ru.modgy.booking.dto.UpdateBookingDto;
import ru.modgy.booking.model.Booking;
import ru.modgy.pet.mapper.PetMapper;
import ru.modgy.room.dto.mapper.RoomMapper;

import java.time.LocalDate;
import java.time.Period;

@Mapper(componentModel = "spring", uses = {RoomMapper.class, PetMapper.class})
public interface BookingMapper {
    @Mapping(target = "daysOfBooking",
            expression = "java(calculateBookingDays(booking.getCheckInDate(), " +
                    "booking.getCheckOutDate()))")
    BookingDto toBookingDto(Booking booking);

    default Integer calculateBookingDays(LocalDate checkInDate, LocalDate checkOutDate) {
        return Period.between(checkInDate, checkOutDate).plusDays(1).getDays();
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
}
