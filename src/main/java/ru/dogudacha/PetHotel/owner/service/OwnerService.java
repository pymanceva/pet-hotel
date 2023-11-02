package ru.dogudacha.PetHotel.owner.service;

import ru.dogudacha.PetHotel.owner.dto.OwnerDto;
import ru.dogudacha.PetHotel.owner.dto.UpdateOwnerDto;

import java.util.Collection;

public interface OwnerService {
    OwnerDto addOwner(Long requesterId, OwnerDto ownerDto);

    OwnerDto getOwnerById(Long requesterId, Long id);

    OwnerDto updateOwner(Long requesterId, Long ownerId, UpdateOwnerDto updateOwnerDto);

    Collection<OwnerDto> getAllOwners(Long requesterId);

    void deleteOwnerById(Long requesterId, Long id);
}
