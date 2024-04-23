package ru.modgy.booking.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.modgy.booking.dto.BookingDto;
import ru.modgy.booking.dto.NewBookingDto;
import ru.modgy.booking.dto.UpdateBookingDto;
import ru.modgy.booking.model.Booking;
import ru.modgy.booking.model.StatusBooking;
import ru.modgy.booking.model.TypesBooking;
import ru.modgy.exception.NotFoundException;
import ru.modgy.pet.dto.PetDto;
import ru.modgy.pet.model.Pet;
import ru.modgy.pet.model.Sex;
import ru.modgy.pet.model.TypeOfPet;
import ru.modgy.room.category.dto.CategoryDto;
import ru.modgy.room.category.model.Category;
import ru.modgy.room.dto.RoomDto;
import ru.modgy.room.model.Room;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@ActiveProfiles("test")
public class BookingServiceIntegrationTest {
    private final User requesterAdmin = User.builder()
            .email("admin@mail.ru")
            .firstName("admin")
            .role(Roles.ROLE_ADMIN)
            .isActive(true)
            .build();
    private final Category category = Category.builder()
            .name("Dog room")
            .description("Room for dogs")
            .build();
    private final Room room = Room.builder()
            .area(5.0)
            .number("standard room")
            .category(category)
            .isVisible(true)
            .build();
    private final Pet pet = Pet.builder()
            .type(TypeOfPet.DOG)
            .name("Шарик")
            .breed("Spaniel")
            .birthDate(LocalDate.of(2023, 1, 1))
            .sex(Sex.FEMALE)
            .build();
    private final Booking booking = Booking.builder()
            .type(TypesBooking.TYPE_BOOKING)
            .checkInDate(LocalDate.now())
            .checkOutDate(LocalDate.now().plusDays(7))
            .status(StatusBooking.STATUS_INITIAL)
            .price(0.0)
            .amount(0.0)
            .prepaymentAmount(0.0)
            .isPrepaid(false)
            .room(room)
            .pets(List.of(pet))
            .build();
    private final RoomDto roomDto = RoomDto.builder()
            .id(1L)
            .area(5.0)
            .number("standard room")
            .categoryDto(new CategoryDto(1L, "name", "description"))
            .isVisible(true)
            .build();
    private final PetDto petDto = PetDto.builder()
            .id(1L)
            .type(TypeOfPet.DOG)
            .name("Шарик")
            .breed("Spaniel")
            .birthDate(LocalDate.of(2023, 1, 1))
            .sex(Sex.FEMALE)
            .build();
    private final NewBookingDto newBookingDto = NewBookingDto.builder()
            .type(TypesBooking.TYPE_BOOKING)
            .roomId(1L)
            .checkInDate(LocalDate.now())
            .checkOutDate(LocalDate.now().plusDays(7))
            .petIds(List.of(1L))
            .build();
    private final BookingDto bookingDto = BookingDto.builder()
            .type(TypesBooking.TYPE_BOOKING)
            .checkInDate(LocalDate.now())
            .checkOutDate(LocalDate.now().plusDays(7))
            .daysOfBooking(8)
            .status(StatusBooking.STATUS_INITIAL)
            .price(0.0)
            .amount(0.0)
            .prepaymentAmount(0.0)
            .isPrepaid(false)
            .room(roomDto)
            .pets(List.of(petDto))
            .build();
    private final UpdateBookingDto updateBookingDto = UpdateBookingDto.builder()
            .price(1000.00)
            .amount(7000.00)
            .prepaymentAmount(1000.00)
            .isPrepaid(true)
            .build();
    private final EntityManager em;
    private final BookingService service;

    @Test
    void addBooking() {
        em.persist(requesterAdmin);
        em.persist(category);
        em.persist(room);
        em.persist(pet);
        newBookingDto.setRoomId(room.getId());
        newBookingDto.setPetIds(List.of(pet.getId()));

        BookingDto result = service.addBooking(requesterAdmin.getId(), newBookingDto);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getType(), equalTo(bookingDto.getType()));
        assertThat(result.getCheckInDate(), equalTo(bookingDto.getCheckInDate()));
        assertThat(result.getCheckOutDate(), equalTo(bookingDto.getCheckOutDate()));
        assertThat(result.getDaysOfBooking(), equalTo(bookingDto.getDaysOfBooking()));
        assertThat(result.getStatus(), equalTo(bookingDto.getStatus()));
        assertThat(result.getPrice(), equalTo(bookingDto.getPrice()));
        assertThat(result.getAmount(), equalTo(bookingDto.getAmount()));
        assertThat(result.getPrepaymentAmount(), equalTo(bookingDto.getPrepaymentAmount()));
        assertThat(result.getIsPrepaid(), equalTo(bookingDto.getIsPrepaid()));
        assertThat(result.getRoom().getNumber(), equalTo(bookingDto.getRoom().getNumber()));
        assertThat(result.getPets().size(), equalTo(1));
    }

    @Test
    void getBookingById() {
        em.persist(requesterAdmin);
        em.persist(booking);

        BookingDto result = service.getBookingById(requesterAdmin.getId(), booking.getId());

        assertThat(result.getId(), notNullValue());
        assertThat(result.getType(), equalTo(bookingDto.getType()));
        assertThat(result.getCheckInDate(), equalTo(bookingDto.getCheckInDate()));
        assertThat(result.getCheckOutDate(), equalTo(bookingDto.getCheckOutDate()));
        assertThat(result.getDaysOfBooking(), equalTo(bookingDto.getDaysOfBooking()));
        assertThat(result.getStatus(), equalTo(bookingDto.getStatus()));
        assertThat(result.getPrice(), equalTo(bookingDto.getPrice()));
        assertThat(result.getAmount(), equalTo(bookingDto.getAmount()));
        assertThat(result.getPrepaymentAmount(), equalTo(bookingDto.getPrepaymentAmount()));
        assertThat(result.getIsPrepaid(), equalTo(bookingDto.getIsPrepaid()));
        assertThat(result.getRoom().getNumber(), equalTo(bookingDto.getRoom().getNumber()));
        assertThat(result.getPets().size(), equalTo(1));
    }

    @Test
    void updateBooking() {
        List<Pet> petList = new ArrayList<>();
        petList.add(pet);
        booking.setPets(petList);

        em.persist(requesterAdmin);
        em.persist(category);
        em.persist(room);
        em.persist(pet);
        em.persist(booking);

        BookingDto result = service.updateBooking(requesterAdmin.getId(), booking.getId(), updateBookingDto);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getType(), equalTo(bookingDto.getType()));
        assertThat(result.getCheckInDate(), equalTo(bookingDto.getCheckInDate()));
        assertThat(result.getCheckOutDate(), equalTo(bookingDto.getCheckOutDate()));
        assertThat(result.getDaysOfBooking(), equalTo(bookingDto.getDaysOfBooking()));
        assertThat(result.getStatus(), equalTo(StatusBooking.STATUS_CONFIRMED));
        assertThat(result.getPrice(), equalTo(updateBookingDto.getPrice()));
        assertThat(result.getAmount(), equalTo(updateBookingDto.getAmount()));
        assertThat(result.getPrepaymentAmount(), equalTo(updateBookingDto.getPrepaymentAmount()));
        assertThat(result.getIsPrepaid(), equalTo(updateBookingDto.getIsPrepaid()));
        assertThat(result.getRoom().getNumber(), equalTo(bookingDto.getRoom().getNumber()));
        assertThat(result.getPets().size(), equalTo(1));
    }

    @Test
    void deleteBookingById() {
        em.persist(requesterAdmin);
        em.persist(category);
        em.persist(room);
        em.persist(pet);
        em.persist(booking);

        service.deleteBookingById(requesterAdmin.getId(), booking.getId());

        String error = String.format("booking with id=%d is not found", booking.getId());
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.getBookingById(requesterAdmin.getId(), booking.getId())
        );

        assertEquals(error, exception.getMessage());
    }
}
