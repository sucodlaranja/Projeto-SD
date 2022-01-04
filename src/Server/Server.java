package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import Server.ServerInfo.ClientInfo.ClientFacade;
import Server.ServerInfo.ClientInfo.IClientFacade;
import Server.ServerInfo.ClientInfo.RepeatedKey;
import Server.ServerInfo.FlightInfo.FlightFacade;
import Server.ServerInfo.FlightInfo.IFlightFacade;

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

        DataOutputStream out;
        DataInputStream in;

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
                            } catch (RepeatedKey e) {
                                out.writeUTF("-1--" + e.getMessage());
                                close = true;
                            }
                        }
                        case 2 -> { // verif flights 
                            String fromV = in.readUTF();
                            String toV = in.readUTF();
                            String result = pathsToString(flights.findAllPossiblePaths(fromV,toV));
                            System.out.println(fromV);
                            System.out.println(toV);
                            System.out.println(result);
                            out.writeUTF("1--" + result);
                        }
                        case 3 -> { // add reservation
                            String flights = in.readUTF();
                            String date1 = in.readUTF();
                            String date2 = in.readUTF();

                            out.writeUTF("3--add reservation funciona");
                        }
                        case 4 -> { // remove reservation
                            int flightIdR = in.readInt();
                            out.writeUTF("3--remove reservation funciona");
                        }
                        case 5 -> // check reservations
                                out.writeUTF("1--check reservations funciona");
                        case 6 -> { // end days
                            String dayE = in.readUTF();
                            out.writeUTF("1--end days funciona");
                        }
                        case 7 -> { // add flights
                            String fromA = in.readUTF();
                            String toA = in.readUTF();
                            int capacity = in.readInt();
                            flights.addFlight(fromA,toA,capacity);
                            out.writeUTF("1--Flight added whit success!");
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


            System.out.println("Desligar "); // TODO RETIRAR
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
