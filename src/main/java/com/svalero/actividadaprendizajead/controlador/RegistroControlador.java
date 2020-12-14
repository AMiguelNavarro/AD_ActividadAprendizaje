package com.svalero.actividadaprendizajead.controlador;

import com.svalero.actividadaprendizajead.clases.Usuario;
import com.svalero.actividadaprendizajead.modelo.UsuariosDAO;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class RegistroControlador {

    public TextField tfNombreUsuario;
    public PasswordField pfContrasenia;
    public Button btRegistro, btVolver;

    private UsuariosDAO usuariosDAO;
    private static final Logger logger = LogManager.getLogger(RegistroControlador.class);



    public RegistroControlador() {

        usuariosDAO = new UsuariosDAO();

        try {

            usuariosDAO.conectar();

            logger.trace("Se conecta con la base de datos para el registro");

        } catch (IOException e) {

            Alertas.mostrarError("Error de conexión","Error al leer el fichero properties");

            logger.error("Error al conectar con la base de datos en el fichero properties " + e.fillInStackTrace());

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error de conexión","Error al conectar con la base de datos");

            logger.error("Error al conectar con la base de datos " + throwables.fillInStackTrace());

        } catch (ClassNotFoundException e) {

            Alertas.mostrarError("Error de conexión","Error al iniciar la aplicación");

            logger.error("Error al conectar con la base de datos e iniciar la aplicación " + e.fillInStackTrace());

        }

    }

    /**
     * Registra el usuario en la base de datos, si tiene existo carga la ventan de la app sino no hace nada
     * @param event
     */
    @FXML
    public void  registrarUsuario(Event event) {

        try {

            String nombreUsuario = tfNombreUsuario.getText();
            String contrasenia = String.valueOf(pfContrasenia.getText());

            if (nombreUsuario.isEmpty() || nombreUsuario == "") {
                Alertas.mostrarError("Error", "El nombre de usuario no puede estar vacio");

                logger.trace("Se intenta registrar un nombre de usuario vacio");
                return;
            }
            if (contrasenia.isEmpty() || contrasenia == "") {
                Alertas.mostrarError("Error", "La contraseña no puede estar vacía");

                logger.trace("Se intenta registrar una contraseña vacia");
                return;
            }

            Usuario usuario = new Usuario(nombreUsuario, contrasenia);
            usuariosDAO.registrarUsuario(usuario);

            FXMLLoader loader = new FXMLLoader();
            AppControlador appControlador = new AppControlador();

            loader.setLocation(R.getUI("interfaz_app.fxml"));
            loader.setController(appControlador);

            Parent root = loader.load();

            appControlador.cargarDatos();
            appControlador.modoEdicion(false);

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            logger.trace("Nuevo usuario " + nombreUsuario + " registrado con éxito, se da paso a la ventana de la app");

            // Capturo el stage actual para cerrarlo
            Stage myStage = (Stage) this.btRegistro.getScene().getWindow();
            myStage.close();

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error", "Error al registrar el usuario en la base de datos");

            logger.error("Error al registrar el usuario"+ tfNombreUsuario.getText() +" en la base de datos");

        } catch (IOException e) {

            Alertas.mostrarError("Error", "Problemas al cargar la ventana de la APP");

            logger.error("Problemas al cargar la ventana de la app " + e.fillInStackTrace());

        }

    }


    /**
     * Método que vuelve a la ventana principal del programa
     * @param event
     */
    @FXML
    public void volverInicio(Event event) {

        try {

            FXMLLoader loader = new FXMLLoader();
            InicioControlador inicioControlador = new InicioControlador();

            loader.setLocation(R.getUI("inicio.fxml"));
            loader.setController(inicioControlador);

            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            logger.trace("Se vuelve a la primera ventana de la app (Inicio y registro)");

            // Capturo el stage actual para cerrarlo
            Stage myStage = (Stage) this.btRegistro.getScene().getWindow();
            myStage.close();



        } catch (IOException e) {

            Alertas.mostrarError("Error", "No se ha podido abrir la ventana inicial del programa");

            logger.error("Error al cargar la primera ventana de la app");

        }

    }

}
