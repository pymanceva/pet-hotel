package ru.modgy.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.modgy.booking.model.ReasonOfStopBooking;
import ru.modgy.booking.model.TypesBooking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewBookingDto {
    @NotNull(message = "Field: type. Error: must not be null.")
    private TypesBooking type;
    @NotNull(message = "Field: checkInDate. Error: must not be null.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate checkInDate;
    @NotNull(message = "Field: checkOutDate. Error: must not be null.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate checkOutDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime checkInTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime checkOutTime;
    private ReasonOfStopBooking reasonOfStop;
    @Size(max = 150)
    private String reasonOfCancel;
    @Min(0)
    @Max(999999)
    private Double price;
    @Min(0)
    @Max(999999)
    private Double amount;
    @Min(0)
    @Max(999999)
    private Double prepaymentAmount;
    private Boolean isPrepaid;
    @Size(max = 150)
    private String comment;
    private String fileUrl;
    @NotNull
    private Long roomId;
    private List<Long> petIds;
}
