package com.svalero.actividadaprendizajead.modelo;

import com.svalero.actividadaprendizajead.clases.Usuario;
import com.svalero.actividadaprendizajead.utilidades.R;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class UsuariosDAO {

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
     * Comprueba si el usuario existe en la base de datos
     * Devuelve un boolean, true si existe, false sino
     * Recibe un objeto usuario con los datos introducidos en los textfields
     * @param usuario
     * @return
     * @throws SQLException
     */
    public boolean validarUsuario(Usuario usuario) throws SQLException {

        boolean resultado = false;
        String sql = "SELECT * FROM usuarios WHERE nombre = ? AND contraseña = ?";

        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setString(1, usuario.getNombreUsuario());
        sentencia.setString(2, usuario.getContrasenia());

        ResultSet rs = sentencia.executeQuery();

        while (rs.next()) {
            resultado = true;
        }



        return resultado;
    }


    public void registrarUsuario(Usuario usuario) throws SQLException {

        String nombreUsuario = usuario.getNombreUsuario();
        String contrasenia = usuario.getContrasenia();

        String sql = "INSERT INTO usuarios (nombre, contraseña) VALUES (?, ?)";

        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setString(1, nombreUsuario);
        sentencia.setString(2, contrasenia);

        sentencia.execute();

    }


}
