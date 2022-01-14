package Server.ServerInfo;

import java.io.*;
import java.time.LocalDate;
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

    ///Reads a \ref ArrayList from a given file.
    public static List getInstanceArrayList(String filename, int days) {

        List arrayList = null;
        try {
            ObjectInputStream is =
                    new ObjectInputStream(new FileInputStream(filename));
            arrayList = (ArrayList) is.readObject();
        }
        catch (IOException ex) {
            System.out.println("O sistema nao conseguiu carregar o ficheiro: " + filename + ".");
        }
        catch (ClassNotFoundException ignored){ }


        if (arrayList == null) {
            List<Boolean> n = new ArrayList<>();
            for(int i = 0; i < days; i++){
                n.add(true);
            }
            return n;
        }
        else return new ArrayList(arrayList);
    }

    /// Writes a \ref HashSet on a given file.
    public static void saveInstanceArrayList(List list,String filename) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
            os.writeObject(list);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///Reads information from a given file.
    public static LocalDate getInstanceLocalDate(String filename) {

        LocalDate localDate = null;
        try {
            ObjectInputStream is =
                    new ObjectInputStream(new FileInputStream(filename));
            localDate = ((LocalDate) is.readObject());
        }
        catch (IOException ex) {
            System.out.println("O sistema nao conseguiu carregar o ficheiro: " + filename + ".");
        }
        catch (ClassNotFoundException ignored){ }

        if (localDate == null) return LocalDateTime.now().toLocalDate();
        else return localDate;
    }

    /// Writes information on a given file.
    public static void saveInstanceLocalDate(LocalDate localDate, String filename) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
            os.writeObject(localDate);
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