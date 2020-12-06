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
import java.sql.SQLException;

public class Registro_Controlador {

    public TextField tfNombreUsuario;
    public PasswordField pfContrasenia;
    public Button btRegistro, btVolver;

    private UsuariosDAO usuariosDAO;



    public Registro_Controlador() {

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
                return;
            }
            if (contrasenia.isEmpty() || contrasenia == "") {
                Alertas.mostrarError("Error", "La contraseña no puede estar vacía");
                return;
            }

            Usuario usuario = new Usuario(nombreUsuario, contrasenia);
            usuariosDAO.registrarUsuario(usuario);

            FXMLLoader loader = new FXMLLoader();
            App_Controlador appControlador = new App_Controlador();

            loader.setLocation(R.getUI("interfaz_app.fxml"));
            loader.setController(appControlador);

            Parent root = loader.load();

            appControlador.cargarDatos();
            appControlador.modoEdicion(false);

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            // Capturo el stage actual para cerrarlo
            Stage myStage = (Stage) this.btRegistro.getScene().getWindow();
            myStage.close();

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error", "Error al registrar el usuario en la base de datos");

        } catch (IOException e) {

            Alertas.mostrarError("Error", "Problemas al cargar la ventana de la APP");

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
            Stage myStage = (Stage) this.btRegistro.getScene().getWindow();
            myStage.close();



        } catch (IOException e) {

            Alertas.mostrarError("Error", "No se ha podido abrir la ventana inicial del programa");

        }

    }

}
