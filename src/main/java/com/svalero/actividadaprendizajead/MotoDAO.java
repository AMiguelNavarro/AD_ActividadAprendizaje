package com.svalero.actividadaprendizajead;

import com.svalero.actividadaprendizajead.beans.Moto;
import com.svalero.actividadaprendizajead.utilidades.R;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
        configuracion.load(R.getProperties("bd.properties"));

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


    /**
     * Guarda la moto en la base de datos
     * @param moto
     * @throws SQLException
     */
    public void guardarMoto(Moto moto) throws SQLException {

        String sql = "INSERT INTO motos (matricula, marca, modelo, tipo) VALUES (?,?,?,?)";

        PreparedStatement sentencia = conexion.prepareStatement(sql);

        sentencia.setString(1, moto.getMatricula());
        sentencia.setString(2, moto.getMarca());
        sentencia.setString(3, moto.getModelo());
        sentencia.setString(4, moto.getTipo());

        sentencia.executeUpdate();
    }

}
