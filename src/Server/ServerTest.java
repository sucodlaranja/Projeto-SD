package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8081);

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("COnnection Started");
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                String x = in.readUTF();
                out.writeUTF("recebi e enviei macaco");
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
