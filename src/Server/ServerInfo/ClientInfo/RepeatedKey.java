package Server.ServerInfo.ClientInfo;

public class RepeatedKey extends Exception{
    public RepeatedKey(String key){
        super("The username " + key + " already exists.\nPlease try another one.");
    }
}
