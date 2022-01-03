package Server.ServerInfo;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

///This class allows reading and writing structures on a file.
public class DataWriteRead {

    ///Reads a \ref Hashmap from a given file.
    public static Map getInstanceHashMap(String filename) {

        HashMap hashMap = null;
        try {
            ObjectInputStream is =
                    new ObjectInputStream(new FileInputStream(filename));
            hashMap = (HashMap) is.readObject();
        }
        catch (IOException ex) {
            System.out.println("O sistema nao conseguiu carregar o ficheiro: " + filename + ".");
        }
        catch (ClassNotFoundException ignored){ }


        if (hashMap == null) return new HashMap<>();
        else return new HashMap<>(hashMap);
    }

    /// Writes a \ref HashMap on a given file.
    public static void saveInstanceHashMap(Map map,String filename) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
            os.writeObject(map);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///Reads a \ref HashSet from a given file.
    public static Set getInstanceHashSet(String filename) {

        Set hashSet = null;
        try {
            ObjectInputStream is =
                    new ObjectInputStream(new FileInputStream(filename));
            hashSet = (HashSet) is.readObject();
        }
        catch (IOException ex) {
            System.out.println("O sistema nao conseguiu carregar o ficheiro: " + filename + ".");
        }
        catch (ClassNotFoundException ignored){ }


        if (hashSet == null) return new HashSet<>();
        else return new HashSet(hashSet);
    }

    /// Writes a \ref HashSet on a given file.
    public static void saveInstanceHashSet(Set set,String filename) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
            os.writeObject(set);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///Reads information from a given file.
    public static LocalDateTime getInstanceOtherInformation(String filename) {

        LocalDateTime localDateTime = null;
        try {
            ObjectInputStream is =
                    new ObjectInputStream(new FileInputStream(filename));
            localDateTime = ((LocalDateTime) is.readObject());
        }
        catch (IOException ex) {
            System.out.println("O sistema nao conseguiu carregar o ficheiro: " + filename + ".");
        }
        catch (ClassNotFoundException ignored){ }

        if (localDateTime == null) return LocalDateTime.now();
        else return localDateTime;
    }

    /// Writes information on a given file.
    public static void saveInstanceOtherInformation(Map map,LocalDateTime localDateTime,String filename) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
            os.writeObject(localDateTime);
            os.writeObject(map);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO : Fica aqui ?
    /// Encounter the biggest number from a Set.
    public static int maxId(Set<Integer> s){
        Optional<Integer> i = s.stream().max(Integer::compareTo);
        if (i.isEmpty()) return 0;
        else return i.get() + 1;
    }
}