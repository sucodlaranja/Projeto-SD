package Server.ServerInfo.FlightInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/// This class stores the information of a single flight.
class Flight implements Serializable {

    public static final int DAYS_AVAILABLE = 90;

    /// The identificador of a flight.
    private final int id;
    /// The name from where the flight originates.
    private final String startLocation;
    /// The name where the flight will end.
    private final String destinyLocation;
    /// Has the occupation of each day for the next 90 days.
    private final List<Integer> occupation;
    /// Max occupation of this flight
    private final Integer maxOccupation;

    /// Lock that will allow multiple threads to access this class.
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /// Basic Constructor.
    public Flight(int id, String startLocation, String destinyLocation, int maxOccupation) {
        this.id = id;
        this.startLocation = startLocation;
        this.destinyLocation = destinyLocation;
        this.maxOccupation = maxOccupation;

        occupation = new ArrayList<>();
        for(int i = 0; i < DAYS_AVAILABLE; i++){
            occupation.add(maxOccupation);
        }
    }



    /// Simple get.
    public int getId() {
        readWriteLock.readLock().lock();
        try {
            return id;
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    /// Simple get.
    public String getStartLocation() {
        readWriteLock.readLock().lock();
        try {
            return startLocation;
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    /// Simple get.
    public String getDestinyLocation() {
        readWriteLock.readLock().lock();
        try {
            return destinyLocation;
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * Updates the array \ref occupation. \n
     * Eliminate the days before today. \n
     * Inserts the last days.
     * @param daysSinceLastUpdate days that passed since the last update.
     */
    public void updateOccupation(int daysSinceLastUpdate) {
        readWriteLock.writeLock().lock();
        try {
            for (int i = 0; i < daysSinceLastUpdate; i++){
                occupation.remove(0);
                occupation.add(maxOccupation);
            }
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Removes the reservation if there was any reservation.
     * @param day day when the flight will happen.
     * @return If there was a previous reservation or not.
     */
    public boolean cancelReservation(int day){
        readWriteLock.writeLock().lock();
        try {
            int occ = occupation.get(day);
            if (occ < maxOccupation) {
                occupation.set(day,occ + 1);
                return true;
            }
            else return false;
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Adds the reservation if there is space.
     * @param day day when the flight will happen.
     * @return If there was space or not.
     */
    public boolean addReservation(int day){
        readWriteLock.writeLock().lock();
        try {
            int occ = occupation.get(day);
            if (occ > 0) {
                occupation.set(day,occ - 1);
                return true;
            }
            else return false;
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /// Simple to String
    public String toString(){
        return startLocation + " -> " + destinyLocation;
    }

}
