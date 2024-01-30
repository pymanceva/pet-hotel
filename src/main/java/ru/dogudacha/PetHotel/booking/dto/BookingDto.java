package ru.dogudacha.PetHotel.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dogudacha.PetHotel.booking.model.ReasonOfStopBooking;
import ru.dogudacha.PetHotel.booking.model.StatusBooking;
import ru.dogudacha.PetHotel.booking.model.TypesBooking;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.room.dto.RoomDto;

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
