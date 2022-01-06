package Server.ServerInfo.FlightInfo;

import java.time.LocalDate;
import java.util.List;

/// This interface will allow other classes to use the FlightFacade component.
public interface IFlightFacade {

    /// Adds a flight to the flight collection.
    void addFlight(String startLocation, String destinyLocation, int maxOccupation);

    /**
     * Finds all possible paths from startLocation to destinyLocation.
     * @param startLocation Location where the path will start.
     * @param destinyLocation Location where the path will end.
     * @return all the possible paths.
     */
    List<List<String>> findAllPossiblePaths(String startLocation, String destinyLocation);

    /**
     * Adds a reservation to the system, if there is space in the interval of time.
     * @param destinations The course that the flights will go through.
     * @param startDate The oldest dates of the interval of time.
     * @param endDate The newest dates of the interval of time.
     * @return The id of the reservation.
     * @throws FlightNotAvailable If there was some problem in the reservation.
     */
    int addReservation(List<String> destinations, LocalDate startDate, LocalDate endDate) throws FlightNotAvailable, WrongDate;

    /**
     * Removes a reservation from the system.
     * @param idReservation Id of the Reservation.
     * @throws ReservationNotAvailable If the reservation could not be removed.
     */
    void removeReservation(int idReservation) throws ReservationNotAvailable, WrongDate;

    /**
     * This method will allow this system to know that no more operation is allowed today \n
     * @return If there was change in the boolean or not.
     */
    boolean endDay(LocalDate day) throws WrongDate;

    /**
     * Gets a list of all the possible flights.
     * @return A list of all the flights.
     */
    List<String> getAllFlights();

    String reservationToString(int reservation);

    /// Saves all the info on a file
    void saveInfo();
}
