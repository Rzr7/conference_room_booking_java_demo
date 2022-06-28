package org.npopov.conference.helpers;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class TimeSlot {
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime from;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime to;

    public static boolean isOverlapping(TimeSlot t1, TimeSlot t2) {
        // (StartDateA <= EndDateB) and (EndDateA >= StartDateB)
        return t1.getFrom().isBefore(t2.getTo()) && t1.getTo().isAfter(t2.getFrom());
    }

    public static TimeSlot createTimeSlot(LocalDateTime from, Integer duration) {
        return new TimeSlot()
                .setFrom(from)
                .setTo(from.plusMinutes(duration));
    }

    @Override
    public String toString() {
        return from + "-" + to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeSlot timeSlot = (TimeSlot) o;

        if (!getFrom().equals(timeSlot.getFrom())) return false;
        return getTo().equals(timeSlot.getTo());
    }

    @Override
    public int hashCode() {
        int result = getFrom().hashCode();
        result = 31 * result + getTo().hashCode();
        return result;
    }
}
