package Server;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8081);
            Socket socket = serverSocket.accept();
            System.out.println("COnnection Started");
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            while (!serverSocket.isClosed()) {
                System.out.println(in.readInt());
                out.writeUTF("1--oooo");
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Logica do protocolo
    public static void deserializeAndSerialize(DataOutputStream out, DataInputStream in) throws IOException {
        System.out.println("pedido recebido");
        switch (in.readInt()) {
            case 0: // signin
                String usernameIn = in.readUTF();
                String passwordIn = in.readUTF();
                out.writeUTF("1--signin funciona");
                break;
            case 1: // signup
                String usernameUp = in.readUTF();
                String passwordUp = in.readUTF();
                out.writeUTF("1--signun funciona");
                break;
            case 2: // verif flights
                String fromV = in.readUTF();
                String toV = in.readUTF();
                int departureV = in.readInt();
                out.writeUTF("1--verif funfa");
                break;
            case 3: // add reservation
                int flightIdA = in.readInt();
                String dayR = in.readUTF();
                out.writeUTF("3--add reservation funciona");
                break;
            case 4: // remove reservation
                int flightIdR = in.readInt();
                out.writeUTF("3--remove reservation funciona");
                break;
            case 5: // check reservations
                out.writeUTF("1--check reservations funciona");
                break;
            case 6: // end days
                String dayE = in.readUTF();
                out.writeUTF("1--end days funciona");
                break;
                //TODO Daqui pra baixo nao sei se vale a pena dar sleep
            case 7: // add flights
                String fromA = in.readUTF();
                String toA = in.readUTF();
                String departureA = in.readUTF();
                out.writeUTF("1--add flights funciona");
                break;
            case 8: // add admin
                String username = in.readUTF();
                String password = in.readUTF();
                out.writeUTF("1--add admin funciona");
                break;

            default:
                break;
        }
        out.flush();
    }
}