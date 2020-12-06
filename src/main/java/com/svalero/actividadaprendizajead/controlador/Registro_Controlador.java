package com.svalero.actividadaprendizajead.controlador;

import com.svalero.actividadaprendizajead.utilidades.Alertas;
import com.svalero.actividadaprendizajead.utilidades.R;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Registro_Controlador {

    public TextField tfNombreUsuario;
    public PasswordField pfContrasenia;
    public Button btRegistro, btVolver;



    @FXML
    public void  registrarUsuario(Event event) {
        //TODO validar datos y registrar usuario en la base de datos
    }


    /**
     * Método que vuelve a la ventana principal del programa
     * @param event
     */
    @FXML
    public void volverInicio(Event event) {

        try {

            FXMLLoader loader = new FXMLLoader();
            Inicio_Controlador inicioControlador = new Inicio_Controlador();

            loader.setLocation(R.getUI("inicio.fxml"));
            loader.setController(inicioControlador);

            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            // Capturo el stage actual para cerrarlo
            Stage myStage = (Stage) this.btRegistro.getScene().getWindow();
            myStage.close();



        } catch (IOException e) {

            Alertas.mostrarError("Error", "No se ha podido abrir la ventana inicial del programa");

        }

    }

}
