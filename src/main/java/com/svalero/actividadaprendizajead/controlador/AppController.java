package com.svalero.actividadaprendizajead.controlador;

import com.svalero.actividadaprendizajead.clases.Moto;
import com.svalero.actividadaprendizajead.modelo.MotoDAO;
import com.svalero.actividadaprendizajead.utilidades.Accion;
import com.svalero.actividadaprendizajead.utilidades.Alertas;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppController {

    public TextField tfMatricula, tfMarca, tfModelo;
    public ComboBox<String> cbTipo, cbTipoBuscar;
    public ListView<Moto> lvMotos;
    public Label lbConfirmacion;
    public Button btNuevo, btGuardar, btModificar, btEliminar, btCancelar, btBuscar;

    private MotoDAO motoDAO;
    private Moto motoSeleccionada;
    private Accion accion;




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


    /**
     * En caso de pulsarlo, mueve el foco a la matrícula y desactica los botones y casillas no necesarias para evitar problemas
     * Establece la acción en NUEVO
     * @param event
     */
    @FXML
    public void nuevaMoto(Event event) { // TODO arreglar bug, se realizan varias operaciones y se pulsa nuevo carga el tipo del último objeto seleccionado

        cargarDatos();
        tfMatricula.requestFocus();
        modoEdicion(true);
        accion = Accion.NUEVO;

    }


    /**
     * COmprueba que los campos estén rellenados, crea un objeto moto y según la accion (NUEVO, MODIFICAR) guarda o modifica la moto
     * @param event
     * @throws SQLException
     */
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
        if (tipo.isEmpty() || tipo == null || tipo == "<Seleccionar tipo>") {
            Alertas.mostrarError("Error Tipo", "El campo tipo no puede estar vacío");
            return;
        }

        Moto moto = new Moto(matricula, marca, modelo, tipo);

        try {

            switch (accion) {

                case NUEVO:
                    motoDAO.guardarMoto(moto);
                    lbConfirmacion.setText("moto guardada con éxito");
                    Alertas.mostrarInformacion("Guardado!", "La moto se ha guardado con éxito");
                    modoEdicion(false);
                    break;

                case MODIFICAR:
                    motoDAO.modificarMoto(motoSeleccionada, moto);
                    lbConfirmacion.setText("Moto modificada con éxito");
                    Alertas.mostrarInformacion("Modificado!", "La moto se ha modificado con éxito");
                    modoEdicion(false);
                    break;

            }

        } catch (SQLException sqle) {

            if (accion == Accion.NUEVO) {
                Alertas.mostrarError("Error", "Error al guardar la moto en la base de datos");
            }

            if (accion == Accion.MODIFICAR) {
                Alertas.mostrarError("Error", "Error al modificar la moto en la base de datos");
            }

        }

        cargarDatos();

    }




    /**
     * Activa el modo edición y establece la acción en modificar
     * @param event
     */
    @FXML
    public void modificarMoto(Event event) {

        modoEdicion(true);
        accion = Accion.MODIFICAR;

    }


    /**
     * Elimina la moto seleccionada de la base de datos. Muestra mensaje de confirmación antes de ello
     * @param event
     */
    @FXML
    public void eliminarMoto(Event event) {

        Moto motoAEliminar = lvMotos.getSelectionModel().getSelectedItem();

        if (motoAEliminar == null) {
            lbConfirmacion.setText("No has seleccionado ninguna moto");
            return;
        }

        try {


            // Si el boton pulsado en la alerta es cancelar, vuelve y no elimina la moto
            if (Alertas.mostrarConfirmación().get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                return;
            }

            motoDAO.eliminarMoto(motoAEliminar);
            lbConfirmacion.setText("Moto eliminada con éxito");
            cargarDatos();

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error", "Error al eliminar la moto de la base de datos");

        }

    }

    @FXML
    public void cancelar(Event event) {

        // Si el usuario cancela la operación vuelve
        if (Alertas.mostrarConfirmación().get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
            cargarDatos();
            return;
        } else {
            modoEdicion(false);
            cargarDatos();
        }


    }

    @FXML
    public void exportar(Event event) {
        // TODO implementar la exportación de datos
    }


    /**
     * Selecciona la moto del List View
     * @param event
     */
    @FXML
    public void getMotoListView(Event event) {

        motoSeleccionada = lvMotos.getSelectionModel().getSelectedItem();
        cargarMoto(motoSeleccionada);
        lbConfirmacion.setText("");

    }


    @FXML
    public void buscarTipo(Event event) {
        // TODO implementar busqueda por tipo de moto y cargar en el lvMotos
    }


    /**
     * Carga los datos de una moto (Se usa con el método Seleccionar moto para cargar los datos
     * la moto seleccionada en el List View)
     * @param moto
     */
    public void cargarMoto(Moto moto) {

        tfMatricula.setText(moto.getMatricula());
        tfMarca.setText(moto.getMarca());
        tfModelo.setText(moto.getModelo());
        cbTipo.setValue(moto.getTipo());

    }


    public void cargarDatos() {

        lvMotos.getItems().clear();

        try {

            ArrayList<Moto> listaMotos = motoDAO.getListaMotos();
            lvMotos.setItems(FXCollections.observableList(listaMotos));

            String[] tipoMoto = new String[] {"<Seleccionar tipo>", "DEPORTIVA", "TRAIL", "NAKED", "SPORT-TURISMO", "SCOOTER"};
            cbTipo.setItems(FXCollections.observableArrayList(tipoMoto));

            tfMatricula.setText("");
            tfMarca.setText("");
            tfModelo.setText("");
            cbTipo.setValue("<Seleccionar tipo>");


        } catch (SQLException throwables) {

            Alertas.mostrarError("Error Conexión", "Error al cargar los datos de la base de datos");

        }

    }


    /**
     * Activa y desactiva botones, casillas y lista según lo que se quiera hacer
     * @param activar
     */
    public void modoEdicion(boolean activar) {

        btNuevo.setDisable(activar);
        btGuardar.setDisable(!activar);
        btModificar.setDisable(activar);
        btEliminar.setDisable(activar);
        btCancelar.setDisable(!activar);

        tfMatricula.setEditable(activar);
        tfMarca.setEditable(activar);
        tfModelo.setEditable(activar);
        cbTipo.setEditable(activar);

        lvMotos.setDisable(activar);

    }



}
