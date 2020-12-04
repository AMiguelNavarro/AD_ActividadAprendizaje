package com.svalero.actividadaprendizajead.utilidades;

import javafx.scene.control.Alert;

public class Alertas {


    /**
     * Muestra una alerta con mensaje de error
     * @param tituloError Titulo del error
     * @param mensajeError Mensaje del error
     */
    public static void mostrarError(String tituloError ,String mensajeError) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(tituloError);
        alerta.setContentText(mensajeError);
        alerta.show();

    }

}
