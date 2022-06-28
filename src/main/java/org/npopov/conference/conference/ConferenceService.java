package org.npopov.netgroup.conference;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.npopov.netgroup.exceptions.EntityNotFoundException;
import org.npopov.netgroup.exceptions.PersonAlreadyParticipating;
import org.npopov.netgroup.exceptions.RoomIsFullException;
import org.npopov.netgroup.exceptions.TimeNotAvailableException;
import org.npopov.netgroup.person.Person;
import org.npopov.netgroup.person.PersonService;
import org.npopov.netgroup.room.Room;
import org.npopov.netgroup.room.RoomService;
import org.npopov.netgroup.helpers.TimeSlot;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConferenceService {
    private final ConferenceRepository conferenceRepository;
    private final RoomService roomService;
    private final PersonService personService;

    public List<Conference> getConferences() {
        return conferenceRepository.findAll();
    }

    public Conference getConference(Long conferenceId) {
        return conferenceRepository.findById(conferenceId).orElseThrow(() -> new EntityNotFoundException(Conference.class, "id", conferenceId.toString()));
    }

    public Conference createConference(ConferenceDTO conferenceInput) {
        Room room = roomService.getRoom(conferenceInput.getRoomId());
        TimeSlot timeSlot = TimeSlot.createTimeSlot(conferenceInput.getBookedAt(), conferenceInput.getDuration());
        if (!room.isTimeAvailable(timeSlot)) {
            log.info("Time " + timeSlot.toString() + " not available for room " + room.getName() + "(" + room.getId() + ")");
            throw new TimeNotAvailableException(Room.class, "time", timeSlot.toString());
        }

        Person owner = personService.getPerson(conferenceInput.getOwnerId());
        Set<Person> personSet = new HashSet<>();
        personSet.add(owner);
        Conference conference = new Conference()
                .setName(conferenceInput.getName())
                .setDuration(conferenceInput.getDuration())
                .setBookedAt(conferenceInput.getBookedAt())
                .setOwner(owner)
                .setPersons(personSet)
                .setRoom(room);
        return conferenceRepository.save(conference);
    }

    public Conference editConference(ConferenceDTO conferenceInput) {
        Conference conference = getConference(conferenceInput.getId());
        Room room = roomService.getRoom(conferenceInput.getRoomId());

        TimeSlot inputTimeSlot = TimeSlot.createTimeSlot(conferenceInput.getBookedAt(), conferenceInput.getDuration());
        if (!conference.getTimeSlot().equals(inputTimeSlot) && !room.isTimeAvailableForEdit(conference, inputTimeSlot)) {
            log.info("Time " + inputTimeSlot.toString() + " not available for room " + room.getName() + "(" + room.getId() + ")");
            throw new TimeNotAvailableException(Room.class, "time", inputTimeSlot.toString());
        }

        Person owner = personService.getPerson(conferenceInput.getOwnerId());
        Set<Person> personSet = conference.getPersons();
        personSet.add(owner);
        conference
                .setName(conferenceInput.getName())
                .setDuration(conferenceInput.getDuration())
                .setBookedAt(conferenceInput.getBookedAt())
                .setOwner(owner)
                .setPersons(personSet)
                .setRoom(room);
        return conferenceRepository.save(conference);
    }

    public void addPerson(Long conferenceId, Long personId) {
        Conference conference = getConference(conferenceId);
        Person person = personService.getPerson(personId);
        Room room = conference.getRoom();

        Set<Person> personSet = conference.getPersons();
        if (personSet.contains(person)) {
            log.info("Person " + person.getName() + " already participating in conference " + conference.getName());
            throw new PersonAlreadyParticipating(Person.class, "name", person.getName());
        }

        if (conference.getPersons().size() == room.getCapacity()) {
            log.info("Room is full " + room.getName() + "(capacity: " + room.getCapacity() + ")");
            throw new RoomIsFullException(Room.class, "capacity", room.getCapacity().toString());
        }

        personSet.add(person);
        conference.setPersons(personSet);
        conferenceRepository.save(conference);
    }

    public void removePerson(Long conferenceId, Long personId) {
        Conference conference = getConference(conferenceId);
        Set<Person> personSet = conference.getPersons().stream().filter(person -> !person.getId().equals(personId)).collect(Collectors.toSet());
        conference.setPersons(personSet);
        conferenceRepository.save(conference);
    }

    public void deleteConference(Long conferenceId) {
        conferenceRepository.deleteById(conferenceId);
    }
}
