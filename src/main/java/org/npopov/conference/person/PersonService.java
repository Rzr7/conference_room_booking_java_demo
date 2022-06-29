package org.npopov.conference.person;

import lombok.RequiredArgsConstructor;
import org.npopov.conference.conference.Conference;
import org.npopov.conference.exceptions.EntityNotFoundException;
import org.npopov.conference.exceptions.UserAlreadyExistsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    public Person getPerson(Long personId) {
        return personRepository.findById(personId).orElseThrow(() -> new EntityNotFoundException(Person.class, "id", personId.toString()));
    }

    public Person getPerson(String username) {
        return personRepository.findByUsername(username);
    }

    public Set<Conference> getPersonConferences(Long personId) {
        Person person = getPerson(personId);
        return person.getConferences();
    }

    public Person createPerson(PersonDTO personDTO) {
        if (personAlreadyExist(personDTO.getUsername())) {
            throw new UserAlreadyExistsException(Person.class, "username", personDTO.getUsername());
        }

        personDTO.setPassword(bCryptPasswordEncoder
                .encode(personDTO.getPassword()));
        return personRepository.save(new Person(personDTO));
    }

    private boolean personAlreadyExist(String username) {
        return personRepository.findByUsername(username) != null;
    }
}
