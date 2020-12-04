package com.svalero.actividadaprendizajead;

import com.svalero.actividadaprendizajead.beans.Moto;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AppController {

    public TextField tfMatricula, tfMarca, tfModelo;
    public ComboBox<String> cbTipo;
    public ListView<Moto> lvMotos;
    public Label lbConfirmacion;
    public Button btNuevo, btGuardar, btModificar, btEliminar, btCancelar;

    private MotoDAO motoDAO;
    private Moto motoSeleccionada;

    public AppController() {
        // TODO instanciar objeto motoDAO y en esa clase implementar los métodos que trabajen con la base de datosS
    }

    @FXML
    public void nuevaMoto(Event event) {

    }

    @FXML
    public void guardarMoto(Event event) {

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
