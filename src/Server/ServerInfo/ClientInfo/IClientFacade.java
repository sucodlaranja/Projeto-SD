package Server.ServerInfo.ClientInfo;

import java.util.List;

/// This interface will allow other classes to use the clientsFacade component.
public interface IClientFacade {

    /**
     * Verifies that this username does not exist in the system. \n
     * If there isn't adds the client to the system.
     * @param username The new Client's username.
     * @param password The new Client's password.
     */
    void addClient(String username,String password,boolean isAdmin) throws RepeatedKey;

    /**
     * Verifies that this username and password combination exists in the system. \n
     * @param username The Client's username.
     * @param password The Client's password.
     * @return If the is or not such client in the system.
     */
    boolean isClientInTheSystem(String username, String password);

    /**
     * Checks if a client is a administrator.
     * @param username Username of the admin.
     * @return If this client is or isn't a admin.
     */
    boolean isClientAdmin(String username);

    /**
     * Adds a reservation to this client.
     * @param username The username of the user.
     * @param reservation The id of the reservation.
     */
    void addReservations(String username,int reservation);

    /**
     * Remove a reservation to this client.
     * @param username The username of the user.
     * @param reservation The id of the reservation.
     */
    void removeReservations(String username,int reservation);

    /**
     * Checks if a client makes a reservation.
     * @param username The username of the user.
     * @param reservation The id of the reservation.
     * @return If this client made this reservation.
     */
    boolean isReservationFromThisUser(String username,int reservation);

    /**
     * Gets the list of reservation of this user.
     * @param username The username of the user.
     * @return List of reservation of this user.
     */
    List<Integer> getsReservation(String username);

    /// Saves the map on a file.
    void saveClients();
}
