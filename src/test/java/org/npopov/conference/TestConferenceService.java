package org.npopov.netgroup;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.npopov.netgroup.conference.Conference;
import org.npopov.netgroup.conference.ConferenceDTO;
import org.npopov.netgroup.conference.ConferenceRepository;
import org.npopov.netgroup.conference.ConferenceService;
import org.npopov.netgroup.exceptions.PersonAlreadyParticipating;
import org.npopov.netgroup.exceptions.RoomIsFullException;
import org.npopov.netgroup.exceptions.TimeNotAvailableException;
import org.npopov.netgroup.person.Person;
import org.npopov.netgroup.person.PersonService;
import org.npopov.netgroup.room.Room;
import org.npopov.netgroup.room.RoomService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestConferenceService {

    @InjectMocks
    ConferenceService conferenceService;

    @Mock
    ConferenceRepository conferenceRepository;

    @Mock
    PersonService personService;

    @Mock
    RoomService roomService;

    Person person = new Person()
            .setId(1L)
            .setName("John Doe")
            .setBirthdate(Date.valueOf(LocalDate.of(1999, Month.FEBRUARY, 11)));
    Room room = new Room()
            .setId(1L)
            .setName("Room 1")
            .setLocation("Office")
            .setCapacity(3);

    ConferenceDTO conferenceDTO = new ConferenceDTO()
            .setName("Created conference")
            .setBookedAt(LocalDateTime.now())
            .setDuration(60)
            .setOwnerId(person.getId())
            .setRoomId(room.getId());

    @Test
    public void createConferenceTest()
    {
        when(personService.getPerson(anyLong())).thenReturn(person);
        when(roomService.getRoom(anyLong())).thenReturn(room);
        when(conferenceRepository.save(any(Conference.class))).thenAnswer(i -> i.getArguments()[0]);

        Conference createdConference = conferenceService.createConference(conferenceDTO);
        assertEquals(conferenceDTO.getName(), createdConference.getName());
        assertEquals(conferenceDTO.getRoomId(), createdConference.getRoom().getId());
        assertEquals(conferenceDTO.getOwnerId(), createdConference.getOwner().getId());
        assertEquals(conferenceDTO.getDuration(), createdConference.getDuration());
        assertEquals(conferenceDTO.getBookedAt(), createdConference.getBookedAt());
    }

    @Test
    public void editConferenceTest()
    {
        when(personService.getPerson(anyLong())).thenReturn(person);
        when(roomService.getRoom(anyLong())).thenReturn(room);
        when(conferenceRepository.save(any(Conference.class))).thenAnswer(i -> i.getArguments()[0]);

        Conference createdConference = conferenceService.createConference(conferenceDTO);
        createdConference.setId(1L);

        when(conferenceRepository.findById(anyLong())).thenReturn(Optional.of(createdConference));

        ConferenceDTO conferenceDTO2 = new ConferenceDTO()
                .setId(createdConference.getId())
                .setName("new name")
                .setOwnerId(createdConference.getOwner().getId())
                .setRoomId(createdConference.getRoom().getId())
                .setBookedAt(LocalDateTime.now().plusMinutes(30))
                .setDuration(90);

        Conference updatedConference = conferenceService.editConference(conferenceDTO2);
        assertEquals(createdConference.getId(), updatedConference.getId());
        assertEquals(conferenceDTO2.getName(), updatedConference.getName());
        assertEquals(conferenceDTO.getRoomId(), updatedConference.getRoom().getId());
        assertEquals(conferenceDTO.getOwnerId(), updatedConference.getOwner().getId());
        assertEquals(conferenceDTO2.getDuration(), updatedConference.getDuration());
        assertEquals(conferenceDTO2.getBookedAt(), updatedConference.getBookedAt());
    }

    @Test
    public void overlappingConferencesTest()
    {
        ConferenceDTO conferenceDTO2 = new ConferenceDTO();
        conferenceDTO2.setName("Overlapping conference");
        conferenceDTO2.setBookedAt(LocalDateTime.now().plusMinutes(30));
        conferenceDTO2.setDuration(60);
        conferenceDTO2.setOwnerId(person.getId());
        conferenceDTO2.setRoomId(room.getId());

        when(personService.getPerson(anyLong())).thenReturn(person);
        when(roomService.getRoom(anyLong())).thenReturn(room);
        when(conferenceRepository.save(any(Conference.class))).thenAnswer(i -> i.getArguments()[0]);

        Conference createdConference = conferenceService.createConference(conferenceDTO);
        room.getConferences().add(createdConference);

        assertThrows(TimeNotAvailableException.class, () -> conferenceService.createConference(conferenceDTO2));
    }

    @Test
    public void addPersonConferenceTest()
    {
        Person person2 = new Person()
                .setId(2L)
                .setName("John Doe 2")
                .setBirthdate(Date.valueOf(LocalDate.of(1999, Month.FEBRUARY, 12)));

        when(personService.getPerson(1L)).thenReturn(person);
        when(personService.getPerson(2L)).thenReturn(person2);
        when(roomService.getRoom(anyLong())).thenReturn(room);
        when(conferenceRepository.save(any(Conference.class))).thenAnswer(i -> i.getArguments()[0]);

        Conference createdConference = conferenceService.createConference(conferenceDTO);
        createdConference.setId(1L);
        room.getConferences().add(createdConference);

        when(conferenceRepository.findById(anyLong())).thenReturn(Optional.of(createdConference));
        assertEquals(1, createdConference.getPersons().size());
        conferenceService.addPerson(createdConference.getId(), 2L);
        assertEquals(2, createdConference.getPersons().size());
    }

    @Test
    public void addSamePersonConferenceTest()
    {
        when(personService.getPerson(1L)).thenReturn(person);
        when(roomService.getRoom(anyLong())).thenReturn(room);
        when(conferenceRepository.save(any(Conference.class))).thenAnswer(i -> i.getArguments()[0]);

        Conference createdConference = conferenceService.createConference(conferenceDTO);
        createdConference.setId(1L);
        room.getConferences().add(createdConference);
        when(conferenceRepository.findById(anyLong())).thenReturn(Optional.of(createdConference));

        assertEquals(1, createdConference.getPersons().size());
        assertThrows(PersonAlreadyParticipating.class, () -> conferenceService.addPerson(createdConference.getId(), 1L));
        assertEquals(1, createdConference.getPersons().size());
    }

    @Test
    public void fullRoomConferenceTest()
    {
        Person person2 = new Person()
                .setId(2L)
                .setName("John Doe 2")
                .setBirthdate(Date.valueOf(LocalDate.of(1999, Month.FEBRUARY, 12)));

        Person person3 = new Person()
                .setId(3L)
                .setName("John Doe 3")
                .setBirthdate(Date.valueOf(LocalDate.of(1999, Month.FEBRUARY, 13)));

        Person person4 = new Person()
                .setId(4L)
                .setName("John Doe 4")
                .setBirthdate(Date.valueOf(LocalDate.of(1999, Month.FEBRUARY, 14)));

        when(personService.getPerson(1L)).thenReturn(person);
        when(personService.getPerson(2L)).thenReturn(person2);
        when(personService.getPerson(3L)).thenReturn(person3);
        when(personService.getPerson(4L)).thenReturn(person4);
        when(roomService.getRoom(anyLong())).thenReturn(room);
        when(conferenceRepository.save(any(Conference.class))).thenAnswer(i -> i.getArguments()[0]);

        Conference createdConference = conferenceService.createConference(conferenceDTO);
        createdConference.setId(1L);
        room.getConferences().add(createdConference);

        when(conferenceRepository.findById(anyLong())).thenReturn(Optional.of(createdConference));
        assertEquals(1, createdConference.getPersons().size());

        conferenceService.addPerson(createdConference.getId(), 2L);
        conferenceService.addPerson(createdConference.getId(), 3L);

        assertEquals(3, createdConference.getPersons().size());
        assertThrows(RoomIsFullException.class, () -> conferenceService.addPerson(createdConference.getId(), 4L));
        assertEquals(3, createdConference.getPersons().size());
    }

    @Test
    public void removePersonConferenceTest()
    {
        Person person2 = new Person()
                .setId(2L)
                .setName("John Doe 2")
                .setBirthdate(Date.valueOf(LocalDate.of(1999, Month.FEBRUARY, 12)));

        Person person3 = new Person()
                .setId(3L)
                .setName("John Doe 3")
                .setBirthdate(Date.valueOf(LocalDate.of(1999, Month.FEBRUARY, 13)));

        when(personService.getPerson(1L)).thenReturn(person);
        when(personService.getPerson(2L)).thenReturn(person2);
        when(personService.getPerson(3L)).thenReturn(person3);
        when(roomService.getRoom(anyLong())).thenReturn(room);
        when(conferenceRepository.save(any(Conference.class))).thenAnswer(i -> i.getArguments()[0]);

        Conference createdConference = conferenceService.createConference(conferenceDTO);
        createdConference.setId(1L);
        room.getConferences().add(createdConference);

        when(conferenceRepository.findById(anyLong())).thenReturn(Optional.of(createdConference));
        assertEquals(1, createdConference.getPersons().size());

        conferenceService.addPerson(createdConference.getId(), 2L);
        conferenceService.addPerson(createdConference.getId(), 3L);
        assertEquals(3, createdConference.getPersons().size());

        conferenceService.removePerson(createdConference.getId(), 2L);

        assertEquals(2, createdConference.getPersons().size());
        assertTrue(createdConference.getPersons().stream().noneMatch(p -> p.getId().equals(2L)));
    }
}
