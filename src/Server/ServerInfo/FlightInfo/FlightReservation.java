package Server.ServerInfo.FlightInfo;

import java.time.LocalDate;
import java.util.List;

/// This class stores the information of a single flight reservation.
public record FlightReservation(int idReservation, List<Integer> idsFlight, LocalDate dateOfReservation) {
}
