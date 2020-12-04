package com.svalero.actividadaprendizajead;

import com.svalero.actividadaprendizajead.utilidades.R;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MotoDAO {

    private Connection conexion;


    /**
     * Método para conectar con la base de datos a través del fichero properties
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void conectar() throws IOException, ClassNotFoundException, SQLException {

        Properties configuracion = new Properties();
        configuracion.load(R.getProperties("base_de_datos.properties"));

        String host = configuracion.getProperty("host");
        String port = configuracion.getProperty("port");
        String namedb = configuracion.getProperty("namedb");
        String user = configuracion.getProperty("user");
        String password = configuracion.getProperty("password");

        Class.forName("com.mysql.cj.jdbc.Driver");
        conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + namedb + "?serverTimezone=UTC", user, password);
    }


    /**
     * Método para desconectar de la base de datos
     * @throws SQLException
     */
    public void desconectar () throws SQLException {

        conexion.close();

    }

}
