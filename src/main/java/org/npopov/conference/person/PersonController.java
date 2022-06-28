package org.npopov.netgroup.person;

import lombok.RequiredArgsConstructor;
import org.npopov.netgroup.conference.Conference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public ResponseEntity<List<Person>> getPersons() {
        return ResponseEntity.ok(personService.getPersons());
    }

    @GetMapping("/{personId}")
    public ResponseEntity<Person> getPerson(@PathVariable Long personId) {
        return ResponseEntity.ok(personService.getPerson(personId));
    }

    @GetMapping("/{personId}/conferences")
    public ResponseEntity<Set<Conference>> getPersonConferences(@PathVariable Long personId) {
        return ResponseEntity.ok(personService.getPersonConferences(personId));
    }

    @PostMapping
    public Person createPerson(@Valid @RequestBody Person personInput) {
        return personService.createPerson(personInput);
    }
}
