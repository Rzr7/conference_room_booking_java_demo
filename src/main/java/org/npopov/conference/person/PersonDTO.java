package org.npopov.conference.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class PersonDTO {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;

    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date birthdate;
}
