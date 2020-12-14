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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppControlador {

    public TextField tfMatricula, tfMarca, tfModelo;
    public ComboBox<String> cbTipo, cbTipoBuscar;
    public ListView<Moto> lvMotos;
    public Button btNuevo, btGuardar, btModificar, btEliminar, btCancelar, btBuscar, btEliminarBD, btRecuperar;

    private MotoDAO motoDAO;
    private Moto motoSeleccionada;
    private Accion accion;

    private static final Logger logger = LogManager.getLogger(AppControlador.class);




    /**
     * Constructor del AppController que inicia la conexión con la base de datos cuando se instancia en el main
     */
    public AppControlador() {

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

        logger.trace("Se pulsa el boton de nueva moto");

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
    public void guardarMoto(Event event){

        String matricula = tfMatricula.getText();
        String marca = tfMarca.getText();
        String modelo = tfModelo.getText();
        String tipo = cbTipo.getSelectionModel().getSelectedItem();

        if (matricula.isEmpty() || matricula == null) {
            Alertas.mostrarError("Error Matrícula", "El campo matrícula no puede estar vacío");
            logger.trace("Error al guardar la moto, el campo matrícula está vacío");
            return;
        }
        if (marca.isEmpty() || marca == null) {
            Alertas.mostrarError("Error Marca", "El campo marca no puede estar vacío");
            logger.trace("Error al guardar la moto, el campo marca está vacío");
            return;
        }
        if (modelo.isEmpty() || modelo == null) {
            Alertas.mostrarError("Error Modelo", "El campo modelo no puede estar vacío");
            logger.trace("Error al guardar la moto, el campo modelo está vacío");
            return;
        }
        if (tipo.isEmpty() || tipo == null || tipo == "<Seleccionar tipo>") {
            Alertas.mostrarError("Error Tipo", "El campo tipo no puede estar vacío");
            logger.trace("Error al guardar la moto, el campo tipo está vacío");
            return;
        }

        Moto moto = new Moto(matricula, marca, modelo, tipo);

        try {

            switch (accion) {

                case NUEVO:

                    motoDAO.guardarMoto(moto);
                    Alertas.mostrarInformacion("Guardado!", "La moto se ha guardado con éxito");
                    modoEdicion(false);

                    logger.trace("Se guarda una NUEVA moto en la base de datos: "
                                    + moto.getMatricula() + ", "
                                    + moto.getMarca() + ", "
                                    + moto.getModelo() + ", "
                                    + moto.getTipo()
                    );
                    break;

                case MODIFICAR:

                    motoDAO.modificarMoto(motoSeleccionada, moto);
                    Alertas.mostrarInformacion("Modificado!", "La moto se ha modificado con éxito");
                    modoEdicion(false);

                    logger.trace("Se MODIFICA la moto: "
                                        + motoSeleccionada.getMatricula() + ", "
                                        + motoSeleccionada.getMarca() + ", "
                                        + motoSeleccionada.getModelo() + ", "
                                        + motoSeleccionada.getTipo() +
                                        ", a --> "
                                            + moto.getMatricula() + ", "
                                            + moto.getMarca() + ", "
                                            + moto.getModelo() + ", "
                                            + moto.getTipo()
                    );
                    break;

            }

        } catch (SQLException sqle) {

            if (accion == Accion.NUEVO) {
                Alertas.mostrarError("Error", "Error al guardar la moto en la base de datos");

                logger.error("Error al guardar la nueva moto en la base de datos " + sqle.fillInStackTrace());
            }

            if (accion == Accion.MODIFICAR) {
                Alertas.mostrarError("Error", "Error al modificar la moto en la base de datos");

                logger.error("Error al modificar la moto en la base de datos " + sqle.fillInStackTrace());
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

        logger.trace("Se pulsa el botón de modificar una moto");

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

            logger.trace("Se intenta eliminar una moto pero no se ha seleccionado ninguna");
            return;
        }

        try {


            // Si el boton pulsado en la alerta es cancelar, vuelve y no elimina la moto
            if (Alertas.mostrarConfirmación().get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                logger.trace("Se cancela la operación de eliminar moto");
                return;
            }

            motoDAO.eliminarMoto(motoAEliminar);
            Alertas.mostrarInformacion("Eliminada!", "Moto eliminada con éxito");

            logger.trace("Se elimina la moto con éxito "
                                + motoAEliminar.getMatricula()
                                + ", " + motoAEliminar.getMarca()
                                + ", " + motoAEliminar.getModelo()
                                + ", " +motoAEliminar.getTipo()
            );

            cargarDatos();

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error", "Error al eliminar la moto de la base de datos");

            logger.error("Error al eliminar la moto de la base de datos " + throwables.fillInStackTrace());

        }

    }

    @FXML
    public void cancelar(Event event) {

        // Si no cancela la operación se queda ahí
        if (Alertas.mostrarConfirmación().get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
            logger.trace("Se pulsa cancelar en la alerta de confirmación");
            cargarDatos();
            return;

        // Si el usuario cancela la operación vuelve
        } else {
            logger.trace("Se pulsa aceptar en la alerta de confirmación");
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

            logger.trace("Se exporta la base de datos con las motos correctamente");

        } catch (IOException e) {

            Alertas.mostrarError("Error", "Error al crear el fichero para exportar");

            logger.error("Error al exportar el fichero con la base de datos de las motos " + e.fillInStackTrace());

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error", "Erro al exportar conectando con la base de datos");

            logger.error("Error al conectar con la base de datos para exportar los datos " + throwables.fillInStackTrace());

        }

    }


    /**
     * Selecciona la moto del List View
     * @param event
     */
    @FXML
    public void getMotoListView(Event event) {

        motoSeleccionada = lvMotos.getSelectionModel().getSelectedItem();

        if (motoSeleccionada == null) {
            Alertas.mostrarInformacion("Lista de motos", "No has seleccionado ninguna moto de la lista");
            logger.trace("Se selecciona un hueco vacío del ListView");
        }

        cargarMoto(motoSeleccionada);

        logger.trace("Se selecciona una moto del listView");

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

                logger.trace("Se intenta filtrar por tipo de moto pero no se selecciona ninguno");

                return;

            }

            ArrayList<Moto> listaMotos = motoDAO.getListaMotosPorTipo(tipo);

            if (listaMotos.isEmpty()) {

                Alertas.mostrarInformacion("Lo sentimos!", "No hay ningúna moto de tipo " + tipo + " en la base de datos");

                logger.trace("No hay ningún tipo de moto " + tipo + " en la base de datos");

                cargarDatos();
                return;

            }

            lvMotos.setItems(FXCollections.observableList(listaMotos));

            logger.trace("Se cargan todas las motos del tipo " + tipo + " de la base de datos");

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error", "Erro al filtrar por tipo de moto en la base de datos");

            logger.error("Error al filtrar el tipo de moto en la base de datos " + throwables.fillInStackTrace());

        }


    }


    /**
     *
     * Elimina todos los datos de la base de datos
     * @param event
     */
    @FXML
    public void eliminarBaseDatos(Event event) {

        try {

            // Si el boton pulsado en la alerta es cancelar, vuelve y no elimina la base de datos
            if (Alertas.mostrarConfirmación().get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {

                logger.trace("Se cancela la operación de eliminar todos los datos de la base de datos");

                return;

            } else {

                motoDAO.eliminarBaseDatos();
                Alertas.mostrarInformacion("BD Eliminada!", "Base de datos eliminada con éxito");
                cargarDatos();

                logger.trace("Se eliminan todos los datos de la base de datos");

            }

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error", "Error al eliminar la base de datos");

            logger.error("Error al eliminar todos los datos de la base de datos");

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
                logger.trace("Se intenta recuperar la última moto eliminada de la base de datos, pero no se había eliminado ninguna");
                return;
            }

            motoDAO.guardarMoto(Moto.motoRecuperar);
            Alertas.mostrarInformacion("Recuperada!", "Se ha recuperado la última moto eliminada: " + Moto.motoRecuperar.getMarca() + " " + Moto.motoRecuperar.getModelo());
            cargarDatos();

            logger.trace("Se recupera la última moto eliminada de la base de datos "
                                + Moto.motoRecuperar.getMatricula() + ", "
                                + Moto.motoRecuperar.getMarca() + ", "
                                + Moto.motoRecuperar.getModelo() + ", "
                                + Moto.motoRecuperar.getTipo()
            );

        } catch (SQLException throwables) {

            Alertas.mostrarError("Error" , "No se ha podido recuperar la última moto eliminada");

            logger.error("Error al recuperar la última moto eliminada de la base de datos " + throwables.fillInStackTrace());

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

        logger.trace("Se carga la moto del ListView en los textfields");

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

            logger.trace("Se cargan los datos de la base de datos y se limpian las cajas");


        } catch (SQLException throwables) {

            Alertas.mostrarError("Error Conexión", "Error al cargar los datos de la base de datos");

            logger.error("Error al cargar los datos de la base de datos " + throwables.fillInStackTrace());

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

        if (activar) {
            logger.trace("Se activa el modo edición");
        } else {
            logger.trace("Se desactiva el modo edición");
        }


    }



}
