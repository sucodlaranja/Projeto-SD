package Server.ServerInfo.FlightInfo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

}
