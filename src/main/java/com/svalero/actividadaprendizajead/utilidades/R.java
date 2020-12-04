package com.svalero.actividadaprendizajead.utilidades;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class R {

    /**
     * Método que accede a la carpeta de interfaz en resources y devuelve la ubicación del archivo fxml que se quiere cargar
     * @param name
     * @return
     */
    public static URL getUI(String name){
        return Thread.currentThread().getContextClassLoader().getResource("interfaz" + File.separator+ name);
    }


    /**
     *
     * @param name -> El nombre de un fichero .properties
     * @return -> devuelve el que se le diga de la carpeta configurations
     */
    public static InputStream getProperties(String name){
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("configuration" + File.separator+ name);
    }

}