package ru.dogudacha.PetHotel.pet.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.ConflictException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;
import ru.dogudacha.PetHotel.pet.mapper.PetMapper;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.pet.repository.PetRepository;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

import java.util.Objects;

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
        findUserById(newPetDto.getOwnerId());
        checkAccess(requester);
        checkPet(newPetDto);
        Pet newPet = petMapper.toPet(newPetDto);
        Pet savedPet = petRepository.save(newPet);
        log.info("PetService: addPet, requesterId={}, petId={}", requesterId, savedPet.getId());
        return petMapper.toPetDto(savedPet);
    }

    @Override
    @Transactional(readOnly = true)
    public PetDto getPetById(Long requesterId, Long petId) {
        findUserById(requesterId);
        Pet pet = getPetIfExists(petId);
        log.info("PetService: getPetById, requesterId={}, petId={}", requesterId, petId);
        return petMapper.toPetDto(pet);
    }

    @Override
    @Transactional
    public PetDto updatePet(Long requesterId, Long petId, UpdatePetDto updatePetDto) {
        User requester = findUserById(requesterId);
        checkAccess(requester);
        Pet pet = getPetIfExists(petId);
        checkPet(pet, updatePetDto);
        if (Objects.nonNull(updatePetDto.getTypeOfPet())) {
            pet.setTypeOfPet(updatePetDto.getTypeOfPet());
        }
        if (Objects.nonNull(updatePetDto.getName()) && !updatePetDto.getName().isBlank()) {
            pet.setName(updatePetDto.getName());
        }
        if (Objects.nonNull(updatePetDto.getBreed()) && !updatePetDto.getBreed().isBlank()) {
            pet.setBreed(updatePetDto.getBreed());
        }
        if (Objects.nonNull(updatePetDto.getBirthDate())) {
            pet.setBirthDate(updatePetDto.getBirthDate());
        }
        if (Objects.nonNull(updatePetDto.getAge()) && !updatePetDto.getAge().isBlank()) {
            pet.setAge(updatePetDto.getAge());
        }
        if (Objects.nonNull(updatePetDto.getSex())) {
            pet.setSex(updatePetDto.getSex());
        }
        if (Objects.nonNull(updatePetDto.getColor()) && !updatePetDto.getColor().isBlank()) {
            pet.setColor(updatePetDto.getColor());
        }
        if (Objects.nonNull(updatePetDto.getSign()) && !updatePetDto.getSign().isBlank()) {
            pet.setSign(updatePetDto.getSign());
        }
        if (Objects.nonNull(updatePetDto.getIsExhibition())) {
            pet.setIsExhibition(updatePetDto.getIsExhibition());
        }
        if (Objects.nonNull(updatePetDto.getVetVisit())) {
            pet.setVetVisit(updatePetDto.getVetVisit());
        }
        if (Objects.nonNull(updatePetDto.getVetVisitReason()) && !updatePetDto.getVetVisitReason().isBlank()) {
            pet.setVetVisitReason(updatePetDto.getVetVisitReason());
        }
        if (Objects.nonNull(updatePetDto.getVaccine()) && !updatePetDto.getVaccine().isBlank()) {
            pet.setVaccine(updatePetDto.getVaccine());
        }
        if (Objects.nonNull(updatePetDto.getParasites()) && !updatePetDto.getParasites().isBlank()) {
            pet.setParasites(updatePetDto.getParasites());
        }
        if (Objects.nonNull(updatePetDto.getFleaMite()) && !updatePetDto.getFleaMite().isBlank()) {
            pet.setFleaMite(updatePetDto.getFleaMite());
        }
        if (Objects.nonNull(updatePetDto.getSurgery()) && !updatePetDto.getSurgery().isBlank()) {
            pet.setSurgery(updatePetDto.getSurgery());
        }
        if (Objects.nonNull(updatePetDto.getPastDisease()) && !updatePetDto.getPastDisease().isBlank()) {
            pet.setPastDisease(updatePetDto.getPastDisease());
        }
        if (Objects.nonNull(updatePetDto.getHealthCharacteristic()) && !updatePetDto.getHealthCharacteristic().isBlank()) {
            pet.setHealthCharacteristic(updatePetDto.getHealthCharacteristic());
        }
        if (Objects.nonNull(updatePetDto.getUrineAnalysis()) && !updatePetDto.getUrineAnalysis().isBlank()) {
            pet.setUrineAnalysis(updatePetDto.getUrineAnalysis());
        }
        if (Objects.nonNull(updatePetDto.getAllergy())) {
            pet.setAllergy(updatePetDto.getAllergy());
        }
        if (Objects.nonNull(updatePetDto.getAllergyType()) && !updatePetDto.getAllergyType().isBlank()) {
            pet.setAllergyType(updatePetDto.getAllergyType());
        }
        if (Objects.nonNull(updatePetDto.getChronicDisease())) {
            pet.setChronicDisease(updatePetDto.getChronicDisease());
        }
        if (Objects.nonNull(updatePetDto.getChronicDiseaseType()) && !updatePetDto.getChronicDiseaseType().isBlank()) {
            pet.setChronicDiseaseType(updatePetDto.getChronicDiseaseType());
        }
        if (Objects.nonNull(updatePetDto.getHeatDate())) {
            pet.setHeatDate(updatePetDto.getHeatDate());
        }
        if (Objects.nonNull(updatePetDto.getVetData()) && !updatePetDto.getVetData().isBlank()) {
            pet.setVetData(updatePetDto.getVetData());
        }
        if (Objects.nonNull(updatePetDto.getStayWithoutMaster()) && !updatePetDto.getStayWithoutMaster().isBlank()) {
            pet.setStayWithoutMaster(updatePetDto.getStayWithoutMaster());
        }
        if (Objects.nonNull(updatePetDto.getStayAlone()) && !updatePetDto.getStayAlone().isBlank()) {
            pet.setStayAlone(updatePetDto.getStayAlone());
        }
        if (Objects.nonNull(updatePetDto.getSpecialCare()) && !updatePetDto.getSpecialCare().isBlank()) {
            pet.setSpecialCare(updatePetDto.getSpecialCare());
        }
        if (Objects.nonNull(updatePetDto.getBarkHowl()) && !updatePetDto.getBarkHowl().isBlank()) {
            pet.setBarkHowl(updatePetDto.getBarkHowl());
        }
        if (Objects.nonNull(updatePetDto.getFurnitureDamage()) && !updatePetDto.getFurnitureDamage().isBlank()) {
            pet.setFurnitureDamage(updatePetDto.getFurnitureDamage());
        }
        if (Objects.nonNull(updatePetDto.getFoodFromTable()) && !updatePetDto.getFoodFromTable().isBlank()) {
            pet.setFoodFromTable(updatePetDto.getFoodFromTable());
        }
        if (Objects.nonNull(updatePetDto.getDefecateAtHome()) && !updatePetDto.getDefecateAtHome().isBlank()) {
            pet.setDefecateAtHome(updatePetDto.getDefecateAtHome());
        }
        if (Objects.nonNull(updatePetDto.getAllergyType()) && !updatePetDto.getAllergyType().isBlank()) {
            pet.setAllergyType(updatePetDto.getAllergyType());
        }
        if (Objects.nonNull(updatePetDto.getMarkAtHome()) && !updatePetDto.getMarkAtHome().isBlank()) {
            pet.setMarkAtHome(updatePetDto.getMarkAtHome());
        }
        if (Objects.nonNull(updatePetDto.getNewPeople()) && !updatePetDto.getNewPeople().isBlank()) {
            pet.setNewPeople(updatePetDto.getNewPeople());
        }
        if (Objects.nonNull(updatePetDto.getIsBitePeople())) {
            pet.setIsBitePeople(updatePetDto.getIsBitePeople());
        }
        if (Objects.nonNull(updatePetDto.getReasonOfBite()) && !updatePetDto.getReasonOfBite().isBlank()) {
            pet.setReasonOfBite(updatePetDto.getReasonOfBite());
        }
        if (Objects.nonNull(updatePetDto.getPlayWithDogs()) && !updatePetDto.getPlayWithDogs().isBlank()) {
            pet.setPlayWithDogs(updatePetDto.getPlayWithDogs());
        }
        if (Objects.nonNull(updatePetDto.getIsDogTraining())) {
            pet.setIsDogTraining(updatePetDto.getIsDogTraining());
        }
        if (Objects.nonNull(updatePetDto.getTrainingName()) && !updatePetDto.getTrainingName().isBlank()) {
            pet.setTrainingName(updatePetDto.getTrainingName());
        }
        if (Objects.nonNull(updatePetDto.getLike()) && !updatePetDto.getLike().isBlank()) {
            pet.setLike(updatePetDto.getLike());
        }
        if (Objects.nonNull(updatePetDto.getNotLike()) && !updatePetDto.getNotLike().isBlank()) {
            pet.setNotLike(updatePetDto.getNotLike());
        }
        if (Objects.nonNull(updatePetDto.getToys()) && !updatePetDto.getToys().isBlank()) {
            pet.setToys(updatePetDto.getToys());
        }
        if (Objects.nonNull(updatePetDto.getBadHabit()) && !updatePetDto.getBadHabit().isBlank()) {
            pet.setBadHabit(updatePetDto.getBadHabit());
        }
        if (Objects.nonNull(updatePetDto.getWalking()) && !updatePetDto.getWalking().isBlank()) {
            pet.setWalking(updatePetDto.getWalking());
        }
        if (Objects.nonNull(updatePetDto.getMorningWalking()) && !updatePetDto.getMorningWalking().isBlank()) {
            pet.setMorningWalking(updatePetDto.getMorningWalking());
        }
        if (Objects.nonNull(updatePetDto.getDayWalking()) && !updatePetDto.getDayWalking().isBlank()) {
            pet.setDayWalking(updatePetDto.getDayWalking());
        }
         //этот метод ещё не доделан
        if (Objects.nonNull(updatePetDto.getTrainingName()) && !updatePetDto.getTrainingName().isBlank()) {
            pet.setTrainingName(updatePetDto.getTrainingName());
        }

        if (Objects.nonNull(updatePetDto.getIsBitePeople())) {
            pet.setIsBitePeople(updatePetDto.getIsBitePeople());
        }

        Pet savedPet = petRepository.save(pet);
        log.info("PetService: updatePet, requesterId={}, petId={}, updatePetDto={}", requesterId, petId, updatePetDto);
        return petMapper.toPetDto(savedPet);

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

    private void checkPet(NewPetDto newPetDto) {
        try {
            Pet pet = petRepository.findByOwnerAndName(newPetDto.getOwnerId(), newPetDto.getName());
            if (pet != null) {
                throw new ConflictException(String.format("The client with id = %d already has a pet with name = %s.", newPetDto.getOwnerId(), newPetDto.getName()));
            }

        } catch (NullPointerException exception) {
            return;
        }

    }

    private void checkPet(Pet oldPet, UpdatePetDto updatePetDto) {
        try {
            Pet pet = petRepository.findByOwnerAndName(oldPet.getOwner(), updatePetDto.getName());
            if (pet != null) {
                throw new ConflictException(String.format("The client with id = %d already has a pet with name = %s.", oldPet.getOwner(), updatePetDto.getName()));
            }

        } catch (NullPointerException exception) {
            return;
        }

    }
}
