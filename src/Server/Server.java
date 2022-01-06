package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import Server.ServerInfo.ClientInfo.ClientFacade;
import Server.ServerInfo.ClientInfo.IClientFacade;
import Server.ServerInfo.ClientInfo.RepeatedKey;
import Server.ServerInfo.FlightInfo.*;

/// This is the server of this project, and will handle all requests.
public class Server {

    /// Allows server to read requests.
    private ServerSocket serverSocket;
    /// Stores all the info of the clients.
    private IClientFacade clients;
    /// Stores all the info of the Flights.
    private IFlightFacade flights;

    /// Basic constructor.
    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.clients = new ClientFacade();
            this.flights = new FlightFacade();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the server working.
     */
    public void run() {
        while (!serverSocket.isClosed()){
            try {
                Socket newRequestSocket = serverSocket.accept();
                System.out.println("New Client!"); // TODO : DEBUG!
                Thread t = new Thread(new ClientHandler(newRequestSocket));
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

     class ClientHandler implements Runnable{

        private final DataOutputStream out;
        private final DataInputStream in;
        private String username;

        public ClientHandler(Socket requestSocket) throws IOException {
            out = new DataOutputStream(new BufferedOutputStream(requestSocket.getOutputStream()));
            in = new DataInputStream(new BufferedInputStream(requestSocket.getInputStream()));
        }

        @Override
        public void run() {
            boolean close = false;

            while (!close) {
                try {
                    switch (in.readInt()) {
                        case 0 -> { // signin
                            String username = in.readUTF();
                            String password = in.readUTF();
                            if (clients.isClientInTheSystem(username,password)) {
                                if (clients.isClientAdmin(username)) out.writeUTF("2--Welcome boss!");
                                else out.writeUTF("1--Login successful!");
                                this.username = username;
                            }
                            else {
                                out.writeUTF("-1--Username and password combination does not exist!");
                                close = true;
                            }
                        }
                        case 1 -> { // signup
                            String username = in.readUTF();
                            String password = in.readUTF();
                            try {
                                clients.addClient(username,password,false);
                                out.writeUTF("1--SignUp successful!");
                                this.username = username;
                            } catch (RepeatedKey e) {
                                out.writeUTF("-1--" + e.getMessage());
                                close = true;
                            }
                        }
                        case 2 -> { // verif flights
                            if (in.readBoolean()){
                                out.writeUTF("1--All the flights available:\n"
                                        + String.join("\n",flights.getAllFlights()));
                            }
                            else{
                                String fromV = in.readUTF();
                                String toV = in.readUTF();
                                String result = pathsToString(flights.findAllPossiblePaths(fromV,toV));
                                out.writeUTF("1--" + result);
                            }
                        }
                        case 3 -> { // add reservation
                            String flightsList = in.readUTF();
                            String date1 = in.readUTF();
                            String date2 = in.readUTF();
                            try {
                                int res = flights.addReservation(Arrays.asList(flightsList.split(";")),
                                        LocalDate.parse(date1), LocalDate.parse(date2));
                                clients.addReservations(username,res);
                                out.writeUTF("3--Reservation " + res +" added with success.");
                            } catch (FlightNotAvailable | WrongDate e){
                                out.writeUTF("3--" + e.getMessage());
                            }
                        }
                        case 4 -> { // remove reservation
                            int flightIdR = in.readInt();
                            if (clients.isReservationFromThisUser(username,flightIdR)){
                                try {
                                    flights.removeReservation(flightIdR);
                                    clients.removeReservations(username,flightIdR);
                                    out.writeUTF("3--Reservation removed with success.");
                                } catch (ReservationNotAvailable | WrongDate e) {
                                    out.writeUTF("-1--" + e.getMessage());
                                }
                            } else out.writeUTF("-1--" + username + " didn't make this reservation.");


                        }
                        case 5 -> { // check reservations
                            List<Integer> l = clients.getsReservation(username);
                            if (l.isEmpty()) out.writeUTF("-1--" + username + " did not make any reservation.");
                            else {
                                List<String> p = l.stream().map(h -> flights.reservationToString(h)).collect(Collectors.toList());
                                String s = String.join("\n", p);
                                out.writeUTF("1--" + s);
                            }
                        }

                        case 6 -> { // end days
                            String dayE = in.readUTF();
                            try {
                                if( flights.endDay(LocalDate.parse(dayE)))
                                    out.writeUTF("1--Day closed.No more reservations or cancellations allowed.");
                                else
                                    out.writeUTF("-1--Reservations and cancellations today were already closed");
                            } catch (WrongDate e) {
                                out.writeUTF("3--" + e.getMessage());
                            }
                        }
                        case 7 -> { // add flights
                            String fromA = in.readUTF();
                            String toA = in.readUTF();
                            int capacity = in.readInt();
                            flights.addFlight(fromA,toA,capacity);
                            out.writeUTF("1--Flight added with success!");
                        }
                        case 8 -> { // add admin
                            String username = in.readUTF();
                            String password = in.readUTF();
                            try {
                                clients.addClient(username,password,true);
                                out.writeUTF("1--New Admin " + username + "added with success!");
                            } catch (RepeatedKey e) {
                                out.writeUTF("-1--" + e.getMessage());
                            }
                        }
                        default -> {
                            out.writeUTF("-1--Option does not exist");
                        }
                    }
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            System.out.println("ClientHandler ends\n "); // TODO : DEBUG!
        }

         private static String pathsToString(List<List<String>> allPaths){
             StringBuilder sb = new StringBuilder();

             for(List<String> path : allPaths){
                 sb.append(path.remove(0));
                 for(String location : path) sb.append(" -> ").append(location);
                 sb.append("\n");
             }

             return sb.toString();
         }
    }
}
