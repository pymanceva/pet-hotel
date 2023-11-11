package ru.dogudacha.PetHotel.room;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.dogudacha.PetHotel.room.dto.RoomDto;
import ru.dogudacha.PetHotel.room.dto.RoomWithoutPriceDto;
import ru.dogudacha.PetHotel.room.dto.UpdateRoomDto;
import ru.dogudacha.PetHotel.room.service.RoomService;

import java.util.Collection;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping(path = "/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomDto addRoom(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId,
                           @RequestBody @Valid RoomDto roomDto) {
        log.info("RoomController: POST/addRoom, requesterId={}, room={}", requesterId, roomDto);
        return roomService.addRoom(requesterId, roomDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoomWithoutPriceDto getRoomWithoutPriceById(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId,
                                                       @PathVariable(value = "id") long roomId) {
        log.info("RoomController: GET/getRoomWithoutPriceById, requesterId={}, roomId={}", requesterId, roomId);
        return roomService.getRoomWithoutPriceById(requesterId, roomId);
    }

    @GetMapping("/{id}/withPrice")
    @ResponseStatus(HttpStatus.OK)
    public RoomDto getRoomById(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId,
                               @PathVariable(value = "id") long roomId) {
        log.info("RoomController: GET/getRoomById, requesterId={}, roomId={}", requesterId, roomId);
        return roomService.getRoomById(requesterId, roomId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoomDto updateRoom(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId,
                              @RequestBody UpdateRoomDto roomDto,
                              @PathVariable(value = "id") long roomId) {
        log.info("RoomController: PATCH/updateRoom, requesterId={}, roomId={}, requestBody={}",
                requesterId, roomId, roomDto);
        return roomService.updateRoom(requesterId, roomId, roomDto);
    }

    @GetMapping("/withPrice")
    @ResponseStatus(HttpStatus.OK)
    public Collection<RoomDto> getAllRooms(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId) {
        log.info("RoomController: GET/getAllRooms, requesterId={}", requesterId);
        return roomService.getAllRooms(requesterId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<RoomWithoutPriceDto> getAllRoomsWithoutPrice(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId) {
        log.info("RoomController: GET/getAllRoomsWithoutPrice, requesterId={}", requesterId);
        return roomService.getAllRoomsWithoutPrice(requesterId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoomById(@RequestHeader(value = "X-PetHotel-User-Id") Long requesterId,
                               @PathVariable("id") Long roomId) {
        log.info("RoomController: DELETE/deleteRoomById, requesterId={}, roomId={}", requesterId, roomId);
        roomService.deleteRoomById(requesterId, roomId);
    }
}
