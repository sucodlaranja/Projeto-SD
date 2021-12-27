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
    public Condition sleep;

    //Verifies if user is an admin
    public boolean isAdmin() {return admin;}
    
    public List<String> getRequests() {return requests;}
    public List<String> getResponses() {return responses;}

    public ClientWorker() {
        requests = new ArrayList<>();
        responses = new ArrayList<>();
        admin = false;
        lock = new ReentrantLock();
        sleep = lock.newCondition();
    }

    /**
     * TODO: VER ISTO TAMBEM
     * @param response
     * @return
     */
    public boolean handleAuthentication(String response) {
        return true;
    }
}