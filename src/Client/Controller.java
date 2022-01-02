package Client;



public class Controller {
    // O model tem a 'lógica de negócio'.
    private final ClientWorker clientWorker;

    

    /**
     * Construtor.
     *
     * Cria os menus e a camada de negócio.
     */
    public Controller() {
        this.clientWorker = new ClientWorker();
    }

    /**
     * Executa o menu principal e invoca o método correspondente à opção
     * seleccionada.
     */
    public void run() {
        // Start worker - manage client interactions with the server using RequestWorker.
        this.clientWorker.startRequestWorker();

        ReaderWriter.printString("Welcome to Flight Choicer!");
        this.authenticate();
        this.mainMenu();
        ReaderWriter.printString("Até breve...");
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

    private void signIn() {
        String username = ReaderWriter.getString("Please insert your username: ");
        String password = ReaderWriter.getString("Please insert your password");

        ReaderWriter.printString("loading...");
        clientWorker.addRequest("signIn " + username + " " + password);
        clientWorker.waitMain();
        ReaderWriter.printString(clientWorker.getResponse());
        ReaderWriter.pressEnterToContinue();
    }

    private void signUp() {
        String username = ReaderWriter.getString("Please insert your username: ");
        String password = ReaderWriter.getString("Please insert your password");

        ReaderWriter.printString("loading...");
        clientWorker.addRequest("signUp " + username + " " + password);
        clientWorker.waitMain();
        ReaderWriter.printString(clientWorker.getResponse());
        ReaderWriter.pressEnterToContinue();
    }

    /**
     *
     */
    private void verifyFlights() {

       
        String from = ReaderWriter.getString("From: ");
        String to = ReaderWriter.getString("To: ");
        
        int depart = ReaderWriter.getInt("Depart: ");
        clientWorker.addRequest("verif " + from + " " + to + " " + depart);
        clientWorker.waitMain();
        ReaderWriter.printString(clientWorker.getResponse());
        ReaderWriter.pressEnterToContinue();
    }

    /**
     * TODO: Casos de nao ser possivel
     * no sleep
     */
    private void makeReservation() {
        int flight = ReaderWriter.getInt("Please Insert flight number: ");
        int day = ReaderWriter.getInt("Please Insert day of flight: ");
        clientWorker.addRequest("AddR" + flight + " " + day);

    }

    /**
     * no sleep
     */
    private void cancelReservation() {
        int reservation = ReaderWriter.getInt("Please Insert reservation number: ");
        clientWorker.addRequest("RemR:" + reservation);
        
        
    }

    /**
     *
     */
    private void checkReservations() {
        ReaderWriter.printString(clientWorker.getResponses());
        String input = ReaderWriter.getString("Do you want to see previous reservations?[y/n]");
        if(input.equals("y")) {
            clientWorker.addRequest("check");
            clientWorker.waitMain();
            ReaderWriter.printString(clientWorker.getResponse());
            ReaderWriter.pressEnterToContinue();
        }
        
    }

    /**
     * sleep?
     */
    private void endDay() {
        int day = ReaderWriter.getInt("Please insert day: ");
        clientWorker.addRequest("End " + day);
        clientWorker.waitMain();
        ReaderWriter.printString(clientWorker.getResponse());
        ReaderWriter.pressEnterToContinue();
    }

    /**
     * sleep?
     */
    private void addFlight() {
        String from = ReaderWriter.getString("Please insert From: ");
        String to   = ReaderWriter.getString("Please insert To: ");
        
        int capacity = ReaderWriter.getInt("Please insert flight capacity: ");
        
        clientWorker.addRequest("AddF " + from + " " + to + " " + capacity);
    }

    /**
     * no sleep
     */
    private void addAdmin() {
        
        String username = ReaderWriter.getString("Please insert username: ");
        String password = ReaderWriter.getString("Please insert password: ");
        clientWorker.addRequest("aDDA " + username + " " + password);
    }




    
}