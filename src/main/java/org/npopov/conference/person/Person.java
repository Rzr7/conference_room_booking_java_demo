package org.npopov.conference.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.npopov.conference.conference.Conference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Entity
@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date birthdate;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name="conference_persons",
            joinColumns={@JoinColumn(name="person_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="conference_id", referencedColumnName="id")})
    private Set<Conference> conferences = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return getId().equals(person.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @PostPersist
    public void logNewPersonAdded() {
        log.info("Person " + getName()  + " created.");
    }

    @PostRemove
    public void logPersonRemoval() {
        log.info("Deleted person: " + getName());
    }

    @PostUpdate
    public void logPersonUpdate() {
        log.info("Updated person: " + getName());
    }
}
