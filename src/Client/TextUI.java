package Client;

import java.util.Scanner;

public class TextUI {
    // O model tem a 'lógica de negócio'.
    private final ClientWorker clientWorker;

    // Scanner para leitura
    private final Scanner scin;

    /**
     * Construtor.
     *
     * Cria os menus e a camada de negócio.
     */
    public TextUI() {
        this.scin = new Scanner(System.in);
        this.clientWorker = new ClientWorker();
    }

    /**
     * Executa o menu principal e invoca o método correspondente à opção
     * seleccionada.
     */
    public void run() {
        // Start worker - manage client interactions with the server using RequestWorker.
        this.clientWorker.startRequestWorker();

        System.out.println("Welcome to Flight Choicer!");
        this.authenticate();
        this.mainMenu();
        System.out.println("Até breve...");
        clientWorker.addRequest("close");
    }

    // Estados da UI

    /// authenticate Menu
    private void authenticate() {
        while(!clientWorker.isLoggedIn()) {
        Menu menu = new Menu(new String[] {
                "Sign in",
                "Sign up"
        });
        menu.setHandler(1, this::signIn);
        menu.setHandler(2, this::signUp);

        menu.runOnce();
        }
    }


    ///Main menu
    private void mainMenu() {
        String[] options;
        if (clientWorker.isAdmin()) {
            options = new String[] {
                    "Check available flights",
                    "Make a reservation",
                    "Cancel a Reservation",
                    "Check Responses",
                    "Add Flight",
                    "End Day",
                    "Add new Admin",

            };
        }
        else {
            options = new String[] {
                    "Check available flights",
                    "Make a reservation",
                    "Cancel a Reservation",
                    "Check Reservations"

            };
        }

        Menu menu = new Menu(options);

        // Registar os handlers das transições
        menu.setHandler(1, this::verifyFlights);
        menu.setHandler(2, this::makeReservation);
        menu.setHandler(3, this::cancelReservation);
        menu.setHandler(4, this::checkReservations);
        if (clientWorker.isAdmin()) {
            menu.setHandler(5, this::addFlight);
            menu.setHandler(6, this::endDay);
            menu.setHandler(7, this::addAdmin);
        }

        // Executar o menu
        menu.run();
    }

    //Handlers

    // TODO: Decidir protocolo!

    private void getUserNameAndPw() {
        System.out.println("Please insert your username: ");
        String username = scin.nextLine();
        System.out.println("Please insert your password: ");
        String password = scin.nextLine();
        System.out.println("loading...");
        clientWorker.addRequest(username + " " + password);
        clientWorker.waitMain();
    }

    private void signIn() {
        getUserNameAndPw();
        System.out.println(clientWorker.getResponses("signIn"));
    }

    private void signUp() {
        getUserNameAndPw();
    }

    /**
     *
     */
    private void verifyFlights() {

        System.out.println("From: ");
        String from = scin.nextLine();
        System.out.println("To: ");
        String to = scin.nextLine();
        System.out.println("Depart: ");
        int depart = readInt();
        clientWorker.addRequest("verif: " + from + " " + to + " " + depart);

    }

    /**
     * TODO: Casos de nao ser possivel
     * no sleep
     */
    private void makeReservation() {
        System.out.println("Please Insert flight number: ");
        int flight = readInt();
        clientWorker.addRequest("AddR:" + flight);

    }

    /**
     * sleep
     */
    private void cancelReservation() {
        System.out.println("Please Insert reservation number: ");
        int reservation = readInt();
        clientWorker.addRequest("RemR:" + reservation);
    }

    /**
     *
     */
    private void checkReservations() {
        System.out.println(clientWorker.getResponses("reservations"));
        System.out.println("Do you want to see previous reservations?[y/n]");
        String input = scin.nextLine();
        if(input.equals("y")) {
            clientWorker.addRequest("check");
            clientWorker.waitMain();
            System.out.println(clientWorker.getResponses("latest"));    //TODO: not sure como fazer isto
        }
        
    }

    /**
     * sleep?
     */
    private void endDay() {
        System.out.println("Please insert day: ");
        int day = readInt();
        clientWorker.addRequest("End:" + day);
    }

    /**
     * sleep?
     */
    private void addFlight() {
        System.out.println("Please insert From: ");
        String from = scin.nextLine();
        System.out.println("Please insert To: ");
        String to = scin.nextLine();
        System.out.println("Please insert flight capacity: ");
        int capacity = readInt();
        clientWorker.addRequest("AddF " + from + " " + to + " " + capacity);
    }

    /**
     * no sleep
     */
    private void addAdmin() {
        System.out.println("Please insert username: ");
        String username = scin.nextLine();
        System.out.println("Insert password: ");
        String password = scin.nextLine();
        clientWorker.addRequest("aDDA " + username + " " + password);
    }


    //auxiliar methods 

    private int readInt() {
        int r;
        try {
            r = scin.nextInt();
        } catch (Exception e) {
            System.out.println("Please insert a number: ");
            scin.nextLine();
            r = readInt();
        }
        return r;
    }
}