package org.npopov.conference.person;

import lombok.RequiredArgsConstructor;
import org.npopov.conference.conference.Conference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/info")
    public Person getUserDetails() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return personService.getPerson(username);
    }

    @GetMapping("/{personId}")
    public ResponseEntity<Person> getPerson(@PathVariable Long personId) {
        return ResponseEntity.ok(personService.getPerson(personId));
    }

    @GetMapping("/{personId}/conferences")
    public ResponseEntity<Set<Conference>> getPersonConferences(@PathVariable Long personId) {
        return ResponseEntity.ok(personService.getPersonConferences(personId));
    }
}
