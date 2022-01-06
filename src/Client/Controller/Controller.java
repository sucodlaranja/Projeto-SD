package Client.Controller;

import Client.ClientWorker;
import Client.View.Menu;
import Client.View.ReaderWriter;

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
        ReaderWriter.printString("Welcome to Flight Choicer!\n(BETA :: username = admin, password = admin)");
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
        ReaderWriter.pressEnterToContinue();
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

    /**
     * handles signIn operation and starts RequestWorker so it can communicate with the server
     */

    private void signIn() {
        String username = ReaderWriter.getString("Please insert your username: ");
        String password = ReaderWriter.getString("Please insert your password: ");

        
        
        clientWorker.addRequest("signIn--" + username + "--" + password);

        this.clientWorker.startRequestWorker(); ///Starts worker
        ReaderWriter.printString("loading...");
        clientWorker.waitMain();
        ReaderWriter.printString(clientWorker.getResponse());
        
    }

    /**
     * handles signUp operation and starts RequestWorker so it can communicate with the server
     */
    private void signUp() {
        String username = ReaderWriter.getString("Please insert your username: ");
        String password = ReaderWriter.getString("Please insert your password: ");
        this.clientWorker.startRequestWorker(); //<- Start worker - manage client interactions with the server using RequestWorker.
        ReaderWriter.printString("loading...");
        clientWorker.addRequest("signUp--" + username + "--" + password);
        clientWorker.waitMain();
        ReaderWriter.printString(clientWorker.getResponse());
        
    }

    /**
     *  Request all available flights from To
     */
    private void verifyFlights() {

       
        String from = ReaderWriter.getString("From: ");
        String to = ReaderWriter.getString("To: ");
        
        
        clientWorker.addRequest("verif--" + from + "--" + to);
        clientWorker.waitMain();
        ReaderWriter.printString(clientWorker.getResponse());
        ReaderWriter.pressEnterToContinue();
    }

    /**
     * Requests a reservation
     */
    private void makeReservation() {
        StringBuilder request = new StringBuilder();
        
        String date1 = "";
        String date2 = "";
        String flight = "";
         do {
         flight = ReaderWriter.getString("Please Insert Place/stop: ");

         request.append(flight).append(";");
        }while(!flight.equals("stop"));
        date1 = ReaderWriter.getDate("Please Insert First date[year(xxxx)-month(xx)-day(xx)]");
        date2 = ReaderWriter.getDate("Please Insert Second date[year(xxxx)-month(xx)-day(xx)]");
       
        clientWorker.addRequest("AddR--" + request + "--" + date1 + "--" + date2);

    }

    /**
     * Cancels a reservation
     */
    private void cancelReservation() {
        int reservation = ReaderWriter.getInt("Please Insert reservation number: ");
        clientWorker.addRequest("RemR--" + reservation);
        
        
    }

    /**
     *  verify all requested reservations that the user didn't saw before,
     *  Has the option for the user to request all previous reservations to the server.
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
     * ends a day
     */
    private void endDay() {
        int day = ReaderWriter.getInt("Please insert day: ");
        clientWorker.addRequest("End--" + day);
        clientWorker.waitMain();
        ReaderWriter.printString(clientWorker.getResponse());
        ReaderWriter.pressEnterToContinue();
    }

    /**
     * adds one flight to the server
     */
    private void addFlight() {
        String from = ReaderWriter.getString("Please insert From: ");
        String to   = ReaderWriter.getString("Please insert To: ");
        
        int capacity = ReaderWriter.getInt("Please insert flight capacity: ");
        
        clientWorker.addRequest("AddF--" + from + "--" + to + "--" + capacity);
        clientWorker.waitMain();
        ReaderWriter.printString(clientWorker.getResponse());
        ReaderWriter.pressEnterToContinue();
    }

    /**
     * adds one admin to the server
     */
    private void addAdmin() {
        
        String username = ReaderWriter.getString("Please insert username: ");
        String password = ReaderWriter.getString("Please insert password: ");
        clientWorker.addRequest("aDDA--" + username + "--" + password);
        clientWorker.waitMain();
        ReaderWriter.printString(clientWorker.getResponse());
        ReaderWriter.pressEnterToContinue();
    }




    
}