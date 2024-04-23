package ru.modgy.pet.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.modgy.pet.dto.NewPetDto;
import ru.modgy.pet.dto.PetDto;
import ru.modgy.pet.dto.UpdatePetDto;
import ru.modgy.pet.service.PetService;


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

    @GetMapping("/{id}")
    public PetDto getPetById(@RequestHeader(USER_ID) Long requesterId,
                             @PathVariable(value = "id") Long petId) {
        log.info("PetController: GET/getPetById, requesterId={}, petId={}", requesterId, petId);
        return petService.getPetById(requesterId, petId);
    }

    @PatchMapping("/{id}")
    public PetDto updatePet(@RequestHeader(USER_ID) Long requesterId,
                            @RequestBody @Valid UpdatePetDto updatePetDto,
                            @PathVariable(value = "id") Long petId) {
        log.info("PetController: PATCH/updatePet, requesterId={}, petId={}", requesterId, petId);
        return petService.updatePet(requesterId, petId, updatePetDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePetById(@RequestHeader(USER_ID) Long requesterId,
                              @PathVariable(value = "id") Long petId) {
        log.info("PetController: DELETE/deletePetById, requesterId= {}, petId={}", requesterId, petId);
        petService.deletePetById(requesterId, petId);
    }

}