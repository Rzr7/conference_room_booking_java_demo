package org.npopov.netgroup.conference;

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
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    @JsonProperty("booked_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bookedAt;

    @NotNull
    private Integer duration; // in minutes

    @NotNull
    @JsonProperty("room_id")
    private Long roomId;

    @NotNull
    @JsonProperty("owner_id")
    private Long ownerId;
}
