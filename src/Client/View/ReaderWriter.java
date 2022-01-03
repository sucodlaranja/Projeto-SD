package Client.View;
import java.util.Scanner;

public class ReaderWriter {
    private static Scanner scin = new Scanner(System.in);

    /**
     * cleans screen
     */
    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    } 

    /**
     * Blocks application until User presses enter
     */
    public static void pressEnterToContinue()
    { 
           System.out.println("\nPressione Enter tecla para continuar...");
           try
           {
               scin.nextLine();
           }  
           catch(Exception e)
           {}  
    }

    /**
     * Prints one given string
     * @param str String that will be printed.
     */
    public static void printString(String str) {
        System.out.println(str);
    }


    /**
     * Reads next line from User
     * @return String que foi obtido.
     */
    public static String getString() {
        String res = scin.nextLine();

        return res;
    }

    /**
     * Reads next line from the user, present one string previously
     * @param str String with message to present previously.
     * @return String wich is UserInput
     */
    public static String getString(String str) {
        System.out.println(str);
        String res = scin.nextLine();

        return res;
    }
    /**
     * Reads UserInput, for int variables 
     * @return UserInput number
     */
    public static int getInt() {
        int r;
        try {
            r = Integer.parseInt(scin.nextLine());
            
        } catch (Exception e) {
            System.out.println("Please insert a number: ");
            r = getInt();
        }
        return r;
    }
    /**
     * Reads next line from the user, present one string previously
     * @param str String with message to present previously.
     * @return UserInput number
     */
    public static int getInt(String str) {
        int r;

        try {
            System.out.println(str);
            r = Integer.parseInt(scin.nextLine());
        } catch (Exception e) {
            System.out.println("Please insert a number: ");
            r = getInt();
        }
        return r;
    }
}
