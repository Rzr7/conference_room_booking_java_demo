package org.npopov.conference.conference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.npopov.conference.helpers.TimeSlot;
import org.npopov.conference.person.Person;
import org.npopov.conference.room.Room;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain = true)
@Table(name = "conference")
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonProperty("booked_at")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime bookedAt;
    private Integer duration; // in minutes

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person owner;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "conference_persons",
            joinColumns = {@JoinColumn(name = "conference_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "person_id", referencedColumnName = "id")})
    private Set<Person> persons = new HashSet<>();

    @JsonIgnore
    public TimeSlot getTimeSlot() {
        return TimeSlot.createTimeSlot(getBookedAt(), getDuration());
    }

    @PostPersist
    public void logNewConferenceAdded() {
        log.info("Conference " + getName() + "(" + getTimeSlot().toString() + ") - room: " + getRoom().getName() + " created.");
    }

    @PostRemove
    public void logConferenceRemoval() {
        log.info("Deleted conference: " + getName());
    }

    @PostUpdate
    public void logConferenceUpdate() {
        log.info("Updated conference: " + getName());
    }

    public Conference(ConferenceDTO conferenceDTO, Person owner, Room room, Set<Person> personSet) {
        this.name = conferenceDTO.getName();
        this.duration = conferenceDTO.getDuration();
        this.bookedAt = conferenceDTO.getBookedAt();
        this.owner = owner;
        this.room = room;
        this.persons = personSet;
    }
}
