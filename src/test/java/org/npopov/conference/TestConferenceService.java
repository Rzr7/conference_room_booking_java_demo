package org.npopov.conference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.npopov.conference.conference.Conference;
import org.npopov.conference.conference.ConferenceDTO;
import org.npopov.conference.conference.ConferenceRepository;
import org.npopov.conference.conference.ConferenceService;
import org.npopov.conference.exceptions.PersonAlreadyParticipatingException;
import org.npopov.conference.exceptions.RoomIsFullException;
import org.npopov.conference.exceptions.TimeNotAvailableException;
import org.npopov.conference.person.Person;
import org.npopov.conference.person.PersonService;
import org.npopov.conference.room.Room;
import org.npopov.conference.room.RoomService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestConferenceService {

    @InjectMocks
    ConferenceService conferenceService;

    @Mock
    ConferenceRepository conferenceRepository;

    @Mock
    PersonService personService;

    @Mock
    RoomService roomService;

    Person person;
    Person person2;
    Person person3;
    Person person4;
    Room room;
    ConferenceDTO conferenceDTO;

    @BeforeEach
    public void init() {
        try {
            person = new Person()
                    .setId(1L)
                    .setName("John Doe")
                    .setUsername("john1")
                    .setBirthdate(new SimpleDateFormat("yyyy-MM-dd").parse("1999-02-11"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        try {
            person2 = new Person()
                    .setId(2L)
                    .setName("John Doe 2")
                    .setUsername("john2")
                    .setBirthdate(new SimpleDateFormat("yyyy-MM-dd").parse("1999-02-11"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        try {
            person3 = new Person()
                    .setId(3L)
                    .setName("John Doe 3")
                    .setUsername("john3")
                    .setBirthdate(new SimpleDateFormat("yyyy-MM-dd").parse("1999-02-12"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        try {
            person4 = new Person()
                    .setId(4L)
                    .setName("John Doe 4")
                    .setUsername("john4")
                    .setBirthdate(new SimpleDateFormat("yyyy-MM-dd").parse("1999-02-13"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        room = new Room()
                .setId(1L)
                .setName("Room 1")
                .setLocation("Office")
                .setCapacity(3);

        conferenceDTO = new ConferenceDTO()
                .setName("Created conference")
                .setBookedAt(LocalDateTime.now())
                .setDuration(60)
                .setRoomId(room.getId());

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(person.getUsername());
        when(personService.getPerson("john1")).thenReturn(person);
        when(personService.getPerson(1L)).thenReturn(person);
        when(personService.getPerson("john2")).thenReturn(person2);
        when(personService.getPerson(2L)).thenReturn(person2);
        when(personService.getPerson("john3")).thenReturn(person3);
        when(personService.getPerson(3L)).thenReturn(person3);
        when(personService.getPerson("john4")).thenReturn(person4);
        when(personService.getPerson(4L)).thenReturn(person4);
        when(roomService.getRoom(anyLong())).thenReturn(room);
        when(conferenceRepository.save(any(Conference.class))).thenAnswer(i -> i.getArguments()[0]);
    }



    @Test
    public void createConferenceTest()
    {
        Conference createdConference = conferenceService.createConference(conferenceDTO);
        assertEquals(conferenceDTO.getName(), createdConference.getName());
        assertEquals(conferenceDTO.getRoomId(), createdConference.getRoom().getId());
        assertEquals(person.getId(), createdConference.getOwner().getId());
        assertEquals(conferenceDTO.getDuration(), createdConference.getDuration());
        assertEquals(conferenceDTO.getBookedAt(), createdConference.getBookedAt());
    }

    @Test
    public void editConferenceTest()
    {
        Conference createdConference = conferenceService.createConference(conferenceDTO);
        createdConference.setId(1L);

        when(conferenceRepository.findById(anyLong())).thenReturn(Optional.of(createdConference));

        ConferenceDTO conferenceDTO2 = new ConferenceDTO()
                .setName("new name")
                .setRoomId(createdConference.getRoom().getId())
                .setBookedAt(LocalDateTime.now().plusMinutes(30))
                .setDuration(90);

        Conference updatedConference = conferenceService.editConference(createdConference.getId(), conferenceDTO2);
        assertEquals(createdConference.getId(), updatedConference.getId());
        assertEquals(conferenceDTO2.getName(), updatedConference.getName());
        assertEquals(conferenceDTO.getRoomId(), updatedConference.getRoom().getId());
        assertEquals(person.getId(), updatedConference.getOwner().getId());
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
        conferenceDTO2.setRoomId(room.getId());

        when(roomService.getRoom(anyLong())).thenReturn(room);
        when(conferenceRepository.save(any(Conference.class))).thenAnswer(i -> i.getArguments()[0]);

        Conference createdConference = conferenceService.createConference(conferenceDTO);
        room.getConferences().add(createdConference);

        assertThrows(TimeNotAvailableException.class, () -> conferenceService.createConference(conferenceDTO2));
    }

    @Test
    public void addPersonConferenceTest() {
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
        Conference createdConference = conferenceService.createConference(conferenceDTO);
        createdConference.setId(1L);
        room.getConferences().add(createdConference);
        when(conferenceRepository.findById(anyLong())).thenReturn(Optional.of(createdConference));

        assertEquals(1, createdConference.getPersons().size());
        assertThrows(PersonAlreadyParticipatingException.class, () -> conferenceService.addPerson(createdConference.getId(), 1L));
        assertEquals(1, createdConference.getPersons().size());
    }

    @Test
    public void fullRoomConferenceTest() {
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
    public void removePersonConferenceTest() {
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
