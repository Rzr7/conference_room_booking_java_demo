package org.npopov.conference.room;

import lombok.RequiredArgsConstructor;
import org.npopov.conference.conference.Conference;
import org.npopov.conference.exceptions.EntityNotFoundException;
import org.npopov.conference.helpers.TimeSlot;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    public Room getRoom(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException(Room.class, "id", roomId.toString()));
    }

    public List<TimeSlot> getBookedTimes(Long roomId) {
        return getRoom(roomId)
                .getFutureConferences().stream()
                .map(Conference::getTimeSlot)
                .toList();
    }

    public List<TimeSlot> getBookedTimes(Long roomId, Date date) {
        return getRoom(roomId)
                .getConferencesForDate(date).stream()
                .map(Conference::getTimeSlot)
                .toList();
    }

    public Set<Conference> getRoomConferences(Long roomId) {
        Room room = getRoom(roomId);
        return room.getConferences();
    }

    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }

    public Room createRoom(Room roomInput) {
        return roomRepository.save(roomInput);
    }

    public Room editRoom(Long roomId, Room roomInput) {
        Room room = getRoom(roomId);
        room.setCapacity(roomInput.getCapacity());
        room.setName(roomInput.getName());
        room.setLocation(roomInput.getLocation());
        return roomRepository.save(room);
    }
}
