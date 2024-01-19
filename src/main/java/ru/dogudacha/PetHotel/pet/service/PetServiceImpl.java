package ru.dogudacha.PetHotel.pet.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;
import ru.dogudacha.PetHotel.pet.mapper.PetMapper;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.pet.repository.PetRepository;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.utility.UtilityService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;
    private final PetMapper petMapper;
    final private UtilityService utilityService;

    @Override
    @Transactional
    public PetDto addPet(Long requesterId, NewPetDto newPetDto) {
        User requester = utilityService.getUserIfExists(requesterId);
        //метод проверки наличия хозяина питомца, будет дописан после добавления сущности оунеров
        //findOwnerById(newPetDto.getOwnerId());
        utilityService.checkBossAdminAccess(requester);
        //метод проверки уникальности питомца, будет дописан после добавления сущности оунеров
//        checkPet(newPetDto);
        Pet newPet = petMapper.toPet(newPetDto);
        Pet savedPet = petRepository.save(newPet);
        log.info("PetService: addPet, requesterId={}, petId={}", requesterId, savedPet.getId());
        return petMapper.toPetDto(savedPet);
    }

    @Override
    @Transactional(readOnly = true)
    public PetDto getPetById(Long requesterId, Long petId) {
        utilityService.getUserIfExists(requesterId);
        Pet pet = utilityService.getPetIfExists(petId);
        log.info("PetService: getPetById, requesterId={}, petId={}", requesterId, petId);
        return petMapper.toPetDto(pet);
    }

    @Override
    @Transactional
    public PetDto updatePet(Long requesterId, Long petId, UpdatePetDto updatePetDto) {
        User requester = utilityService.getUserIfExists(requesterId);
        utilityService.checkBossAdminAccess(requester);
        Pet oldPet = utilityService.getPetIfExists(petId);
        Pet newPet = petMapper.toPet(updatePetDto);
        newPet.setId(oldPet.getId());
        //метод проверки уникальности питомца, будет дописан после добавления сущности оунеров
//        checkPet(pet, updatePetDto);
        if (Objects.isNull(updatePetDto.getType())) {
            newPet.setType(oldPet.getType());
        }
        if (Objects.isNull(updatePetDto.getName()) || updatePetDto.getName().isBlank()) {
            newPet.setName(oldPet.getName());
        }
        if (Objects.isNull(updatePetDto.getBreed()) || updatePetDto.getBreed().isBlank()) {
            newPet.setBreed(oldPet.getBreed());
        }
        if (Objects.isNull(updatePetDto.getBirthDate())) {
            newPet.setBirthDate(oldPet.getBirthDate());
        }
        if (Objects.isNull(updatePetDto.getSex())) {
            newPet.setSex(oldPet.getSex());
        }
        if (Objects.isNull(updatePetDto.getColor()) || updatePetDto.getColor().isBlank()) {
            newPet.setColor(oldPet.getColor());
        }
        if (Objects.isNull(updatePetDto.getSign()) || updatePetDto.getSign().isBlank()) {
            newPet.setSign(oldPet.getSign());
        }
        if (Objects.isNull(updatePetDto.getIsExhibition())) {
            newPet.setIsExhibition(oldPet.getIsExhibition());
        }
        if (Objects.isNull(updatePetDto.getVetVisitDate())) {
            newPet.setVetVisitDate(oldPet.getVetVisitDate());
        }
        if (Objects.isNull(updatePetDto.getVetVisitReason()) || updatePetDto.getVetVisitReason().isBlank()) {
            newPet.setVetVisitReason(oldPet.getVetVisitReason());
        }
        if (Objects.isNull(updatePetDto.getVaccine()) || updatePetDto.getVaccine().isBlank()) {
            newPet.setVaccine(oldPet.getVaccine());
        }
        if (Objects.isNull(updatePetDto.getParasites()) || updatePetDto.getParasites().isBlank()) {
            newPet.setParasites(oldPet.getParasites());
        }
        if (Objects.isNull(updatePetDto.getFleaMite()) || updatePetDto.getFleaMite().isBlank()) {
            newPet.setFleaMite(oldPet.getFleaMite());
        }
        if (Objects.isNull(updatePetDto.getSurgery()) || updatePetDto.getSurgery().isBlank()) {
            newPet.setSurgery(oldPet.getSurgery());
        }
        if (Objects.isNull(updatePetDto.getPastDisease()) || updatePetDto.getPastDisease().isBlank()) {
            newPet.setPastDisease(oldPet.getPastDisease());
        }
        if (Objects.isNull(updatePetDto.getHealthCharacteristic()) || updatePetDto.getHealthCharacteristic().isBlank()) {
            newPet.setHealthCharacteristic(oldPet.getHealthCharacteristic());
        }
        if (Objects.isNull(updatePetDto.getUrineAnalysis()) || updatePetDto.getUrineAnalysis().isBlank()) {
            newPet.setUrineAnalysis(oldPet.getUrineAnalysis());
        }
        if (Objects.isNull(updatePetDto.getIsAllergy())) {
            newPet.setIsAllergy(oldPet.getIsAllergy());
        }
        if (Objects.isNull(updatePetDto.getAllergyType()) || updatePetDto.getAllergyType().isBlank()) {
            newPet.setAllergyType(oldPet.getAllergyType());
        }
        if (Objects.isNull(updatePetDto.getIsChronicDisease())) {
            newPet.setIsChronicDisease(oldPet.getIsChronicDisease());
        }
        if (Objects.isNull(updatePetDto.getChronicDiseaseType()) || updatePetDto.getChronicDiseaseType().isBlank()) {
            newPet.setChronicDiseaseType(oldPet.getChronicDiseaseType());
        }
        if (Objects.isNull(updatePetDto.getHeatDate())) {
            newPet.setHeatDate(oldPet.getHeatDate());
        }
        if (Objects.isNull(updatePetDto.getVetData()) || updatePetDto.getVetData().isBlank()) {
            newPet.setVetData(oldPet.getVetData());
        }
        if (Objects.isNull(updatePetDto.getStayWithoutMaster()) || updatePetDto.getStayWithoutMaster().isBlank()) {
            newPet.setStayWithoutMaster(oldPet.getStayWithoutMaster());
        }
        if (Objects.isNull(updatePetDto.getStayAlone()) || updatePetDto.getStayAlone().isBlank()) {
            newPet.setStayAlone(oldPet.getStayAlone());
        }
        if (Objects.isNull(updatePetDto.getSpecialCare()) || updatePetDto.getSpecialCare().isBlank()) {
            newPet.setSpecialCare(oldPet.getSpecialCare());
        }
        if (Objects.isNull(updatePetDto.getBarkHowl()) || updatePetDto.getBarkHowl().isBlank()) {
            newPet.setBarkHowl(oldPet.getBarkHowl());
        }
        if (Objects.isNull(updatePetDto.getFurnitureDamage()) || updatePetDto.getFurnitureDamage().isBlank()) {
            newPet.setFurnitureDamage(oldPet.getFurnitureDamage());
        }
        if (Objects.isNull(updatePetDto.getFoodFromTable()) || updatePetDto.getFoodFromTable().isBlank()) {
            newPet.setFoodFromTable(oldPet.getFoodFromTable());
        }
        if (Objects.isNull(updatePetDto.getDefecateAtHome()) || updatePetDto.getDefecateAtHome().isBlank()) {
            newPet.setDefecateAtHome(oldPet.getDefecateAtHome());
        }
        if (Objects.isNull(updatePetDto.getAllergyType()) || updatePetDto.getAllergyType().isBlank()) {
            newPet.setAllergyType(oldPet.getAllergyType());
        }
        if (Objects.isNull(updatePetDto.getMarkAtHome()) || updatePetDto.getMarkAtHome().isBlank()) {
            newPet.setMarkAtHome(oldPet.getMarkAtHome());
        }
        if (Objects.isNull(updatePetDto.getNewPeople()) || updatePetDto.getNewPeople().isBlank()) {
            newPet.setNewPeople(oldPet.getNewPeople());
        }
        if (Objects.isNull(updatePetDto.getIsBitePeople())) {
            newPet.setIsBitePeople(oldPet.getIsBitePeople());
        }
        if (Objects.isNull(updatePetDto.getReasonOfBite()) || updatePetDto.getReasonOfBite().isBlank()) {
            newPet.setReasonOfBite(oldPet.getReasonOfBite());
        }
        if (Objects.isNull(updatePetDto.getPlayWithDogs()) || updatePetDto.getPlayWithDogs().isBlank()) {
            newPet.setPlayWithDogs(oldPet.getPlayWithDogs());
        }
        if (Objects.isNull(updatePetDto.getIsDogTraining())) {
            newPet.setIsDogTraining(oldPet.getIsDogTraining());
        }
        if (Objects.isNull(updatePetDto.getTrainingName()) || updatePetDto.getTrainingName().isBlank()) {
            newPet.setTrainingName(oldPet.getTrainingName());
        }
        if (Objects.isNull(updatePetDto.getLike()) || updatePetDto.getLike().isBlank()) {
            newPet.setLike(oldPet.getLike());
        }
        if (Objects.isNull(updatePetDto.getNotLike()) || updatePetDto.getNotLike().isBlank()) {
            newPet.setNotLike(oldPet.getNotLike());
        }
        if (Objects.isNull(updatePetDto.getToys()) || updatePetDto.getToys().isBlank()) {
            newPet.setToys(oldPet.getToys());
        }
        if (Objects.isNull(updatePetDto.getBadHabit()) || updatePetDto.getBadHabit().isBlank()) {
            newPet.setBadHabit(oldPet.getBadHabit());
        }
        if (Objects.isNull(updatePetDto.getWalking()) || updatePetDto.getWalking().isBlank()) {
            newPet.setWalking(oldPet.getWalking());
        }
        if (Objects.isNull(updatePetDto.getMorningWalking()) || updatePetDto.getMorningWalking().isBlank()) {
            newPet.setMorningWalking(oldPet.getMorningWalking());
        }
        if (Objects.isNull(updatePetDto.getDayWalking()) || updatePetDto.getDayWalking().isBlank()) {
            newPet.setDayWalking(oldPet.getDayWalking());
        }
        if (Objects.isNull(updatePetDto.getEveningWalking()) || updatePetDto.getEveningWalking().isBlank()) {
            newPet.setEveningWalking(oldPet.getEveningWalking());
        }
        if (Objects.isNull(updatePetDto.getFeedingQuantity())) {
            newPet.setFeedingQuantity(oldPet.getFeedingQuantity());
        }
        if (Objects.isNull(updatePetDto.getFeedType()) || updatePetDto.getFeedType().isBlank()) {
            newPet.setFeedType(oldPet.getFeedType());
        }
        if (Objects.isNull(updatePetDto.getFeedName()) || updatePetDto.getFeedName().isBlank()) {
            newPet.setFeedName(oldPet.getFeedName());
        }
        if (Objects.isNull(updatePetDto.getFeedComposition()) || updatePetDto.getFeedComposition().isBlank()) {
            newPet.setFeedComposition(oldPet.getFeedComposition());
        }
        if (Objects.isNull(updatePetDto.getFeedingRate()) || updatePetDto.getFeedingRate().isBlank()) {
            newPet.setFeedingRate(oldPet.getFeedingRate());
        }
        if (Objects.isNull(updatePetDto.getFeedingPractice()) || updatePetDto.getFeedingPractice().isBlank()) {
            newPet.setFeedingPractice(oldPet.getFeedingPractice());
        }
        if (Objects.isNull(updatePetDto.getTreat()) || updatePetDto.getTreat().isBlank()) {
            newPet.setTreat(oldPet.getTreat());
        }
        if (Objects.isNull(updatePetDto.getIsMedicine())) {
            newPet.setIsMedicine(oldPet.getIsMedicine());
        }
        if (Objects.isNull(updatePetDto.getMedicineRegimen()) || updatePetDto.getMedicineRegimen().isBlank()) {
            newPet.setMedicineRegimen(oldPet.getMedicineRegimen());
        }
        if (Objects.isNull(updatePetDto.getAdditionalData()) || updatePetDto.getAdditionalData().isBlank()) {
            newPet.setAdditionalData(oldPet.getAdditionalData());
        }
        Pet savedPet = petRepository.save(newPet);
        log.info("PetService: updatePet, requesterId={}, petId={}, updatePetDto={}", requesterId, petId, updatePetDto);
        return petMapper.toPetDto(savedPet);

    }

    @Override
    @Transactional
    public void deletePetById(Long requesterId, Long petId) {
        User requester = utilityService.getUserIfExists(requesterId);
        utilityService.checkBossAdminAccess(requester);

        int result = petRepository.deletePetById(petId);

        if (result == 0) {
            throw new NotFoundException(String.format("pet with id=%d not found", petId));
        }
        log.info("PetService: deletePetById, requesterId={}, petId={}", requesterId, petId);
    }

    //метод проверки наличия хозяина питомца, будет дописан после добавления сущности оунеров
//    private User findOwnerById(long userId) {
//        return ownerRepository.findById(userId).orElseThrow(() ->
//                new NotFoundException(String.format("Owner with id = %d not found", userId)));
//    }

    //метод проверки уникальности питомца, будет дописан после добавления сущности оунеров
//    private void checkPet(NewPetDto newPetDto) {
//        try {
//            Pet pet = petRepository.findByOwnerAndName(newPetDto.getOwnerId(), newPetDto.getName());
//            if (pet != null) {
//                throw new ConflictException(String.format("The client with id = %d already has a pet with name = %s.", newPetDto.getOwnerId(), newPetDto.getName()));
//            }
//
//        } catch (NullPointerException exception) {
//            return;
//        }
//
//    }
    //метод проверки уникальности питомца, будет дописан после добавления сущности оунеров
//    private void checkPet(Pet oldPet, UpdatePetDto updatePetDto) {
//        try {
//            Pet pet = petRepository.findByOwnerAndName(oldPet.getOwner(), updatePetDto.getName());
//            if (pet != null) {
//                throw new ConflictException(String.format("The client with id = %d already has a pet with name = %s.", oldPet.getOwner(), updatePetDto.getName()));
//            }
//
//        } catch (NullPointerException exception) {
//            return;
//        }
//
//    }
}
