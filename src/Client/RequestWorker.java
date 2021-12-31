package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class RequestWorker implements Runnable {
    ClientWorker worker;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    // TODO: Definir o protocolo

    public RequestWorker(ClientWorker worker) {
        try {
            this.worker = worker;
            this.socket = new Socket("localhost", 8081);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace(); // TODO: e so pra testar
        }
    }


    /**
     * Como fechar o socket sem excecao? E necessario enviar para o outro lad?
     */
    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {

                List<String> requests = worker.getRequests();
                worker.waitRequestWorker();
                for (String request : requests) {
                    if (request.equals("close")) {
                        closeSocket();
                        break;
                    } else {
                        out.writeUTF(request);
                        worker.handleresponse(in.readUTF());
                        worker.deleteRequest(request);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // TODO: e so pra testar
        }
    }


    //Closes socket
    private void closeSocket() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

}