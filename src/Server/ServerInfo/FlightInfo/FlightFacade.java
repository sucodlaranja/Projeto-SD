package Server.ServerInfo.FlightInfo;

import Server.ServerInfo.DataWriteRead;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/// This class will manage all the Flight in the system.
public class FlightFacade implements IFlightFacade {

    private static final int MAX_DEPTH_SEARCH = 3;

    /// This Map contains all the flights of the system.
    private Map<Integer, Flight> flights;
    /// Determines the new flight's id.
    private int nextFlightId;
    /// This Map contains all the reservations of the system.
    private Map<Integer, FlightReservation> flightReservations;
    /// Determines the new reservation's id.
    private int nextReservationId;
    /// Date when the system flight's occupation was last updated.
    private LocalDate lastUpdated;

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

    /// Adds a flight to the flight collection.
    public void addFlight(String startLocation, String destinyLocation, int maxOccupation){
        readWriteLock.writeLock().lock();
        try {
            flights.put(nextFlightId,new Flight(nextFlightId,startLocation,destinyLocation,maxOccupation));
            nextFlightId++;
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Adds a reservation to the system, if there is space in the interval of time.
     * @param destinations The course that the flights will go through.
     * @param startDate The oldest dates of the interval of time.
     * @param endDate The newest dates of the interval of time.
     * @return The id of the reservation.
     * @throws FlightNotAvailable If there was some problem in the reservation.
     */
    public int addReservation(List<String> destinations,LocalDate startDate,LocalDate endDate) throws FlightNotAvailable {
        List<Integer> flightsIds = getAllIdsFlights(destinations);

        while (endDate.compareTo(startDate) >= 0 && !reserveIfAvailable(flightsIds,startDate))
            startDate = startDate.plusDays(1);

        if (endDate.compareTo(startDate) >= 0)
            throw new FlightNotAvailable("There isn´t the seats available for this course!\n");

        readWriteLock.writeLock().lock();
        try {
            flightReservations.put(nextReservationId,new FlightReservation(nextReservationId,flightsIds,startDate));
            return nextReservationId++;
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Removes a reservation from the system.
     * @param idReservation Id of the Reservation.
     * @throws ReservationNotAvailable If the reservation could not be removed.
     */
    public void removeReservation(int idReservation) throws ReservationNotAvailable {
        readWriteLock.writeLock().lock();
        try {
            FlightReservation flightRes = flightReservations.remove(idReservation);
            if (flightRes == null) throw new  ReservationNotAvailable("Reservation does not exist.\n");
            else{
                int days = (int) lastUpdated.until(flightRes.dateOfReservation(),ChronoUnit.DAYS);
                for(int flightsIds : flightRes.idsFlight()){
                    if (!flights.get(flightsIds).cancelReservation(days))
                        throw new  ReservationNotAvailable("Data does not match! Something is wrong.\n");
                }
            }
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Finds all possible paths from startLocation to destinyLocation.
     * @param startLocation Location where the path will start.
     * @param destinyLocation Location where the path will end.
     * @return all the possible paths.
     */
    public List<List<String>> findAllPossiblePaths(String startLocation, String destinyLocation){
        List<List<String>> allPaths = new ArrayList<>();

        List<List<String>> expandMatrix = new ArrayList<>();
        List<String> firstPath = new ArrayList<>();
        firstPath.add(startLocation);
        expandMatrix.add(new ArrayList<>(firstPath));
        for(int i=0; i< MAX_DEPTH_SEARCH; i++){
            expandMatrix = evolvePossiblePaths(expandMatrix);
            List<List<String>> solutions = expandMatrix.stream()
                    .filter(list -> list.get(0).equals(destinyLocation)).collect(Collectors.toList());
            expandMatrix.removeAll(solutions);
            solutions.forEach(Collections::reverse);
            allPaths.addAll(solutions);
        }

        if (allPaths.isEmpty()){
            List<String> l = new ArrayList<>();
            l.add("There isn't any flights available!\n");
            allPaths.add(l);
        }
        return allPaths;
    }

    /// Private methods

    /**
     * Reserves the seats if there is space in \b all the flights.
     * @param flightsIds Every flight's id to be reserved.
     * @param localDate Day of the reserve.
     * @return If there was a reserve or not.
     */
    private boolean reserveIfAvailable(List<Integer> flightsIds,LocalDate localDate){
        updateFlightsOccupation();
        int days = (int) lastUpdated.until(localDate,ChronoUnit.DAYS);
        List<Integer> remove = new ArrayList<>();
        readWriteLock.writeLock().lock();
        try {
            for(Integer idAdd :flightsIds){
                if(flights.get(idAdd).addReservation(days)) remove.add(idAdd);
                else{
                    for (Integer idRemove : remove){
                        flights.get(idRemove).cancelReservation(days);
                    }
                    return false;
                }

            }
            return true;
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Finds all the ids of the flights needed to travel between all destinations.
     * @param destinations The list of destinations.
     * @return All the ids of the Flights.
     * @throws FlightNotAvailable If this course can't be made.
     */
    private List<Integer> getAllIdsFlights(List<String> destinations) throws FlightNotAvailable{
        if (destinations.size() < 1) throw new FlightNotAvailable("Need more destinations!\n");
        List<Integer> result = new ArrayList<>();
        String previous = destinations.get(0);
        for (String destination : destinations){
            int id = getIdFlights(previous,destination);
            if (id == -1) throw new FlightNotAvailable("No flight from " + previous + " to " + destination + ".\n");
            result.add(id);
            previous = destination;
        }
        return result;
    }

    /**
     * Takes every path in the old matrix and creates all the new possible paths.
     * @param matrix old matrix.
     * @return new matriz.
     */
    private List<List<String>> evolvePossiblePaths(List<List<String>> matrix){
        List<List<String>> newMatrix = new ArrayList<>();

        for(List<String> path: matrix){
            String startLocation = path.get(0);
            List<String> possibleDestinations = getPossibleDestinations(startLocation);
            for(String destination : possibleDestinations) {
                if (!path.contains(destination)) {
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(0, destination);
                    newMatrix.add(newPath);
                }
            }
        }

        return newMatrix;
    }

    /**
     * Finds the all possible destinations.
     * @param startLocation Location where the flight will start.
     * @return The list of destinations.
     */
    private List<String> getPossibleDestinations(String startLocation){
        readWriteLock.readLock().lock();
        try {
            return flights.values().stream()
                    .filter(flight -> flight.getStartLocation().equals(startLocation))
                    .map(Flight::getDestinyLocation).collect(Collectors.toList());
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * Finds the id off a flight that corresponds to the given parameters.
     * @param startLocation Location where the flight will start.
     * @param destinyLocation Location where the flight will end.
     * @return The id of the flight or -1 in case it does not exist.
     */
    private int getIdFlights(String startLocation, String destinyLocation){
        readWriteLock.readLock().lock();
        try {
            return flights.values().stream()
                    .filter(flight -> flight.getStartLocation().equals(startLocation)
                            && flight.getDestinyLocation().equals(destinyLocation))
                    .map(Flight::getId)
                    .findAny().orElse(-1);
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * Updates all the information on the \ref flights collection. \n
     * Calculates the days that passed since the last update and applies it to the \ref updateOccupation in \ref flight.
      */
    private void updateFlightsOccupation(){
        readWriteLock.readLock().lock();
        try {
            LocalDate now = LocalDateTime.now().toLocalDate();
            int days = (int) lastUpdated.until(now, ChronoUnit.DAYS);
            flights.values().forEach(flight -> flight.updateOccupation(days));
            lastUpdated = now;
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }



}
