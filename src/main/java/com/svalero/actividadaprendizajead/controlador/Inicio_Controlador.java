package com.svalero.actividadaprendizajead.controlador;

import com.svalero.actividadaprendizajead.utilidades.Alertas;
import com.svalero.actividadaprendizajead.utilidades.R;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Inicio_Controlador {

    public Button btIniciarSesion, btRegistrarse;



    @FXML
    public void iniciarSesion(Event event) {

        try {

            FXMLLoader loader = new FXMLLoader();
            InicioSesion_Controlador inicioSesionControlador = new InicioSesion_Controlador();

            loader.setLocation(R.getUI("inicio_sesion.fxml"));
            loader.setController(inicioSesionControlador);

            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            // Capturo el stage actual para cerrarlo
            Stage myStage = (Stage) this.btIniciarSesion.getScene().getWindow();
            myStage.close();



        } catch (IOException e) {

            Alertas.mostrarError("Error", "No se ha podido abrir la ventana de inicio de sesión");

        }

    }


    @FXML
    public void registrarse(Event event) {

        try {

            FXMLLoader loader = new FXMLLoader();
            Registro_Controlador registroControlador = new Registro_Controlador();

            loader.setLocation(R.getUI("registro.fxml"));
            loader.setController(registroControlador);

            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            // Capturo el stage actual para cerrarlo
            Stage myStage = (Stage) this.btIniciarSesion.getScene().getWindow();
            myStage.close();



        } catch (IOException e) {

            Alertas.mostrarError("Error", "No se ha podido abrir la ventana de registro");

        }


    }





}
