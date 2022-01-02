package Server.ServerInfo.FlightInfo;

import Server.ServerInfo.DataWriteRead;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/// This class will manage all the Flight in the system.
public class FlightFacade {

    /// This Map contains all the flights of the system.
    private Map<Integer, Flight> flights;
    /// Determines the new flight's id.
    private int nextFlightId;
    /// This Map contains all the reservations of the system.
    private Map<Integer, FlightReservation> flightReservations;
    /// Determines the new reservation's id.
    private int nextReservationId;
    /// Date when the system flight's occupation was last updated.
    private LocalDateTime lastUpdated;

    /// Lock that will allow multiple threads to access this class.
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /// Constructor that will read a file containing /ref Flights.
    public FlightFacade(){
        flights = DataWriteRead.getInstanceHashMap("flights");
        lastUpdated = DataWriteRead.getInstanceOtherInformation("flights");
        flightReservations = DataWriteRead.getInstanceHashMap("flightReservations");

        nextFlightId = DataWriteRead.maxId(flights.keySet());
        nextReservationId = DataWriteRead.maxId(flightReservations.keySet());
    }



}
