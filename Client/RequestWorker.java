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

    @Override
    /**
     * TODO: Temos que definir o protocolo, vou so supor coisas para ja
     */
    public void run() {
        try {
            while (!socket.isClosed()) {
                
                List<String> requests = worker.getRequests();
                worker.waitRequestWorker();
                for (String request : requests) {
                    if(request.equals("close")) {closeSocket();}

                    out.writeUTF(request);
                    worker.handleresponse(in.readUTF());
                    worker.deleteRequest(request);
                }
            }
        } catch (IOException  e) {
            e.printStackTrace(); // TODO: e so pra testar
        }
    }

    private void closeSocket() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

}
