package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;



public class ClientWorker {
    private final List<String> requests;
    private final List<String> responses;
    private boolean admin;
    private boolean loggedIn;

    public ReentrantLock lock;
    public Condition sleepmain;
    public Condition sleepRequest;

    /// Verifies if user is an admin
    public boolean isAdmin() {
        lock.lock();
        try {
            return admin;
        } finally {
            lock.unlock();
        }
    }

    /// Verifies if user is loggedIn
    public boolean isLoggedIn() {
        lock.lock();
        try {
            return loggedIn;
        } finally {
            lock.unlock();
        }
    }

    public ClientWorker() {
        requests = new ArrayList<>();
        responses = new ArrayList<>();
        admin = false;
        loggedIn = false;
        lock = new ReentrantLock();
        sleepmain = lock.newCondition();
        sleepRequest = lock.newCondition();
    }

    /**
     * Handles response from server
     * 
     * @param response response given from the server.
     */
    public void handleresponse(String response) {
        lock.lock();
        try {

            String[] spliter = response.split("--");
            responses.add(spliter[1]);
            switch (spliter[0]) {
                case "-1":
                    sleepmain.signal();
                    break;
                case "1":
                    if (!loggedIn)
                        loggedIn = true;
                    sleepmain.signal();
                    break;
                case "2":
                    this.loggedIn = true;
                    this.admin = true;
                    sleepmain.signal();
                    break;
                default:
                    break;
            }

        } finally {
            lock.unlock();
        }

    }

    // Information handlers

    /**
     * Adds one request from the client to a list so that the requestWorker can ask
     * to the server
     * 
     * @param request // request made by the client
     */
    public void addRequest(String request) {
        lock.lock();
        try {
            requests.add(request);
            sleepRequest.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 
     * @return a cloned list of requests
     */
    public List<String> getRequests() {
        lock.lock();
        try {
            return new ArrayList<String>(requests);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Deletes one request from request list
     * 
     * @param request request to be removed
     */
    public void deleteRequest(String request) {
        lock.lock();
        try {
            requests.remove(request);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gives the responses in responses list that was unseen
     */
    public String getResponses() {
        lock.lock();
        try {
            StringBuilder response = new StringBuilder();
            if (responses.isEmpty()) {
                response.append("No reservations made");
            } else {
                for (String r : responses) {
                    response.append(r + "\n");
                }
                responses.clear();
            }
            return response.toString();
        } finally {
            lock.unlock();
        }
    }

    // Gives last response in response list
    public String getResponse() {
        lock.lock();
        try {
            String ola = responses.remove(responses.size() - 1);
            return ola;
        } finally {
            lock.unlock();
        }
    }

    /// Creates and starts RequestWorker thread
    public void startRequestWorker() {
        Thread requestWorker = new Thread(new RequestWorker(this));
        requestWorker.start();
    }

    // waiters

    /**
     * Puts main thread to sleep while there is no more new responses
     */
    public void waitMain() {
        lock.lock();
        int size = responses.size();
        try {
            while (size == responses.size()) {
                try {
                  
                    sleepmain.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    //Puts requestWorker thread to sleep until there is a request
    public void waitRequestWorker() {
        lock.lock();
        try {
            while (requests.isEmpty()) {
                try {
                    sleepRequest.await();
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
         
        } finally {
            lock.unlock();
        }
    }

    //Class that communicates with the server
    public class RequestWorker implements Runnable {
        ClientWorker worker;
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;

        
        //Constructor
        public RequestWorker(ClientWorker worker) {
            try {
                this.worker = worker;
                this.socket = new Socket("localhost", 8081);
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace(); 
            }
        }

        /**
         * Does communication with the server
         */
        @Override
        public void run() {
            try {
                while (!socket.isClosed()) {

                    List<String> requests = worker.getRequests();
                    worker.waitRequestWorker();
                    for (String request : requests) {
                        serializeandSend(request);
                        worker.handleresponse(in.readUTF());
                        worker.deleteRequest(request);

                    }
                }
            } catch (IOException e) {}
        }

        // Closes socket
        private void closeSocket() {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         *Serializes information and sends it to the server
         * @param str information to be serialized
         */
        private void serializeandSend(String str) throws IOException {
            String[] spliter = str.split("--");
            switch (spliter[0]) {
                case "close":
                    closeSocket();
                    break;
                case "signIn":
                    out.writeInt(0);
                    out.writeUTF(spliter[1]); // Username
                    out.writeUTF(spliter[2]); // Password
                    break;
                case "signUp":
                    out.writeInt(1);
                    out.writeUTF(spliter[1]); // Username
                    out.writeUTF(spliter[2]); // Password
                    break;
                case "verif":
                    out.writeInt(2);
                    out.writeUTF(spliter[1]); // from
                    if (!spliter[1].equals("all")) out.writeUTF(spliter[2]); // to
                    break;
                case "AddR":
                    out.writeInt(3);
                    out.writeUTF(spliter[1]); //places
                    out.writeUTF(spliter[2]); //first date
                    out.writeUTF(spliter[3]);// second date
                    break;
                case "RemR":
                    out.writeInt(4);
                    out.writeInt(Integer.parseInt( spliter[1])); // Reservation number
                    break;
                case "check":
                    out.writeInt(5); // Checks all reservations
                    break;
                case "End":
                    out.writeInt(6);
                    out.writeUTF(spliter[1]); // day
                    break;
                case "AddF":
                    out.writeInt(7);
                    out.writeUTF(spliter[1]); // From
                    out.writeUTF(spliter[2]); // to
                    out.writeInt(Integer.parseInt(spliter[3])); // Capacity
                    break;
                case "aDDA":
                    out.writeInt(8);
                    out.writeUTF(spliter[1]); // username
                    out.writeUTF(spliter[2]); // password
                default:
                    break;
            }
            if (!spliter[0].equals("close")) {
    
                out.flush();
            }

        }

    }

}