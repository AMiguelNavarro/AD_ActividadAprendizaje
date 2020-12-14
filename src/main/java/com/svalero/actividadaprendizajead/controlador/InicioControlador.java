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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class InicioControlador {

    public Button btIniciarSesion, btRegistrarse;

    private static final Logger logger = LogManager.getLogger(InicioControlador.class);



    @FXML
    public void iniciarSesion(Event event) {

        try {

            FXMLLoader loader = new FXMLLoader();
            InicioSesionControlador inicioSesionControlador = new InicioSesionControlador();

            loader.setLocation(R.getUI("inicio_sesion.fxml"));
            loader.setController(inicioSesionControlador);

            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            logger.trace("Se navega la ventana de iniciar sesión");

            // Capturo el stage actual para cerrarlo
            Stage myStage = (Stage) this.btIniciarSesion.getScene().getWindow();
            myStage.close();



        } catch (IOException e) {

            Alertas.mostrarError("Error", "No se ha podido abrir la ventana de inicio de sesión");

            logger.error("Hay un error al navegar a la ventana de inicio de sesión " + e.fillInStackTrace());

        }

    }


    @FXML
    public void registrarse(Event event) {

        try {

            FXMLLoader loader = new FXMLLoader();
            RegistroControlador registroControlador = new RegistroControlador();

            loader.setLocation(R.getUI("registro.fxml"));
            loader.setController(registroControlador);

            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            logger.trace("Se navega la ventana de registro de nuevo usuario");

            // Capturo el stage actual para cerrarlo
            Stage myStage = (Stage) this.btIniciarSesion.getScene().getWindow();
            myStage.close();



        } catch (IOException e) {

            Alertas.mostrarError("Error", "No se ha podido abrir la ventana de registro");

            logger.error("Hay un error al navegar a la ventana de registro de usuario " + e.fillInStackTrace());

        }


    }





}
