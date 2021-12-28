import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClientController
 */
public class ClientWorker {
    private List<String> requests;
    private List<String> responses;
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
        lock = new ReentrantLock();
        sleepmain = lock.newCondition();
        sleepRequest = lock.newCondition();
    }

    /**
     * TODO ACORDAR O MAIN NAS VEZES EM QUE ADORMECE
     * TODO: DESENVOLVER ISTO APOS DEFINIR PROTOCOLO
     * 
     * @param response
     * @return
     */
    public void handleresponse(String response) {
        lock.lock();
        try {
            loggedIn = true;
            responses.add(response);
            sleepmain.signal();
        } finally {
            lock.unlock();
        }

    }


    //Information handlers


    /**
     * Adds one request from the client to a list so that the requestWorker can ask
     * to the server
     * 
     * @param request // request made by the client
     */
    public void addRequest(String request) {
        lock.lock();
        try {
            System.out.println("esta a entrar aqui pelo menos");
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
            List<String> cloned = new ArrayList<String>();
            for (String request : requests) {
                cloned.add(request);
            }
            return cloned;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Deletes one request from request list, that is already answered
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
     * TODO: NOT SURE QUE SEJA NECESSARIO, DEPENDE DO PROTOCOLO
     * @param wich
     * @return
     */
    public String getResponses(String wich) {
        lock.lock();
        try {
            String response = null;
            switch (wich) {
                case "signIn":
                    response = "signed in!";
                case "reservations":
                    response = "reservations bro!";
                default:
                    response = "No response";
            }
            return response;
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
     * Puts main thread to sleep while there is no responses
     * TODO : ISto apenas funciona se as respostas estiverem vaiz
     */
    public void waitMain() {
        lock.lock();
        int size = responses.size();
        try {
            while (size == responses.size()) {
                try {
                    System.out.println("Main thread is sleeping");
                    sleepmain.await();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void waitRequestWorker() {
        lock.lock();
        try {
            while (requests.isEmpty()) {
                try {
                    System.out.println("request Worker sleeping...");
                    sleepRequest.await();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } finally {
            lock.unlock();
        }
    }

}