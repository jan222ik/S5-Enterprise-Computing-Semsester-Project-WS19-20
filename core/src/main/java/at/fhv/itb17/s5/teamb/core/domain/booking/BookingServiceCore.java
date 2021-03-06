package at.fhv.itb17.s5.teamb.core.domain.booking;

import at.fhv.itb17.s5.teamb.persistence.entities.Ticket;
import org.jetbrains.annotations.Nullable;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface BookingServiceCore {

    @Nullable
    List<Ticket> bookTickets(List<Ticket> tickets);

    @Nullable
    List<Ticket> reserveTickets(List<Ticket> tickets);
}
