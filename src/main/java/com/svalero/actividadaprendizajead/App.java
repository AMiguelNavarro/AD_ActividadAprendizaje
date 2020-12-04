package com.svalero.actividadaprendizajead;

import com.svalero.actividadaprendizajead.controlador.AppController;
import com.svalero.actividadaprendizajead.utilidades.R;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String [] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        AppController controlador = new AppController();

        loader.setLocation(R.getUI("interfaz_app.fxml"));
        loader.setController(controlador);

        VBox vbox = loader.load();

        controlador.cargarDatos();
        controlador.modoEdicion(false);

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();

    }


    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
