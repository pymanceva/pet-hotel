package ru.dogudacha.PetHotel.owner.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.dogudacha.PetHotel.owner.dto.UpdateOwnerDto;
import ru.dogudacha.PetHotel.owner.dto.OwnerDto;
import ru.dogudacha.PetHotel.owner.service.OwnerService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/owners")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OwnerDto addOwner(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId,
                             @RequestBody @Valid OwnerDto ownerDto
    ) {

        log.info("OwnerController: requesterId={} POST/addOwner body={}",
                requesterId, ownerDto);
        return ownerService.addOwner(requesterId, ownerDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OwnerDto getOwnerById(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId,
                                 @PathVariable(value = "id") Long id
    ) {
        log.info("OwnerController: requesterId={} GET/getOwnerById id={}", requesterId, id);
        return ownerService.getOwnerById(requesterId, id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OwnerDto updateOwner(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId,
                                @RequestBody @Valid UpdateOwnerDto updateOwnerDto,
                                @PathVariable(value = "id") Long ownerId
    ) {
        log.info("OwnerController: requesterId={} PATCH/updateOwner id={}, updateBody={}",
                requesterId, ownerId, updateOwnerDto);
        return ownerService.updateOwner(requesterId, ownerId, updateOwnerDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<OwnerDto> getAllOwners(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId
    ) {
        log.info("OwnerController: requesterId={} GET/getAllOwners", requesterId);
        return ownerService.getAllOwners(requesterId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnerById(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId,
                                @PathVariable("id") Long id
    ) {
        log.info("OwnerController: requesterId={} DELETE/deleteOwnerById id= {}", requesterId, id);
        ownerService.deleteOwnerById(requesterId, id);
    }

}
