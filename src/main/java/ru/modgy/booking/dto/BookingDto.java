package ru.modgy.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.modgy.booking.model.ReasonOfStopBooking;
import ru.modgy.booking.model.StatusBooking;
import ru.modgy.booking.model.TypesBooking;
import ru.modgy.pet.dto.PetDto;
import ru.modgy.room.dto.RoomDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private TypesBooking type;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Integer daysOfBooking;
    private StatusBooking status;
    private ReasonOfStopBooking reasonOfStop;
    private String reasonOfCancel;
    private Double price;
    private Double amount;
    private Double prepaymentAmount;
    private Boolean isPrepaid;
    private String comment;
    private String fileUrl;
    private RoomDto room;
    private List<PetDto> pets;
}
