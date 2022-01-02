package Server.ServerInfo.FlightInfo;

import java.time.LocalDateTime;

/// This class stores the information of a single flight reservation.
public record FlightReservation(int idReservation, int idFlight, LocalDateTime dateOfReservation) {
}
