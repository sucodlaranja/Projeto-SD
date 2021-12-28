import java.util.Scanner;

public class TextUI {
    // O model tem a 'lógica de negócio'.
    private ClientWorker worker;

    // Scanner para leitura
    private Scanner scin;

    /**
     * Construtor.
     *
     * Cria os menus e a camada de negócio.
     */
    public TextUI() {

        this.worker = new ClientWorker();
        scin = new Scanner(System.in);
        worker.startRequestWorker();
    }

    /**
     * Executa o menu principal e invoca o método correspondente à opção
     * seleccionada.
     */
    public void run() {
        System.out.println("Welcome to Flight Choicer!");
        this.authenticate();
        this.mainMenu();
        System.out.println("Até breve...");
        worker.addRequest("close");
    }

    // Estados da UI

    /// authenticate Menu
    private void authenticate() {
        while(!worker.isLoggedIn()) {
        Menu menu = new Menu(new String[] {
                "Sign in",
                "Sign up"
        });
        menu.setHandler(1, () -> signIn());
        menu.setHandler(2, () -> signUp());

        menu.runOnce();
        }
    }


    ///Main menu
    private void mainMenu() {
        String[] options;
        if (worker.isAdmin()) {
            options = new String[] {
                    "Check available flights",
                    "Make a reservation",
                    "Cancel a Reservation",
                    "Check Responses",
                    "Add Flight",
                    "End Day",
                    "Add new Admin",

            };
        } else {
            options = new String[] {
                    "Check available flights",
                    "Make a reservation",
                    "Cancel a Reservation",
                    "Check Reservations"

            };
        }
        Menu menu = new Menu(options);


        // Registar os handlers das transições
        menu.setHandler(1, () -> verifyFlights());
        menu.setHandler(2, () -> makeReservation());
        menu.setHandler(3, () -> cancelReservation());
        menu.setHandler(4, () -> checkReservations());
        if (worker.isAdmin()) {
            menu.setHandler(5, () -> addFlight());
            menu.setHandler(6, () -> endDay());
            menu.setHandler(7, () -> addAdmin());
        }

        // Executar o menu
        menu.run();
    }

    //Handlers


        // TODO: Decidir protocolo
        private void signIn() {
            System.out.println("Please insert your username: ");
            String username = scin.nextLine();
            System.out.println("Please insert your password: ");
            String password = scin.nextLine();
            System.out.println("loading...");
            worker.addRequest(username + " " + password);
            worker.waitMain();
            System.out.println(worker.getResponses("signIn"));
            
        }
    
        // TODO: Decidir Protocolo
        private void signUp() {
            System.out.println("Please insert your username: ");
            String username = scin.nextLine();
            System.out.println("Please insert your password: ");
            String password = scin.nextLine();
            System.out.println("loading...");
            worker.addRequest(username + " " + password);
            worker.waitMain();
        }


    /**
     * TODO Decidir protocolo
     */
    private void verifyFlights() {

        System.out.println("From: ");
        String from = scin.nextLine();
        System.out.println("To: ");
        String to = scin.nextLine();
        System.out.println("Depart: ");
        int depart = readInt();
        worker.addRequest("verif: " + from + " " + to + " " + depart);

    }

    /**
     * TODO: Decidir protocolo e casos de nao ser possivel
     * no sleep
     */
    private void makeReservation() {
        System.out.println("Please Insert flight number: ");
        int flight = readInt();
        worker.addRequest("AddR:" + flight);

    }

    /**
     * TODO: Decidir protocolo
     * no sleep
     */
    private void cancelReservation() {
        System.out.println("Please Insert reservation number: ");
        int reservation = readInt();
        worker.addRequest("RemR:" + reservation);
    }

    /**
     * TODO: Decidir protocolo
     * 
     */
    private void checkReservations() {
        System.out.println(worker.getResponses("reservations"));
        System.out.println("Do you want to see previous reservations?[y/n]");
        String input = scin.nextLine();
        if(input.equals("y")) {
            worker.addRequest("check");
            worker.waitMain();
            System.out.println(worker.getResponses("latest"));                  //TODO: not sure como fazer isto
        }
        
    }

    /**
     * TODO: Decidir protocolo
     * sleep?
     */
    private void endDay() {
        System.out.println("Please insert day: ");
        int day = readInt();
        worker.addRequest("End:" + day);
    }

    /**
     * TODO: Decidir protocolo
     * sleep?
     */
    private void addFlight() {
        System.out.println("Please insert From: ");
        String from = scin.nextLine();
        System.out.println("Please insert To: ");
        String to = scin.nextLine();
        System.out.println("Please insert flight capacity: ");
        int capacity = readInt();
        worker.addRequest("AddF " + from + " " + to + " " + Integer.toString(capacity));
    }

    /**
     * TODO: Decidir protocolo
     * no sleep
     */
    private void addAdmin() {
        System.out.println("Please insert username: ");
        String username = scin.nextLine();
        System.out.println("Insert password: ");
        String password = scin.nextLine();
        worker.addRequest("aDDA " + username + " " + password);
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