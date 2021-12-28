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
    }

    // Todo: WOrker part
    private void authenticate() {
        Menu menu = new Menu(new String[] {
                "Sign in",
                "Sign up"
        });
        menu.setHandler(1, () -> signIn());
        menu.setHandler(2, () -> signUp());

        menu.runOnce();
    }

    // Todo: WOrker part
    private void signIn() {
        System.out.println("Please insert your username: ");
        String username = scin.nextLine();
        System.out.println("Please insert your password: ");
        String password = scin.nextLine();
        System.out.println("loading...");
        worker.addRequest(username + " " + password);
        worker.waitMain();
        worker.getResponses("signIn");
        
        
    }

    // Todo: Worker part
    private void signUp() {
        System.out.println("Please insert your username: ");
        String username = scin.nextLine();
        System.out.println("Please insert your password: ");
        String password = scin.nextLine();
        System.out.println("loading...");
    }

    // Métodos auxiliares - Estados da UI

    /**
     * Estado - Menu Principal
     *
     * Transições para:
     * Operações sobre Alunos
     * Operações sobre Turmas
     * Adicionar Aluno a Turma
     * Remover Aluno de Turma
     * Listar Alunos de Turma
     */
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

        // mais pré-condições?

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

    /**
     * TODO adicionar worker part
     */
    private void verifyFlights() {

        System.out.println("From: ");
        String from = scin.nextLine();
        System.out.println("To: ");
        String to = scin.nextLine();
        System.out.println("Depart: ");
        int depart = readInt();

    }

    /**
     * TODO: ADD worker Part
     * no sleep
     */
    private void makeReservation() {
        System.out.println("Please Insert flight number: ");
        int flight = readInt();
    }

    /**
     * TODO: ADD Worker Part
     * no sleep
     */
    private void cancelReservation() {
        System.out.println("Please Insert reservation number: ");
        int reservation = readInt();
    }

    /**
     * TODO: ADD Worker Part
     * falta o pedido das reservas anteriores
     */
    private void checkReservations() {
        worker.getResponses("reservations");
    }

    /**
     * TODO: ADD Worker Part
     * sleep?
     */
    private void endDay() {
        System.out.println("Please insert day: ");
        int day = readInt();
    }

    /**
     * TODO: ADD Worker Part
     * sleep?
     */
    private void addFlight() {
        System.out.println("Please insert From: ");
        String from = scin.nextLine();
        System.out.println("Please insert To: ");
        String to = scin.nextLine();
        System.out.println("Please insert flight capacity: ");
        int capacity = readInt();
    }

    /**
     * TODO: ADD WORKER PART
     * no sleep
     */
    private void addAdmin() {
        System.out.println("Please insert username: ");
        String username = scin.nextLine();
        System.out.println("Insert password: ");
        String password = scin.nextLine();
    }

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