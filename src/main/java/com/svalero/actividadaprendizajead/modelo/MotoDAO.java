package com.svalero.actividadaprendizajead.modelo;

import com.svalero.actividadaprendizajead.clases.Moto;
import com.svalero.actividadaprendizajead.utilidades.R;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
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
     * Devuelve una lista con las motos de la base de datos
     * @return lista de motos
     * @throws SQLException
     */
    public ArrayList <Moto> getListaMotos() throws SQLException {

        ArrayList <Moto> listaMotos = new ArrayList<>();

        String sql  = "SELECT * FROM motos";

        PreparedStatement sentencia = conexion.prepareStatement(sql);
        ResultSet resultado = sentencia.executeQuery();

        while (resultado.next()) {

            Moto moto = new Moto();
            /* Se pone 1 mas ya que el primer campo de la base de datos es el ID */
            moto.setMatricula(resultado.getString(2));
            moto.setMarca(resultado.getString(3));
            moto.setModelo(resultado.getString(4));
            moto.setTipo(resultado.getString(5));

            listaMotos.add(moto);

        }

        return listaMotos;
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


    /**
     * Método que elimina la moto de la base de datos
     * @param moto
     * @throws SQLException
     */
    public void eliminarMoto(Moto moto) throws SQLException {

        String sql = "DELETE FROM motos WHERE matricula = ? AND marca = ? AND modelo = ? AND tipo = ?";

        PreparedStatement sentencia = conexion.prepareStatement(sql);

        sentencia.setString(1, moto.getMatricula());
        sentencia.setString(2, moto.getMarca());
        sentencia.setString(3, moto.getModelo());
        sentencia.setString(4, moto.getTipo());

        sentencia.executeUpdate();

    }


    public void modificarMoto(Moto motoAntigua, Moto motoNueva) throws SQLException {

        String sql = "UPDATE motos SET matricula = ?, marca = ?, modelo = ?, tipo = ? WHERE matricula = ?";
        // TODO establecer matricula como pk también en bd

        PreparedStatement sentencia = conexion.prepareStatement(sql);

        // Datos moto nueva
        sentencia.setString(1, motoNueva.getMatricula());
        sentencia.setString(2, motoNueva.getMarca());
        sentencia.setString(3, motoNueva.getModelo());
        sentencia.setString(4, motoNueva.getTipo());

        // Datos moto antigüa
        sentencia.setString(5, motoAntigua.getMatricula());

        sentencia.executeUpdate();

    }



    public void eliminarBaseDatos() throws SQLException {

        // Usar truncate para que se reinicide el id auto incremental
        String sql = "TRUNCATE motos";

        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.executeUpdate();

    }

}
