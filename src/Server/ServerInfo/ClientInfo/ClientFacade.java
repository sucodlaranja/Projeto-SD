package Server.ServerInfo.ClientInfo;

import Server.ServerInfo.DataWriteRead;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/// This class will manage all the clients in the system.
public class ClientFacade implements IClientFacade {

    /// This Map contains all the clients of the system.
    private final Map<String,Client> clients;

    /// Lock that will allow multiple threads to access this class.
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /// Constructor that will read a file containing /ref Clients.
    public ClientFacade(){
        clients = DataWriteRead.getInstanceHashMap("clients");
        if (clients.isEmpty()) clients.put("admin",new Client("admin","admin",true));
    }

    /**
     * Verifies that this username does not exist in the system. \n
     * If there isn't adds the client to the system.
     * @param username The new Client's username.
     * @param password The new Client's password.
     */
    public void addClient(String username,String password,boolean isAdmin) throws RepeatedKey{
        readWriteLock.readLock().lock();
        try {
            if (clients.containsKey(username)) throw new RepeatedKey(username);
        }
        finally {
            readWriteLock.readLock().unlock();
        }
        readWriteLock.writeLock().lock();
        try {
            clients.put(username,new Client(username,password,isAdmin));
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Verifies that this username and password combination exists in the system. \n
     * @param username The Client's username.
     * @param password The Client's password.
     * @return If the is or not such client in the system.
     */
    public boolean isClientInTheSystem(String username, String password){
        readWriteLock.readLock().lock();
        try {
            return clients.containsKey(username) && clients.get(username).getPassword().equals(password);
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * Checks if a client is a administrator.
     * @param username Username of the admin.
     * @return If this client is or isn't a admin.
     */
    public boolean isClientAdmin(String username){
        readWriteLock.readLock().lock();
        try {
            return clients.containsKey(username) && clients.get(username).isAdmin();
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * Adds a reservation to this client.
     * @param username The username of the user.
     * @param reservation The id of the reservation.
     */
    public void addReservations(String username,int reservation){
        readWriteLock.writeLock().lock();
        try {
            clients.get(username).addReservations(reservation);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Remove a reservation to this client.
     * @param username The username of the user.
     * @param reservation The id of the reservation.
     */
    public void removeReservations(String username,int reservation){
        readWriteLock.writeLock().lock();
        try {
            clients.get(username).removeReservations(reservation);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Checks if a client makes a reservation.
     * @param username The username of the user.
     * @param reservation The id of the reservation.
     * @return If this client made this reservation.
     */
    public boolean isReservationFromThisUser(String username,int reservation){
        readWriteLock.writeLock().lock();
        try {
            List<Integer> l = clients.get(username).getReservations();
            return l.contains(reservation);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Gets the list of reservation of this user.
     * @param username The username of the user.
     * @return List of reservation of this user.
     */
    public List<Integer> getsReservation(String username){
        readWriteLock.writeLock().lock();
        try {
            return clients.get(username).getReservations();
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /// Saves the map on a file.
    public void saveClients(){
        readWriteLock.readLock().lock();
        try {
            DataWriteRead.saveInstanceHashMap(clients,"clients");
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

}
