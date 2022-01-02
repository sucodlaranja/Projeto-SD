package Server.ServerInfo.ClientInfo;

import Server.ServerInfo.DataWriteRead;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

///This class will manage all the clients in the system.
public class ClientFacade {

    /// This Map contains all the clients of the system.
    Map<String,Client> clients;

    /// Lock that will allow multiple threads to access this class.
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /// Constructor that will read a file containing /ref Clients.
    public ClientFacade(){
        clients = DataWriteRead.getInstanceHashMap("clients");
    }

    /**
     * Verifies that this username does not exist in the system. \n
     * If there isn't adds the client to the system.
     * @param username The new Client's username.
     * @param password The new Client's password.
     */
    public void addClient(String username,String password) throws RepeatedKey{
        readWriteLock.readLock().lock();
        try {
            if (clients.containsKey(username)) throw new RepeatedKey(username);
        }
        finally {
            readWriteLock.readLock().unlock();
        }
        readWriteLock.writeLock().lock();
        try {
            clients.put(username,new Client(username,password));
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