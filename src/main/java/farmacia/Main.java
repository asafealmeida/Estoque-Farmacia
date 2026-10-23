package farmacia;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Classe que abre a janela.
public class Main extends Application {

    // O JavaFX chama este método ao iniciar. "Stage" é a janela.
    @Override
    public void start(Stage stage) {
        // O controlador monta a tela e tudo que a compoe
        MedicamentoController controller = new MedicamentoController();

        stage.setTitle("Estoque Medicamento");

        // "Scene" é o tamanho da janela
        stage.setScene(new Scene(controller.criarTela(), 900, 550));
        stage.show();
    }
}