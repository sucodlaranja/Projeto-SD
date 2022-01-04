package Server.ServerInfo.ClientInfo;

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
    public boolean isClientAdmin(String username);

    /// Saves the map on a file.
    void saveClients();
}
