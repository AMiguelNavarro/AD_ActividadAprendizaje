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

public class InicioSesionControlador {

    public TextField tfNombreUsuario;
    public PasswordField pfContrasenia;
    public Button btValidarInicioSesion, btVolver;

    private UsuariosDAO usuariosDAO;
    private static final Logger logger = LogManager.getLogger(InicioSesionControlador.class);


    public InicioSesionControlador() {

        usuariosDAO = new UsuariosDAO();

        try {

            usuariosDAO.conectar();

            logger.trace("Se conecta con la base de datos para iniciar sesión");

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
     * Comprueba si el usuario existe en la base de datos mediante el método validarUsuario() del DAO
     *  Si existe y los datos son correctos pasa a la ventana de la app
     * @param event
     */
    @FXML
    public void validarInicioSesion(Event event) {

        try {

            String nombreUsuario = tfNombreUsuario.getText();
            String contrasenia = String.valueOf(pfContrasenia.getText());
            boolean existe;

            if (nombreUsuario.isEmpty() || nombreUsuario == "") {
                Alertas.mostrarError("Error", "El nombre de usuario no puede estar vacio");

                logger.trace("Se intenta iniciar sesión pero el nombre de usuario está vacío");
                return;
            }
            if (contrasenia.isEmpty() || contrasenia == ""){
                Alertas.mostrarError("Error", "La contraseña no puede estar vacía");
                logger.trace("Se intenta iniciar sesión pero la contraseña está vacía");
                return;
            }

            Usuario usuario = new Usuario(nombreUsuario, contrasenia);

            existe = usuariosDAO.validarUsuario(usuario);

            // Si existe
            if (existe) {

                logger.trace("El usuario introducido existe, se da paso a la aplicación");

                try {

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

                    logger.trace("Se carga la ventana de la aplicación");

                    // Capturo el stage actual para cerrarlo
                    Stage myStage = (Stage) this.btValidarInicioSesion.getScene().getWindow();
                    myStage.close();



                } catch (IOException e) {

                    Alertas.mostrarError("Error", "No se ha podido abrir la ventana de inicio de la aplicación");

                    logger.error("Error al cargar la ventana de inicio de la app " + e.fillInStackTrace());

                }

            // Si no existe
            } else {
                Alertas.mostrarError("Error", "El usuario introducido no existe en la base de datos");

                logger.trace("El usuario introducido no existe");
            }

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error", "Error al validar el usuario");

            logger.error("Error al validar el usuario en la base de datos " + throwables.fillInStackTrace());

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
            Stage myStage = (Stage) this.btValidarInicioSesion.getScene().getWindow();
            myStage.close();



        } catch (IOException e) {

            Alertas.mostrarError("Error", "No se ha podido abrir la ventana inicial del programa");

            logger.error("Error al cargar la primera ventana de la app");

        }

    }


}
