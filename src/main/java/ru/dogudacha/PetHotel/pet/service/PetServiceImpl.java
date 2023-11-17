package ru.dogudacha.PetHotel.pet.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.dto.PetForAdminDto;
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;
import ru.dogudacha.PetHotel.pet.mapper.PetMapper;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.pet.repository.PetRepository;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetMapper petMapper;

    @Override
    @Transactional
    public PetDto addPet(Long requesterId, NewPetDto newPetDto) {
        User requester = findUserById(requesterId);
        checkAccess(requester);
        Pet newPet = petMapper.toPet(newPetDto);
        Pet savedPet = petRepository.save(newPet);
        log.info("PetService: addPet, requesterId={}, petId={}", requesterId, savedPet.getId());
        return petMapper.toPetDto(savedPet);
    }

    @Override
    @Transactional(readOnly = true)
    public PetDto getPetByIdForUser(Long requesterId, Long petId) {
        findUserById(requesterId);
        Pet pet = getPetIfExists(petId);
        log.info("PetService: getPetById, requesterId={}, petId={}", requesterId, petId);
        return petMapper.toPetDto(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public PetForAdminDto getPetByIdForAdmin(Long requesterId, Long petId) {
        User requester = findUserById(requesterId);
        checkAccess(requester);
        Pet pet = getPetIfExists(petId);
        log.info("PetService: getPetById, requesterId={}, petId={}", requesterId, petId);
        return petMapper.toPetForAdminDto(pet);
    }

    @Override
    @Transactional
    public PetDto updatePet(Long requesterId, Long petId, UpdatePetDto updatePetDto) {
        User requester = findUserById(requesterId);
        checkAccess(requester);
        Pet pet = getPetIfExists(petId);
        if (Objects.nonNull(updatePetDto.getTypeOfPet()) && !updatePetDto.getTypeOfPet().isBlank()) {
            pet.setTypeOfPet(updatePetDto.getTypeOfPet());
        }
        if (Objects.nonNull(updatePetDto.getBreed()) && !updatePetDto.getBreed().isBlank()) {
            pet.setBreed(updatePetDto.getBreed());
        }
        if (Objects.nonNull(updatePetDto.getSex())) {
            pet.setSex(updatePetDto.getSex());
        }
        if (Objects.nonNull(updatePetDto.getAge())) {
            pet.setAge(updatePetDto.getAge());
        }
        if (Objects.nonNull(updatePetDto.getWeight())) {
            pet.setWeight(updatePetDto.getWeight());
        }
        if (Objects.nonNull(updatePetDto.getDiet())) {
            pet.setDiet(updatePetDto.getDiet());
        }
        if (Objects.nonNull(updatePetDto.getIsTakesMedications())) {
            pet.setIsTakesMedications(updatePetDto.getIsTakesMedications());
        }
        if (Objects.nonNull(updatePetDto.getIsContact())) {
            pet.setIsContact(updatePetDto.getIsContact());
        }
        if (Objects.nonNull(updatePetDto.getIsPhotographed())) {
            pet.setIsPhotographed(updatePetDto.getIsPhotographed());
        }
        Pet savedPet = petRepository.save(pet);
        log.info("PetService: updatePet, requesterId={}, petId={}, updatePetDto={}", requesterId, petId, updatePetDto);
        return petMapper.toPetDto(savedPet);

    }

    @Override
    @Transactional(readOnly = true)
    public List<PetDto> getAllPetsForAdmin(Long requesterId) {
        User requester = findUserById(requesterId);
        checkAccess(requester);
        List<Pet> pets = petRepository.findAll();
        log.info("PetService: getAllPetsForAdmin, requesterId={}", requesterId);
        return pets.stream()
                .map(petMapper::toPetForAdminDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePetById(Long requesterId, Long petId) {
        User requester = findUserById(requesterId);
        checkAccess(requester);
        getPetIfExists(petId);
        petRepository.deleteById(petId);
        log.info("PetService: deletePetById, requesterId={}, petId={}", requesterId, petId);
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id = %d not found", userId)));
    }

    private void checkAccess(User requester) {
        if (requester.getRole().ordinal() >= 2) {
            throw new AccessDeniedException(String.format("User with role = %s, can't access for this action",
                    requester.getRole()));
        }
    }

    private Pet getPetIfExists(Long petId) {
        return petRepository.findById(petId).orElseThrow(() ->
                new NotFoundException(String.format("Pet with id = %d not found", petId)));
    }
}
