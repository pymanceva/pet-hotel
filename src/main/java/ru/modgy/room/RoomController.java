package ru.modgy.room;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.modgy.room.dto.NewRoomDto;
import ru.modgy.room.dto.RoomDto;
import ru.modgy.room.dto.UpdateRoomDto;
import ru.modgy.room.service.RoomService;
import ru.modgy.utility.UtilityService;

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
    public RoomDto addRoom(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                           @RequestBody @Valid NewRoomDto newRoomDto) {
        log.info("RoomController: POST/addRoom, requesterId={}, room={}", requesterId, newRoomDto);
        return roomService.addRoom(requesterId, newRoomDto);
    }

    @GetMapping("/{id}")
    public RoomDto getRoomById(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                               @PathVariable("id") Long roomId) {
        log.info("RoomController: GET/getRoomById, requesterId={}, roomId={}", requesterId, roomId);
        return roomService.getRoomById(requesterId, roomId);
    }

    @PatchMapping("/{id}")
    public RoomDto updateRoom(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                              @RequestBody @Valid UpdateRoomDto roomDto,
                              @PathVariable("id") Long roomId) {
        log.info("RoomController: PATCH/updateRoom, requesterId={}, roomId={}, requestBody={}",
                requesterId, roomId, roomDto);
        return roomService.updateRoom(requesterId, roomId, roomDto);
    }

    @GetMapping
    public Collection<RoomDto> getAllRooms(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                           @Param("isVisible") Boolean isVisible) {
        log.info("RoomController: GET/getAllRooms, requesterId={}", requesterId);
        return roomService.getAllRooms(requesterId, isVisible);
    }

    @PatchMapping("/{id}/hide")
    public RoomDto hideRoomById(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                @PathVariable("id") Long roomId) {
        log.info("RoomController: PATCH/hideRoomById, requesterId={}, roomId={}",
                requesterId, roomId);
        return roomService.hideRoomById(requesterId, roomId);
    }

    @PatchMapping("/{id}/unhide")
    public RoomDto unhideRoomById(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                  @PathVariable("id") Long roomId) {
        log.info("RoomController: PATCH/unhideRoomById, requesterId={}, roomId={}",
                requesterId, roomId);
        return roomService.unhideRoomById(requesterId, roomId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoomById(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                               @PathVariable("id") Long roomId) {
        log.info("RoomController: DELETE/deleteRoomById, requesterId={}, roomId={}", requesterId, roomId);
        roomService.permanentlyDeleteRoomById(requesterId, roomId);
    }
}
