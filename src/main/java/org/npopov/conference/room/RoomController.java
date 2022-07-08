package org.npopov.conference.room;

import lombok.RequiredArgsConstructor;
import org.npopov.conference.conference.Conference;
import org.npopov.conference.helpers.TimeSlot;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @PutMapping("/{roomId}")
    public ResponseEntity<Room> editRoom(@PathVariable Long roomId, @Valid @RequestBody Room roomInput) {
        Room room = roomService.editRoom(roomId, roomInput);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{roomId}/booked")
    public ResponseEntity<List<TimeSlot>> getBookedTimes(@PathVariable Long roomId, @RequestParam("date") String dateString) {
        if (!dateString.isEmpty()) {
            Date dateTime;
            try {
                dateTime = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            } catch (ParseException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok(roomService.getBookedTimes(roomId, dateTime));
        }
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
