package org.npopov.conference.person;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PersonAuthRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
