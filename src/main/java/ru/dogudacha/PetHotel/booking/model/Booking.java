package ru.dogudacha.PetHotel.booking.model;

import jakarta.persistence.*;
import lombok.*;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.room.model.Room;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "bookings")
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bookings")
    private Long id;
    @Column(name = "type_bookings")
    @Enumerated(EnumType.STRING)
    private TypesBooking type;
    @Column(name = "check_in_date_bookings")
    private LocalDate checkInDate;
    @Column(name = "check_out_date_bookings")
    private LocalDate checkOutDate;
    @Column(name = "check_in_time_bookings")
    private LocalTime checkInTime;
    @Column(name = "check_out_time_bookings")
    private LocalTime checkOutTime;
    @Column(name = "status_bookings")
    @Enumerated(EnumType.STRING)
    private StatusBooking status;
    @Column(name = "reason_of_stop_bookings")
    @Enumerated(EnumType.STRING)
    private ReasonOfStopBooking reasonOfStop;
    @Column(name = "reason_of_cancel_bookings")
    private String reasonOfCancel;
    @Column(name = "price_bookings")
    private Double price;
    @Column(name = "amount_bookings")
    private Double amount;
    @Column(name = "prepayment_amount_bookings")
    private Double prepaymentAmount;
    @Column(name = "made_prepayment_bookings")
    private Boolean isPrepaid;
    @Column(name = "comment_bookings")
    private String comment;
    @Column(name = "file_bookings")
    private String fileUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id_bookings")
    @ToString.Exclude
    private Room room;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pets_in_bookings",
            joinColumns = @JoinColumn(name = "id_bookings"),
            inverseJoinColumns = @JoinColumn(name = "id_pets")
    )
    private Set<Pet> pets;
}
