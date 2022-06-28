package org.npopov.netgroup.person;

import lombok.RequiredArgsConstructor;
import org.npopov.netgroup.conference.Conference;
import org.npopov.netgroup.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    public Person getPerson(Long personId) {
        return personRepository.findById(personId).orElseThrow(() -> new EntityNotFoundException(Person.class, "id", personId.toString()));
    }

    public Set<Conference> getPersonConferences(Long personId) {
        Person person = getPerson(personId);
        return person.getConferences();
    }

    public Person createPerson(Person personInput) {
        return personRepository.save(personInput);
    }
}
