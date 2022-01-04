package Server.ServerInfo.ClientInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/// This class stores the information of a client.
class Client implements Serializable {

    /// The client's username.
    private final String username;
    /// The client's password.
    private final String password;
    /// List of ids of reservations the client made.
    private final List<Integer> reservations;
    /// If this account has the admin privilegies.
    private boolean isAdmin;

    /// Basic Constructor.
    public Client(String username, String password,boolean isAdmin){
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        reservations = new ArrayList<>();
    }

    /// Simple get
    public boolean isAdmin() {return isAdmin;}

    /// Simple get.
    public String getUsername() {
        return username;
    }

    /// Simple get.
    public String getPassword() {
        return password;
    }

    /// Simple get whit clone.
    public List<Integer> getReservations() {
        return new ArrayList<>(reservations);
    }

    /// Adds an id to the list of reservations.
    public void addReservations(Integer reservationId){
        reservations.add(reservationId);
    }
}
