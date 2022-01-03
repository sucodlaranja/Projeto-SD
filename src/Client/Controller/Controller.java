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

    /**
     * handles signIn operation and starts RequestWorker so it can communicate with the server
     */

    private void signIn() {
        String username = ReaderWriter.getString("Please insert your username: ");
        String password = ReaderWriter.getString("Please insert your password");

        ReaderWriter.printString("loading...");
        
        clientWorker.addRequest("signIn " + username + " " + password);

        this.clientWorker.startRequestWorker(); ///Starts worker
        clientWorker.waitMain();
        ReaderWriter.printString(clientWorker.getResponse());
        ReaderWriter.pressEnterToContinue();
    }

    /**
     * handles signUp operation and starts RequestWorker so it can communicate with the server
     */
    private void signUp() {
        String username = ReaderWriter.getString("Please insert your username: ");
        String password = ReaderWriter.getString("Please insert your password");
        this.clientWorker.startRequestWorker(); //<- Start worker - manage client interactions with the server using RequestWorker.
        ReaderWriter.printString("loading...");
        clientWorker.addRequest("signUp " + username + " " + password);
        clientWorker.waitMain();
        ReaderWriter.printString(clientWorker.getResponse());
        ReaderWriter.pressEnterToContinue();
    }

    /**
     *  Request all available flights from To in specific day
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
     * Requests a reservation
     */
    private void makeReservation() {
        int flight = ReaderWriter.getInt("Please Insert flight number: ");
        int day = ReaderWriter.getInt("Please Insert day of flight: ");
        clientWorker.addRequest("AddR" + flight + " " + day);

    }

    /**
     * Cancels a reservation
     */
    private void cancelReservation() {
        int reservation = ReaderWriter.getInt("Please Insert reservation number: ");
        clientWorker.addRequest("RemR:" + reservation);
        
        
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
        clientWorker.addRequest("End " + day);
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
        
        clientWorker.addRequest("AddF " + from + " " + to + " " + capacity);
    }

    /**
     * adds one admin to the server
     */
    private void addAdmin() {
        
        String username = ReaderWriter.getString("Please insert username: ");
        String password = ReaderWriter.getString("Please insert password: ");
        clientWorker.addRequest("aDDA " + username + " " + password);
    }




    
}