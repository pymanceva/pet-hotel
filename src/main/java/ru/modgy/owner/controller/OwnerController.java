package ru.modgy.owner.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.modgy.exception.BadRequestException;
import ru.modgy.owner.dto.*;
import ru.modgy.owner.service.OwnerService;
import ru.modgy.user.model.Roles;
import ru.modgy.utility.UtilityService;

import java.util.Collection;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(path = "/owners")
@RequiredArgsConstructor
@Validated
public class OwnerController {
    private final OwnerService ownerService;
    private final UtilityService utilityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OwnerDto addOwner(
            @RequestHeader(value = UtilityService.REQUESTER_ID_HEADER) Long requesterId,
            @RequestBody @Valid NewOwnerDto newOwnerDto
    ) {
        log.info("OwnerController: requesterId={} POST/addOwner body={}",
                requesterId, newOwnerDto);
        utilityService.checkHigherOrdinalRoleAccess(requesterId, Roles.ROLE_ADMIN);
        return ownerService.addOwner(requesterId, newOwnerDto);
    }

    @GetMapping("/{id}")
    public OwnerDto getOwnerById(
            @RequestHeader(value = UtilityService.REQUESTER_ID_HEADER) Long requesterId,
            @PathVariable(value = "id") Long id
    ) {
        log.info("OwnerController: requesterId={} GET/getOwnerByIdForAdmin id={}", requesterId, id);
        utilityService.checkHigherOrdinalRoleAccess(requesterId, Roles.ROLE_ADMIN);
        return ownerService.getOwnerById(requesterId, id);
    }

    @GetMapping("/short/{id}")
    public OwnerShortDto getShortOwnerById(
            @RequestHeader(value = UtilityService.REQUESTER_ID_HEADER) Long requesterId,
            @PathVariable(value = "id") Long id
    ) {
        log.info("OwnerController: requesterId={} GET/getOwnerById id={}", requesterId, id);
        utilityService.checkHigherOrdinalRoleAccess(requesterId, Roles.ROLE_ADMIN);
        return ownerService.getShortOwnerById(requesterId, id);
    }

    @GetMapping("/short")
    public Collection<OwnerShortDto> getSomeShortOwners(
            @RequestHeader(value = UtilityService.REQUESTER_ID_HEADER) Long requesterId,
            @RequestParam(value = "num", required = false, defaultValue = "10") @Positive int num
    ) {
        log.info("OwnerController: requesterId={} GET/getSomeShortOwners num={}", requesterId, num);
        utilityService.checkHigherOrdinalRoleAccess(requesterId, Roles.ROLE_ADMIN);
        return ownerService.getSomeShortOwners(requesterId, num);
    }

    @PatchMapping("/{id}")
    public OwnerDto updateOwner(
            @RequestHeader(value = UtilityService.REQUESTER_ID_HEADER) Long requesterId,
            @RequestBody @Valid UpdateOwnerDto updateOwnerDto,
            @PathVariable(value = "id") Long ownerId
    ) {
        log.info("OwnerController: requesterId={} PATCH/updateOwner id={}, updateBody={}",
                requesterId, ownerId, updateOwnerDto);
        utilityService.checkHigherOrdinalRoleAccess(requesterId, Roles.ROLE_ADMIN);
        return ownerService.updateOwner(requesterId, ownerId, updateOwnerDto);
    }

    @GetMapping
    public Collection<OwnerDto> getAllOwners(
            @RequestHeader(value = UtilityService.REQUESTER_ID_HEADER) Long requesterId
    ) {
        log.info("OwnerController: requesterId={} GET/getAllOwners", requesterId);
        utilityService.checkHigherOrdinalRoleAccess(requesterId, Roles.ROLE_ADMIN);
        return ownerService.getAllOwners(requesterId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnerById(
            @RequestHeader(value = UtilityService.REQUESTER_ID_HEADER) Long requesterId,
            @PathVariable("id") Long id
    ) {
        log.info("OwnerController: requesterId={} DELETE/deleteOwnerById id= {}", requesterId, id);
        ownerService.deleteOwnerById(requesterId, id);
    }

    @PostMapping("/check")
    public OwnerDto checkOwnerPhoneNumber(
            @RequestHeader(value = UtilityService.REQUESTER_ID_HEADER) Long requesterId,
            @RequestBody @Valid CheckOwnerDto checkOwnerDto
    ) {
        log.info("OwnerController: requesterId={} GET/checkOwnerPhoneNumber checkOwnerDto={}",
                requesterId, checkOwnerDto);
        utilityService.checkHigherOrdinalRoleAccess(requesterId, Roles.ROLE_ADMIN);
        return ownerService.checkOwnerPhoneNumber(requesterId, checkOwnerDto);
    }

    @PostMapping("/search")
    public Collection<OwnerDto> searchOwner(
            @RequestHeader(value = UtilityService.REQUESTER_ID_HEADER) Long requesterId,
            @RequestBody SearchOwnerDto searchOwnerDto,
            @RequestParam(value = "direction") String directionString
    ) {
        SearchDirection searchDirection = SearchDirection.fromString(directionString);
        if (searchDirection == null) {
            throw new BadRequestException("RequestParam direction error");
        }
        log.info("OwnerController: requesterId={} GET/searchOwner searchOwnerDto={}, direction={}",
                requesterId, searchOwnerDto, searchDirection);
        utilityService.checkHigherOrdinalRoleAccess(requesterId, Roles.ROLE_ADMIN);
        return ownerService.searchOwner(requesterId, searchOwnerDto, searchDirection);
    }
}
