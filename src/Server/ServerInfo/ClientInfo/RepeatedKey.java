package Server.ServerInfo.ClientInfo;

public class RepeatedKey extends Exception{
    public RepeatedKey(String key){
        super("The username " + key + "already exists.\n Please try another one.");
    }
}
