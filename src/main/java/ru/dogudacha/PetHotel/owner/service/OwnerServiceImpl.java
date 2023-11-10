package ru.dogudacha.PetHotel.owner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.owner.dto.NewOwnerDto;
import ru.dogudacha.PetHotel.owner.dto.OwnerShortDto;
import ru.dogudacha.PetHotel.owner.dto.UpdateOwnerDto;
import ru.dogudacha.PetHotel.owner.dto.OwnerDto;
import ru.dogudacha.PetHotel.owner.dto.mapper.OwnerMapper;
import ru.dogudacha.PetHotel.owner.model.Owner;
import ru.dogudacha.PetHotel.owner.repository.OwnerRepository;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @Transactional()
    @Override
    public OwnerDto addOwner(Long requesterId, NewOwnerDto newOwnerDto) {
        User requester = findUserById(requesterId);
        checkAccessForEdit(requester);
        Owner newOwner = ownerMapper.toOwner(newOwnerDto);

        Owner addedOwner = ownerRepository.save(newOwner);
        log.info("ownerService: has been added owner={}", addedOwner);
        return ownerMapper.toOwnerDto(addedOwner);
    }

    @Transactional(readOnly = true)
    @Override
    public OwnerShortDto getShortOwnerById(Long requesterId, Long ownerId) {
        User requester = findUserById(requesterId);
        checkAccessForBrowse(requester);
        Owner owner = findOwnerById(ownerId);

        log.info("ownerService: has been returned ownerShortDto={}, by id={}", owner, ownerId);
        OwnerShortDto ownerShortDto = ownerMapper.toOwnerShortDto(owner);
        switch (owner.getPreferCommunication()) {
            case MAIN_PHONE -> ownerShortDto.setContact(owner.getMainPhone());
            case OPTIONAL_PHONE -> ownerShortDto.setContact( owner.getOptionalPhone());
            case TELEGRAM -> ownerShortDto.setContact(owner.getTelegram());
            case WHATSAPP -> ownerShortDto.setContact(owner.getWhatsapp());
            case VIBER -> ownerShortDto.setContact(owner.getViber());
            case VK -> ownerShortDto.setContact(owner.getVk());
        }
        return ownerShortDto;
    }

    @Transactional(readOnly = true)
    @Override
    public OwnerDto getOwnerById(Long requesterId, Long ownerId) {
        User requester = findUserById(requesterId);
        checkAccessForBrowse(requester);
        Owner owner = findOwnerById(ownerId);

        log.info("ownerService: has been returned ownerDto={}, by id={}", owner, ownerId);
        return ownerMapper.toOwnerDto(owner);
    }

    @Transactional
    @Override
    public OwnerDto updateOwner(Long requesterId, Long ownerId, UpdateOwnerDto ownerDto) {
        User requester = findUserById(requesterId);
        checkAccessForEdit(requester);
        Owner oldOwner = findOwnerById(ownerId);
        Owner newOwner = ownerMapper.toOwner(ownerDto);
        newOwner.setId(ownerId);

        if (Objects.isNull(newOwner.getName())) {
            newOwner.setName(oldOwner.getName());
        }

        if (Objects.isNull(newOwner.getEmail())) {
            newOwner.setEmail(oldOwner.getEmail());
        }

        Owner updatedOwner = ownerRepository.save(newOwner);
        log.info("ownerService: old owner={} has been updated to new owner={}", oldOwner, updatedOwner);

        return ownerMapper.toOwnerDto(updatedOwner);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OwnerDto> getAllOwners(Long requesterId) {
        User requester = findUserById(requesterId);
        checkAccessForBrowse(requester);
        List<Owner> allOwners = ownerRepository.findAll();

        log.info("ownerService: has been returned all {} owners", allOwners.size());
        return ownerMapper.map(allOwners);
    }

    @Transactional
    @Override
    public void deleteOwnerById(Long requesterId, Long ownerId) {
        User requester = findUserById(requesterId);
        checkAccessForEdit(requester);

        ownerRepository.deleteById(ownerId);
        log.info("ownerService: has been delete owner with id={}", ownerId);
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("user with id=%d not found", userId)));
    }

    private void checkAccessForEdit(User requester) {

        if (requester.getRole().ordinal() > 1) {

            throw new AccessDeniedException(String.format("User with role=%s, hasn't access for edit this information",
                    requester.getRole()));
        }
    }

    private void checkAccessForBrowse(User requester) {
        if (requester.getRole().ordinal() > 2) {
            throw new AccessDeniedException(String.format("User with role=%s, hasn't access for browsing this information",
                    requester.getRole()));
        }
    }

    private Owner findOwnerById(long ownerId) {
        return ownerRepository.findById(ownerId).orElseThrow(() ->
                new NotFoundException(String.format("owner with id=%d not found", ownerId)));
    }
}
