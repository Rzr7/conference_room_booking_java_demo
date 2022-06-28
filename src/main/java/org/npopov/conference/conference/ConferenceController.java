package org.npopov.netgroup.conference;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
import javax.validation.constraints.NotNull;
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

    @PutMapping
    public ResponseEntity<Conference> editConference(@Valid @RequestBody ConferenceDTO conferenceInput) {
        if (conferenceInput.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Conference conference = conferenceService.editConference(conferenceInput);
        return new ResponseEntity<>(conference, HttpStatus.OK);
    }

    @PostMapping("/{conferenceId}/person")
    public ResponseEntity<String> addPerson(@PathVariable Long conferenceId, @Valid @RequestBody PersonForm personForm) {
        conferenceService.addPerson(conferenceId, personForm.getPersonId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{conferenceId}/person")
    public ResponseEntity<String> removePerson(@PathVariable Long conferenceId, @Valid @RequestBody PersonForm personForm) {
        conferenceService.removePerson(conferenceId, personForm.getPersonId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{conferenceId}")
    public ResponseEntity<String> deleteConference(@PathVariable Long conferenceId) {
        conferenceService.deleteConference(conferenceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Getter
    @Setter
    public static class PersonForm {
        @NotNull
        private Long personId;
    }
}
