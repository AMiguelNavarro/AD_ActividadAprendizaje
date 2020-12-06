package com.svalero.actividadaprendizajead.controlador;

import com.svalero.actividadaprendizajead.clases.Moto;
import com.svalero.actividadaprendizajead.modelo.MotoDAO;
import com.svalero.actividadaprendizajead.utilidades.Accion;
import com.svalero.actividadaprendizajead.utilidades.Alertas;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class App_Controlador {

    public TextField tfMatricula, tfMarca, tfModelo;
    public ComboBox<String> cbTipo, cbTipoBuscar;
    public ListView<Moto> lvMotos;
    public Button btNuevo, btGuardar, btModificar, btEliminar, btCancelar, btBuscar, btEliminarBD, btRecuperar;

    private MotoDAO motoDAO;
    private Moto motoSeleccionada;
    private Accion accion;




    /**
     * Constructor del AppController que inicia la conexión con la base de datos cuando se instancia en el main
     */
    public App_Controlador() {

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
    public void nuevaMoto(Event event) { //

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
                    Alertas.mostrarInformacion("Guardado!", "La moto se ha guardado con éxito");
                    modoEdicion(false);
                    break;

                case MODIFICAR:

                    motoDAO.modificarMoto(motoSeleccionada, moto);
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
        Moto.motoRecuperar = motoAEliminar;

        if (motoAEliminar == null) {
            Alertas.mostrarError("Error", "No has seleccionado ningunas moto");
            return;
        }

        try {


            // Si el boton pulsado en la alerta es cancelar, vuelve y no elimina la moto
            if (Alertas.mostrarConfirmación().get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                return;
            }

            motoDAO.eliminarMoto(motoAEliminar);
            Alertas.mostrarInformacion("Eliminada!", "Moto eliminada con éxito");
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


    /**
     * Exporta los datos de la base de datos a un archivo CSV
     * @param event
     */
    @FXML
    public void exportar(Event event) {

        try {

            FileChooser fileChooser = new FileChooser();
            File fichero = fileChooser.showSaveDialog(null);

            //CSVPrinter requiere de un FileWriter
            FileWriter fileWriter = new FileWriter(fichero);
            CSVPrinter printer = new CSVPrinter(fileWriter, CSVFormat.EXCEL.withHeader("Matrícula", "Marca", "Modelo", "Tipo"));

            ArrayList<Moto> motos = motoDAO.getListaMotos();

                for (Moto moto : motos) {

                    printer.printRecord(moto.getMatricula(), moto.getMarca(), moto.getModelo(), moto.getTipo());

                }

            Alertas.mostrarInformacion("Exportado!", "Archivo CSV exportado con éxito");
            printer.close();

        } catch (IOException e) {

            Alertas.mostrarError("Error", "Error al crear el fichero para exportar");

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error", "Erro al exportar conectando con la base de datos");

        }

    }


    /**
     * Selecciona la moto del List View
     * @param event
     */
    @FXML
    public void getMotoListView(Event event) {

        motoSeleccionada = lvMotos.getSelectionModel().getSelectedItem();
        cargarMoto(motoSeleccionada);

    }


    /**
     * Busca la moto por tipo puesto en el combo Box
     * Imprime los datos que recibe del arrayList en el listView
     * @param event
     */
    @FXML
    public void buscarTipo(Event event) {

        lvMotos.getItems().clear();

        modoEdicion(false);

        try {

            String tipo = cbTipoBuscar.getSelectionModel().getSelectedItem();

            if (tipo == null || tipo == "<Seleccionar tipo>") {

                Alertas.mostrarError("Error" , "Debes seleccionar un tipo de moto");
                return;

            }

            ArrayList<Moto> listaMotos = motoDAO.getListaMotosPorTipo(tipo);

            if (listaMotos.isEmpty()) {

                Alertas.mostrarInformacion("Lo sentimos!", "No hay ningúna moto de tipo " + tipo + " en la base de datos");
                cargarDatos();
                return;

            }

            lvMotos.setItems(FXCollections.observableList(listaMotos));

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error", "Erro al filtrar por tipo de moto en la base de datos");

        }


    }


    /**
     *
     * Elimina todos los datos de la base de datos
     * @param event
     */
    @FXML
    public void eliminarBaseDatos(Event event) {
        //TODO en un procedimiento almacenado
        // TODO ¿MOSTRAR TOTAL DE MOTOS CON UNA FUNCIÓN ALMACENADA?

        try {

            // Si el boton pulsado en la alerta es cancelar, vuelve y no elimina la base de datos
            if (Alertas.mostrarConfirmación().get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {

                return;

            } else {

                motoDAO.eliminarBaseDatos();
                Alertas.mostrarInformacion("BD Eliminada!", "Base de datos eliminada con éxito");
                cargarDatos();

            }

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error", "Error al eliminar la base de datos");

        }

    }


    /**
     * Recupera la última moto eliminada de la base de datos
     * @param event
     */
    @FXML
    public void recuperarUltimaMotoEliminada(Event event) {

        try {

            if (Moto.motoRecuperar == null) {
                Alertas.mostrarInformacion("UPS!", "Aún  no has eliminado ninguna moto de la base de datos");
                return;
            }

            motoDAO.guardarMoto(Moto.motoRecuperar);
            Alertas.mostrarInformacion("Recuperada!", "Se ha recuperado la última moto eliminada: " + Moto.motoRecuperar.getMarca() + " " + Moto.motoRecuperar.getModelo());
            cargarDatos();

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error" , "No se ha podido recuperar la última moto eliminada");

        }

    }







    /**
     * Carga los datos de una moto (Se usa con el método seleccionarMoto para cargar los datos
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
            cbTipoBuscar.setItems(FXCollections.observableArrayList(tipoMoto));

            tfMatricula.setText("");
            tfMarca.setText("");
            tfModelo.setText("");
            cbTipo.setValue("<Seleccionar tipo>");
            cbTipoBuscar.setValue("<Seleccionar tipo>");


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
//        cbTipo.setEditable(activar);

        lvMotos.setDisable(activar);

    }



}
