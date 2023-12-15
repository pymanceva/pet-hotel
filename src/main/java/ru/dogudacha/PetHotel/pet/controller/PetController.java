package ru.dogudacha.PetHotel.pet.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.dto.PetForAdminDto;
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;
import ru.dogudacha.PetHotel.pet.service.PetService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/pets")
@RequiredArgsConstructor
@Slf4j
public class PetController {
    private static final String USER_ID = "X-PetHotel-User-Id";
    private final PetService petService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetDto addPet(@RequestHeader(USER_ID) Long requesterId,
                         @RequestBody @Valid NewPetDto newPetDto) {
        log.info("PetController: POST/addPet, requesterId={}", requesterId);
        return petService.addPet(requesterId, newPetDto);
    }

    @GetMapping("/{id}/admin")
    public PetForAdminDto getPetByIdForAdmin(@RequestHeader(USER_ID) Long requesterId,
                                             @PathVariable(value = "id") Long petId) {
        log.info("PetController: GET/getPetById, requesterId={}, petId={}", requesterId, petId);
        return petService.getPetByIdForAdmin(requesterId, petId);
    }

    @GetMapping("/{id}")
    public PetDto getPetByIdForUser(@RequestHeader(USER_ID) Long requesterId,
                                    @PathVariable(value = "id") Long petId) {
        log.info("PetController: GET/getPetById, requesterId={}, petId={}", requesterId, petId);
        return petService.getPetByIdForUser(requesterId, petId);
    }

    @PatchMapping("/{id}")
    public PetDto updatePet(@RequestHeader(USER_ID) Long requesterId,
                            @RequestBody @Valid UpdatePetDto updatePetDto,
                            @PathVariable(value = "id") Long petId) {
        log.info("PetController: PATCH/updatePet, requesterId={}, petId={}", requesterId, petId);
        return petService.updatePet(requesterId, petId, updatePetDto);
    }

    @GetMapping
    public List<PetDto> getAllPets(@RequestHeader(USER_ID) Long requesterId) {
        log.info("PetController: GET/getAllPets, requesterId={}", requesterId);
        return petService.getAllPetsForAdmin(requesterId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePetById(@RequestHeader(USER_ID) Long requesterId,
                              @PathVariable(value = "id") Long petId) {
        log.info("PetController: DELETE/deletePetById, requesterId= {}, petId={}", requesterId, petId);
        petService.deletePetById(requesterId, petId);
    }

}
