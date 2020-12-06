package com.svalero.actividadaprendizajead;

import com.svalero.actividadaprendizajead.controlador.Inicio_Controlador;
import com.svalero.actividadaprendizajead.utilidades.R;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
//        AppController controlador = new AppController();
        Inicio_Controlador controlador = new Inicio_Controlador();

        loader.setLocation(R.getUI("inicio.fxml"));
        loader.setController(controlador);

        Parent root = loader.load();

//        controlador.cargarDatos();
//        controlador.modoEdicion(false);

        Scene scene = new Scene(root);
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
