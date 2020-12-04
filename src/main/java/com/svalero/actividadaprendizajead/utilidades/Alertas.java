package com.svalero.actividadaprendizajead.utilidades;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

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


    /**
     * Muestra un mensaje de confirmación, en este caso no paso titulo y mensaje por parámetros ya que siempre
     * va a ser una pregunta al usuario de si está seguro de ello
     *
     * Devuelve lo que el usuario responde al saltarle la alerta
     * @return respuesta
     */
    public static Optional<ButtonType> mostrarConfirmación() {

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setContentText("¿Estás seguro?");
        Optional<ButtonType> respuesta = alerta.showAndWait();

        return respuesta;

    }


    /**
     * Muestra un mensaje de información en una alerta
     * @param titulo
     * @param mensajeInformativo
     */
    public static void mostrarInformacion(String titulo, String mensajeInformativo) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(mensajeInformativo);
        alerta.show();

    }

}
