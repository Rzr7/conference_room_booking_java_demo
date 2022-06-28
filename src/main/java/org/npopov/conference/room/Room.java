package org.npopov.conference.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.npopov.conference.conference.Conference;
import org.npopov.conference.exceptions.EntityNotFoundException;
import org.npopov.conference.helpers.TimeSlot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Entity
@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String location;

    @NotNull
    private Integer capacity;

    @OneToMany(mappedBy="room")
    @JsonIgnore
    private Set<Conference> conferences = new HashSet<>();

    @JsonIgnore
    public Set<Conference> getFutureConferences() {
        return conferences.stream()
                .filter(conference -> conference.getBookedAt()
                        .plusMinutes(conference.getDuration())
                        .isAfter(LocalDateTime.now()))
                .collect(Collectors.toSet());
    }

    public boolean isTimeAvailable(TimeSlot timeSlot) throws EntityNotFoundException {
        return getFutureConferences().stream()
                .noneMatch(conference -> TimeSlot.isOverlapping(conference.getTimeSlot(), timeSlot));
    }

    public boolean isTimeAvailableForEdit(Conference conference, TimeSlot timeSlot) throws EntityNotFoundException {
        return getFutureConferences().stream()
                .filter(conference1 -> !conference.getId().equals(conference1.getId()))
                .noneMatch(conference1 -> TimeSlot.isOverlapping(conference1.getTimeSlot(), timeSlot));
    }

    @PostPersist
    public void logNewRoomAdded() {
        log.info("Room " + getName()  + " created.");
    }

    @PostRemove
    public void logRoomRemoval() {
        log.info("Deleted room: " + getName());
    }

    @PostUpdate
    public void logRoomUpdate() {
        log.info("Updated room: " + getName());
    }
}
