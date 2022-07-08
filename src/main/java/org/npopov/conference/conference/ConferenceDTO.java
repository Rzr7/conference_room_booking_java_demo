package org.npopov.conference.conference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class ConferenceDTO {
    @NotEmpty
    private String name;

    @JsonProperty("bookedAt")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime bookedAt;

    @NotNull
    private Integer duration; // in minutes

    @NotNull
    @JsonProperty("roomId")
    private Long roomId;
}
