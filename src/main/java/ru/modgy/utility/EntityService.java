package ru.modgy.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.modgy.booking.model.Booking;
import ru.modgy.booking.repository.BookingRepository;
import ru.modgy.exception.ConflictException;
import ru.modgy.exception.NotFoundException;
import ru.modgy.owner.model.Owner;
import ru.modgy.owner.repository.OwnerRepository;
import ru.modgy.pet.model.Pet;
import ru.modgy.pet.repository.PetRepository;
import ru.modgy.room.category.model.Category;
import ru.modgy.room.category.repository.CategoryRepository;
import ru.modgy.room.model.Room;
import ru.modgy.room.repository.RoomRepository;
import ru.modgy.user.model.User;
import ru.modgy.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EntityService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RoomRepository roomRepository;
    private final PetRepository petRepository;
    private final BookingRepository bookingRepository;
    private final OwnerRepository ownerRepository;

    public User getUserIfExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d is not found", userId)));
    }

    public Owner getOwnerIfExists(Long ownerId) {
        return ownerRepository.findById(ownerId).orElseThrow(() ->
                new NotFoundException(String.format("Owner with id=%d is not found", ownerId)));
    }

    public Category getCategoryIfExists(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Category with id=%d is not found", id)));
    }

    public Room getRoomIfExists(Long id) {
        return roomRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Room with id=%d is not found", id)));
    }

    public Pet getPetIfExists(Long petId) {
//        return petRepository.findById(petId).orElseThrow(() ->
        return petRepository.findById(petId).orElseThrow(() ->
                new NotFoundException(String.format("Pet with id=%d is not found", petId)));
    }

    public Booking getBookingIfExists(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format("Booking with id=%d is not found", bookingId)));
    }

    public List<Pet> getListOfPetsByIds(List<Long> petIds) {
        return petRepository.findAllByIdIn(petIds)
                .orElseThrow(() -> new ConflictException("At least one id should be in list"));
    }
}
