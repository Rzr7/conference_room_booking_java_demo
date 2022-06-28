package org.npopov.conference.room;

import lombok.RequiredArgsConstructor;
import org.npopov.conference.conference.Conference;
import org.npopov.conference.exceptions.EntityNotFoundException;
import org.npopov.conference.helpers.TimeSlot;
import org.springframework.stereotype.Service;

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

    public Set<Conference> getRoomConferences(Long roomId) {
        Room room = getRoom(roomId);
        return room.getConferences();
    }

    public Room createRoom(Room roomInput) {
        return roomRepository.save(roomInput);
    }
}
