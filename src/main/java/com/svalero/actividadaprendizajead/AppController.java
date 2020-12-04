package com.svalero.actividadaprendizajead;

import com.svalero.actividadaprendizajead.beans.Moto;
import com.svalero.actividadaprendizajead.utilidades.Alertas;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

public class AppController {

    public TextField tfMatricula, tfMarca, tfModelo;
    public ComboBox<String> cbTipo;
    public ListView<Moto> lvMotos;
    public Label lbConfirmacion;
    public Button btNuevo, btGuardar, btModificar, btEliminar, btCancelar;

    private MotoDAO motoDAO;
    private Moto motoSeleccionada;




    /**
     * Constructor del AppController que inicia la conexión con la base de datos cuando se instancia en el main
     */
    public AppController() {

        motoDAO = new MotoDAO();

        try {

            motoDAO.conectar();

        } catch (IOException e) {

            Alertas.mostrarError("Error de conexión","Error al leer el fichero properties");

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error de conexión","Error al conectar con la base de datos");

        } catch (ClassNotFoundException e) {

            Alertas.mostrarError("Error de conexión","Error al iniciar la aplicación");

        }

    }



    @FXML
    public void nuevaMoto(Event event) {

    }

    @FXML
    public void guardarMoto(Event event) throws SQLException {

        String matricula = tfMatricula.getText();
        String marca = tfMarca.getText();
        String modelo = tfModelo.getText();
        String tipo = cbTipo.getSelectionModel().getSelectedItem();

        if (matricula.isEmpty() || matricula == null) {
            Alertas.mostrarError("Error Matrícula", "El campo matrícula no puede estar vacío");
            return;
        }
        if (marca.isEmpty() || marca == null) {
            Alertas.mostrarError("Error Marca", "El campo marca no puede estar vacío");
            return;
        }
        if (modelo.isEmpty() || modelo == null) {
            Alertas.mostrarError("Error Modelo", "El campo modelo no puede estar vacío");
            return;
        }
//        if (tipo.isEmpty() || tipo == null) {
//            Alertas.mostrarError("Error Tipo", "El campo tipo no puede estar vacío");
//            return;
//        }

        Moto moto = new Moto(matricula, marca, modelo, tipo);
        motoDAO.guardarMoto(moto);
        lbConfirmacion.setText("Coche guardado con éxito");

    }

    @FXML
    public void modificarMoto(Event event) {

    }

    @FXML
    public void eliminarMoto(Event event) {

    }

    @FXML
    public void cancelar(Event event) {

    }

    @FXML
    public void exportar(Event event) {

    }

}
