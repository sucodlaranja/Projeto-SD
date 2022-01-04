package Server;

import Client.View.ReaderWriter;
import Server.ServerInfo.ClientInfo.ClientFacade;
import Server.ServerInfo.ClientInfo.IClientFacade;
import Server.ServerInfo.FlightInfo.FlightFacade;
import Server.ServerInfo.FlightInfo.IFlightFacade;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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

    static class ClientHandler implements Runnable{

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
                            String usernameIn = in.readUTF();
                            String passwordIn = in.readUTF();
                            out.writeUTF("1--signin funciona");
                        }
                        case 1 -> { // signup
                            String usernameUp = in.readUTF();
                            String passwordUp = in.readUTF();
                            out.writeUTF("1--signun funciona");
                        }
                        case 2 -> { // verif flights
                            String fromV = in.readUTF();
                            String toV = in.readUTF();
                            int departureV = in.readInt();
                            out.writeUTF("1--verif funfa");
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
                            String departureA = in.readUTF();
                            out.writeUTF("1--add flights funciona");
                        }
                        case 8 -> { // add admin
                            String username = in.readUTF();
                            String password = in.readUTF();
                            out.writeUTF("1--add admin funciona");
                        }
                        default -> {
                        }
                    }
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                close = true;
            }
        }
    }
}
