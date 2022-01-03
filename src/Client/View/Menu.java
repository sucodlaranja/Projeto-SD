package Client.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu {

    // Auxiliar Interfaces

    /** Functional interface for handlers. */
    public interface Handler {
        void execute();
    }

    /** Functional interface for pre conditions. */
    public interface PreCondition {
        boolean validate();
    }

    // Instance Variables

    private final String titulo; // Menu Title (opcional)
    private final List<String> opcoes; // List of options
    private final List<PreCondition> disponivel; // List of pre conditions
    private final List<Handler> handlers; // List of handlers

    // Construtors

    /**
     * Constructor for objects of the Menu class (title an List of options).
     *
     * Creates an option menu without event handlers.
     *
     * @param titulo The menu title
     * @param opcoes A list of Strings with menu options.
     */
    public Menu(String titulo, List<String> opcoes) {
        this.titulo = titulo;
        this.opcoes = new ArrayList<>(opcoes);
        this.disponivel = new ArrayList<>();
        this.handlers = new ArrayList<>();
        this.opcoes.forEach(s -> {
            this.disponivel.add(() -> true);
            this.handlers.add(() -> System.out.println("\nATENÇÃO: Opção não implementada!"));
        });
    }

    /**
     * Constructor for objects of the Menu class (without title and with List of options).
     *
     * @param opcoes A list of Strings with menu options.
     */
    public Menu(List<String> opcoes) {
        this("Menu", opcoes);
    }


    /**
     * Constructor para objectos da classe Menu (sem título e com array de opções).
     *
     *
     * new Menu(String[]{
     * "Opção 1",
     * "Opção 2",
     * "Opção 3"
     * })
     *
     * @param opcoes Um array de Strings com as opções do menu.
     */
    public Menu(String[] opcoes) {
        this(Arrays.asList(opcoes));
    }


    /**
     * Runs menu once
     */
    public void runOnce() {
        int op;
        show();
        op = readOption();
        // testar pré-condição
        if (op > 0 && !this.disponivel.get(op - 1).validate()) {
            System.out.println("Opção indisponível!");
        } else if (op > 0) {
            // executar handler
            this.handlers.get(op - 1).execute();
        }
    }

    /**
     * Run the menu multiple times.
     *
     * Ends with option 0.
     */
    public void run() {
        int op;
        do {
            show();
            op = readOption();
            // testar pré-condição
            if (op > 0 && !this.disponivel.get(op - 1).validate()) {
                System.out.println("Opção indisponível! Tente novamente.");
            } else if (op > 0) {
                // executar handler
                this.handlers.get(op - 1).execute();
            }
        } while (op != 0);
    }


    /**
     * Method for registering a handler in a menu option.
     *
     * @param i option index (starts at 1)
     * @param h handlers to register
     */
    public void setHandler(int i, Handler h) {
        this.handlers.set(i - 1, h);
    }

    // Auxiliary methods

    /** display the menu */
    private void show() {
        System.out.println("\n *** " + this.titulo + " *** ");
        for (int i = 0; i < this.opcoes.size(); i++) {
            System.out.print(i + 1);
            System.out.print(" - ");
            System.out.println(this.disponivel.get(i).validate() ? this.opcoes.get(i) : "---");
        }
        System.out.println("0 - Sair");
    }

    /** Reads a valid option */
    private int readOption() {
        int op;

        System.out.print("Opção: ");
        try {
            op = ReaderWriter.getInt();
        } catch (NumberFormatException e) { // Não foi inscrito um int
            op = -1;
        }
        if (op < 0 || op > this.opcoes.size()) {
            System.out.println("Opção Inválida!!!");
            op = -1;
        }
        return op;
    }
}
