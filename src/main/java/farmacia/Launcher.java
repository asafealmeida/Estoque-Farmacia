package farmacia;

import javafx.application.Application;

// Classe de entrada do programa (é ela que você executa no IntelliJ).
// Fica separada da Main para evitar erro no JavaFX.
public class Launcher {

    public static void main(String[] args) {
       //chama o start da classe main
        Application.launch(Main.class, args);
    }
}