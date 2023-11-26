package ru.dogudacha.PetHotel.room;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.dogudacha.PetHotel.room.dto.RoomDto;
import ru.dogudacha.PetHotel.room.dto.UpdateRoomDto;
import ru.dogudacha.PetHotel.room.service.RoomService;

import java.util.Collection;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping(path = "/rooms")
@RequiredArgsConstructor
public class RoomController {
    private static final String USER_ID = "X-PetHotel-User-Id";
    private final RoomService roomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomDto addRoom(@RequestHeader(USER_ID) Long requesterId,
                           @RequestBody @Valid RoomDto roomDto) {
        log.info("RoomController: POST/addRoom, requesterId={}, room={}", requesterId, roomDto);
        return roomService.addRoom(requesterId, roomDto);
    }

    @GetMapping("/{id}")
    public RoomDto getRoomById(@RequestHeader(USER_ID) Long requesterId,
                               @PathVariable("id") long roomId) {
        log.info("RoomController: GET/getRoomById, requesterId={}, roomId={}", requesterId, roomId);
        return roomService.getRoomById(requesterId, roomId);
    }

    @PatchMapping("/{id}")
    public RoomDto updateRoom(@RequestHeader(USER_ID) Long requesterId,
                              @RequestBody UpdateRoomDto roomDto,
                              @PathVariable("id") long roomId) {
        log.info("RoomController: PATCH/updateRoom, requesterId={}, roomId={}, requestBody={}",
                requesterId, roomId, roomDto);
        return roomService.updateRoom(requesterId, roomId, roomDto);
    }

    @GetMapping
    public Collection<RoomDto> getAllRooms(@RequestHeader(USER_ID) Long requesterId,
                                           @Param("isVisible") Boolean isVisible) {
        log.info("RoomController: GET/getAllRooms, requesterId={}", requesterId);
        return roomService.getAllRooms(requesterId, isVisible);
    }

    @PatchMapping("/{id}/hide")
    public RoomDto hideRoomById(@RequestHeader(USER_ID) Long requesterId,
                                @PathVariable("id") long roomId) {
        log.info("RoomController: PATCH/hideRoomById, requesterId={}, roomId={}",
                requesterId, roomId);
        return roomService.hideRoomById(requesterId, roomId);
    }

    @PatchMapping("/{id}/unhide")
    public RoomDto unhideRoomById(@RequestHeader(USER_ID) Long requesterId,
                                  @PathVariable("id") long roomId) {
        log.info("RoomController: PATCH/unhideRoomById, requesterId={}, roomId={}",
                requesterId, roomId);
        return roomService.unhideRoomById(requesterId, roomId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoomById(@RequestHeader(USER_ID) Long requesterId,
                               @PathVariable("id") Long roomId) {
        log.info("RoomController: DELETE/deleteRoomById, requesterId={}, roomId={}", requesterId, roomId);
        roomService.permanentlyDeleteRoomById(requesterId, roomId);
    }
}
