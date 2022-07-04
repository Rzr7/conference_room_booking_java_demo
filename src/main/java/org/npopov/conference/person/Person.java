package org.npopov.conference.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.util.Date;
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

    private String username;

    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd")
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

    public Person(PersonDTO personDTO) {
        this.username = personDTO.getUsername();
        this.name = personDTO.getName();
        this.birthdate = personDTO.getBirthdate();
        this.password = personDTO.getPassword();
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
