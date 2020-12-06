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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class InicioSesion_Controlador {

    public TextField tfNombreUsuario;
    public PasswordField pfContrasenia;
    public Button btValidarInicioSesion, btVolver;

    private UsuariosDAO usuariosDAO;


    public InicioSesion_Controlador() {

        usuariosDAO = new UsuariosDAO();

        try {

            usuariosDAO.conectar();

        } catch (IOException e) {

            Alertas.mostrarError("Error de conexión","Error al leer el fichero properties");

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error de conexión","Error al conectar con la base de datos");

        } catch (ClassNotFoundException e) {

            Alertas.mostrarError("Error de conexión","Error al iniciar la aplicación");

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
                return;
            }
            if (contrasenia.isEmpty() || contrasenia == ""){
                Alertas.mostrarError("Error", "La contraseña no puede estar vacía");
                return;
            }

            Usuario usuario = new Usuario(nombreUsuario, contrasenia);

            existe = usuariosDAO.validarUsuario(usuario);

            // Si existe
            if (existe) {

                try {

                    FXMLLoader loader = new FXMLLoader();
                    App_Controlador appControlador = new App_Controlador();

                    loader.setLocation(R.getUI("interfaz_app.fxml"));
                    loader.setController(appControlador);

                    Parent root = loader.load();

                    Scene scene = new Scene(root);
                    Stage stage = new Stage();

                    stage.setScene(scene);
                    stage.show();

                    // Capturo el stage actual para cerrarlo
                    Stage myStage = (Stage) this.btValidarInicioSesion.getScene().getWindow();
                    myStage.close();



                } catch (IOException e) {

                    Alertas.mostrarError("Error", "No se ha podido abrir la ventana de inicio de sesión");

                }

            // Si no existe
            } else {
                Alertas.mostrarError("Error", "El usuario introducido no existe en la base de datos");
            }

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error", "Error al validar el usuario");

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
            Inicio_Controlador inicioControlador = new Inicio_Controlador();

            loader.setLocation(R.getUI("inicio.fxml"));
            loader.setController(inicioControlador);

            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            // Capturo el stage actual para cerrarlo
            Stage myStage = (Stage) this.btValidarInicioSesion.getScene().getWindow();
            myStage.close();



        } catch (IOException e) {

            Alertas.mostrarError("Error", "No se ha podido abrir la ventana inicial del programa");

        }

    }


}
