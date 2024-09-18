package ru.modgy.owner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.modgy.exception.NotFoundException;
import ru.modgy.owner.controller.SearchDirection;
import ru.modgy.owner.dto.*;
import ru.modgy.owner.dto.mapper.OwnerMapper;
import ru.modgy.owner.model.Owner;
import ru.modgy.owner.repository.OwnerRepository;
import ru.modgy.utility.EntityService;
import ru.modgy.utility.PhoneFormatMapper;
import ru.modgy.utility.UpdateField;

import java.util.Collection;
import java.util.List;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final EntityService entityService;

    @Transactional()
    @Override
    public OwnerDto addOwner(Long requesterId, NewOwnerDto newOwnerDto) {
        Owner newOwner = ownerMapper.toOwner(newOwnerDto);
        newOwner.setRegistrationDate(now());
        Owner addedOwner = ownerRepository.save(newOwner);
        log.info("ownerService: addOwner, requesterId={}, owner={}", requesterId, addedOwner);
        return ownerMapper.toOwnerDto(addedOwner);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OwnerShortDto> getSomeShortOwners(Long requesterId, int num) {
        Pageable pageable = PageRequest.of(0, num, Sort.by("registrationDate").descending());
        Page<Owner> someLastOwners = ownerRepository.findAll(pageable);
        log.info("ownerService: getSomeShortOwners, requesterId={}, num={}, num of owners={}",
                requesterId, num, someLastOwners.toList().size());
        return ownerMapper.shortMap(someLastOwners.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public OwnerShortDto getShortOwnerById(Long requesterId, Long ownerId) {
        Owner owner = entityService.getOwnerIfExists(ownerId);
        log.info("ownerService: getShortOwnerById, requesterId={}, ownerShortDto={}, ownerId={}",
                requesterId, owner, ownerId);
        return ownerMapper.toOwnerShortDto(owner);
    }

    @Transactional(readOnly = true)
    @Override
    public OwnerDto getOwnerById(Long requesterId, Long ownerId) {
        Owner owner = entityService.getOwnerIfExists(ownerId);
        log.info("ownerService: getOwnerById, requesterId={}, ownerDto={}, ownerId={}", requesterId, owner, ownerId);
        return ownerMapper.toOwnerDto(owner);
    }

    @Transactional
    @Override
    public OwnerDto updateOwner(Long requesterId, Long ownerId, UpdateOwnerDto updateOwnerDto) {
        Owner oldOwner = entityService.getOwnerIfExists(ownerId);
        Owner newOwner = ownerMapper.toOwner(updateOwnerDto);
        newOwner.setId(ownerId);
        newOwner.setRegistrationDate(oldOwner.getRegistrationDate());

        newOwner.setLastName(UpdateField.stringField(oldOwner.getLastName(), newOwner.getLastName()));
        newOwner.setFirstName(UpdateField.stringField(oldOwner.getFirstName(), newOwner.getFirstName()));
        newOwner.setMiddleName(UpdateField.stringField(oldOwner.getMiddleName(), newOwner.getMiddleName()));
        newOwner.setMainPhone(UpdateField.stringField(oldOwner.getMainPhone(), newOwner.getMainPhone()));
        newOwner.setOptionalPhone(UpdateField.stringField(oldOwner.getOptionalPhone(), newOwner.getOptionalPhone()));
        newOwner.setOtherContacts(UpdateField.stringField(oldOwner.getOtherContacts(), newOwner.getOtherContacts()));
        newOwner.setActualAddress(UpdateField.stringField(oldOwner.getActualAddress(), newOwner.getActualAddress()));
        newOwner.setTrustedMan(UpdateField.stringField(oldOwner.getTrustedMan(), newOwner.getTrustedMan()));
        newOwner.setSource(UpdateField.stringField(oldOwner.getSource(), newOwner.getSource()));
        newOwner.setComment(UpdateField.stringField(oldOwner.getComment(), newOwner.getComment()));
        newOwner.setRating(UpdateField.intField(oldOwner.getRating(), newOwner.getRating()));

        Owner updatedOwner = ownerRepository.save(newOwner);
        log.info("ownerService: updateOwner, requesterId={}, old owner={}, updatedOwner={}",
                requesterId, oldOwner, updatedOwner);

        return ownerMapper.toOwnerDto(updatedOwner);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OwnerDto> getAllOwners(Long requesterId) {
        List<Owner> allOwners = ownerRepository.findAll();

        log.info("ownerService: getAllOwners, requesterId={}, num of owners={}", requesterId, allOwners.size());
        return ownerMapper.map(allOwners);
    }

    @Transactional
    @Override
    public void deleteOwnerById(Long requesterId, Long ownerId) {
        int result = ownerRepository.deleteOwnerById(ownerId);

        if (result == 0) {
            throw new NotFoundException(String.format("owner with id=%d not found", ownerId));
        }
        log.info("ownerService: deleteOwnerById, requesterId={}, ownerId={}", requesterId, ownerId);
    }

    @Transactional(readOnly = true)
    @Override
    public OwnerDto checkOwnerPhoneNumber(Long requesterId, CheckOwnerDto checkOwnerDto) {
        String phoneNumber = PhoneFormatMapper.formatPhoneNumber(checkOwnerDto.getMainPhone());
        Owner owner = ownerRepository.findByMainPhoneOrOptionalPhone(phoneNumber, phoneNumber).orElseGet(Owner::new);
        log.info("ownerService: checkOwnerPhoneNumber, requesterId={}, checkOwnerDto={}, phoneNumber={}, owner={}",
                requesterId, checkOwnerDto, phoneNumber, owner);
        return ownerMapper.toOwnerDto(owner);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<OwnerDto> searchOwner(Long requesterId, SearchOwnerDto searchOwnerDto, SearchDirection searchDirection) {
        String searchLine = searchOwnerDto.getWanted();
        List<Owner> foundOwners = ownerRepository.searchOwner(searchLine, searchDirection.getTitle());

        log.info("ownerService: searchOwner, requesterId={}, searchOwnerDto={}, direction={}, num foundOwners={}",
                requesterId,searchOwnerDto, searchDirection, foundOwners.size());

        return ownerMapper.map(foundOwners);
    }
}
