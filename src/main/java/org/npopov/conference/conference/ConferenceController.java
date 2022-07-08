package org.npopov.conference.conference;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/conference")
@RequiredArgsConstructor
public class ConferenceController {
    private final ConferenceService conferenceService;

    @GetMapping
    public ResponseEntity<List<Conference>> getConferences() {
        return ResponseEntity.ok(conferenceService.getConferences());
    }

    @GetMapping("/{conferenceId}")
    public ResponseEntity<Conference> getConference(@PathVariable Long conferenceId) {
        return ResponseEntity.ok(conferenceService.getConference(conferenceId));
    }

    @PostMapping
    public ResponseEntity<Conference> createConference(@Valid @RequestBody ConferenceDTO conferenceInput) {
        Conference conference = conferenceService.createConference(conferenceInput);
        return new ResponseEntity<>(conference, HttpStatus.CREATED);
    }

    @PutMapping("/{conferenceId}/owner/{personId}")
    public ResponseEntity<Conference> transferOwnership(@PathVariable Long conferenceId, @PathVariable Long personId) {
        Conference conference = conferenceService.transferOwnership(conferenceId, personId);
        return new ResponseEntity<>(conference, HttpStatus.OK);
    }

    @PutMapping("/{conferenceId}")
    public ResponseEntity<Conference> editConference(@PathVariable Long conferenceId, @Valid @RequestBody ConferenceDTO conferenceInput) {
        Conference conference = conferenceService.editConference(conferenceId, conferenceInput);
        return new ResponseEntity<>(conference, HttpStatus.OK);
    }

    @PostMapping("/{conferenceId}/person/{personId}")
    public ResponseEntity<String> addPerson(@PathVariable Long conferenceId, @PathVariable Long personId) {
        conferenceService.addPerson(conferenceId, personId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{conferenceId}")
    public ResponseEntity<String> deleteConference(@PathVariable Long conferenceId) {
        conferenceService.deleteConference(conferenceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{conferenceId}/person/{personId}")
    public ResponseEntity<String> removePerson(@PathVariable Long conferenceId, @PathVariable Long personId) {
        conferenceService.removePerson(conferenceId, personId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
