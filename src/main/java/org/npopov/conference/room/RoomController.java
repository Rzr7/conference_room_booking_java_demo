package org.npopov.conference.room;

import lombok.RequiredArgsConstructor;
import org.npopov.conference.conference.Conference;
import org.npopov.conference.helpers.TimeSlot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<Room>> getRooms() {
        return ResponseEntity.ok(roomService.getRooms());
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoom(roomId));
    }

    @GetMapping("/{roomId}/booked")
    public ResponseEntity<List<TimeSlot>> getBookedTimes(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getBookedTimes(roomId));
    }

    @GetMapping("/{roomId}/conferences")
    public ResponseEntity<Set<Conference>> getRoomConferences(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoomConferences(roomId));
    }

    @PostMapping
    public Room createRoom(@Valid @RequestBody Room roomInput) {
        return roomService.createRoom(roomInput);
    }
}
