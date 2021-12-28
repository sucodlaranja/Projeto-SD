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

    public ReentrantLock lock;
    public Condition sleepmain;
    public Condition sleepRequest;

    // Verifies if user is an admin
    public boolean isAdmin() {
        lock.lock();
        try {
        return admin;
        } finally {lock.unlock();}
    }

    public List<String> getRequests() {
        lock.lock();
        try {
            List<String> cloned = new ArrayList<String>();
            for (String request : requests) {
                cloned.add(request);
            }
        return cloned;
        }finally {lock.unlock();}
    }

    public void deleteRequest(String request) {
        lock.lock();
        try {
            requests.remove(request);
        }finally {lock.unlock();}
    }

    public String getResponses(String wich) {
        lock.lock();
        try {
        String response = null;
        switch (wich) {
            case "signIn": response = "signed in!";
            case "reservations": response = "reservations bro!";
            default: response = "No response";
        }
        return response;}
        finally {lock.unlock();}
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
     * 
     * @param response
     * @return
     */
    public void handleresponse(String response) {
        lock.lock();
        try {
            admin = true;
            responses.add(response);
            sleepmain.signal();
        } finally {
            lock.unlock();
        }

    }

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

    public void startRequestWorker() {
        Thread requestWorker = new Thread(new RequestWorker(this));
        requestWorker.start();
    }

    /**
     * sleep for the main thread so
     */
    public void waitMain() {
        lock.lock();
        try {
            while (responses.isEmpty()) {
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