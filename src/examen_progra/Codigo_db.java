/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examen_progra;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Enrique
 */
public class Codigo_db {

    static Connection conn;
    static PreparedStatement pst;
    static Statement st;
    static ResultSet rs;
    static String query;
    static String jdbc = "jdbc:postgresql://localhost:5432/examen_progra";
    String tipo = "";

    boolean Agregar_Producto(String nombre_product, int precio, String tipo) {
        boolean agregado = false;
        try {
            conn = DriverManager.getConnection(jdbc, "postgres", "Admin");
            query = "INSERT INTO productos(nombre,precio,tipo) VALUES (?,?,?)";
            pst = conn.prepareStatement(query);
            pst.setString(1, nombre_product);
            pst.setInt(2, precio);
            pst.setString(3, tipo);
            pst.executeUpdate();
            conn.close();
            agregado = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return agregado;
    }

    public ArrayList<String[]> Buscar_Productos(int edad) {
        if (edad <= 12) {
            tipo = "niño";
        } else if (edad >= 13 && edad <= 17) {
            tipo = "joven";
        } else {
            tipo = "adulto";
        }
        ArrayList<String[]> lista = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(jdbc, "postgres", "Admin");
            query = "SELECT id_producto,nombre FROM productos WHERE tipo = '" + tipo + "';";
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                String[] producto = new String[]{
                    String.valueOf(rs.getInt("id_producto")),
                    rs.getString("nombre")
                };
                lista.add(producto);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public boolean Agregar_Compra(String nombre_persona, int edad, String genero, int id_producto, String fecha) {
        boolean agregado;
        java.util.Date date1;
        Date date2 = null;
        try {
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            date1 = format.parse(fecha);
            date2 = new java.sql.Date(date1.getTime());

        } catch (ParseException ex) {
            ex.printStackTrace();
            date1 = null;
        }
        try {
            conn = DriverManager.getConnection(jdbc, "postgres", "Admin");
            query = "INSERT INTO compras(nombre_usuario,edad,genero,productofk,fecha)"
                    + " VALUES (?,?,?,?,?)";
            pst = conn.prepareStatement(query);
            pst.setString(1, nombre_persona);
            pst.setInt(2, edad);
            if (genero.equals("Masculino")) {
                pst.setBoolean(3, false);
            } else {
                pst.setBoolean(3, true);
            }

            pst.setInt(4, id_producto);
            pst.setDate(5, date2);
            pst.executeUpdate();
            conn.close();
            agregado = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            agregado = false;
        }
        return agregado;
    }

    ArrayList<String[]> Buscar_Compras() {
        ArrayList<String[]> lista = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(jdbc, "postgres", "Admin");
            query = "SELECT comp.nombre_usuario,comp.genero,comp.edad,prod.nombre FROM compras AS comp "
                    + "INNER JOIN productos AS prod ON comp.productofk = prod.id_producto WHERE comp.genero = true AND comp.edad >= 12;";
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                String[] producto = new String[]{
                    rs.getString("nombre_usuario"),
                    String.valueOf(rs.getBoolean("genero")),
                    String.valueOf(rs.getInt("edad")),
                    rs.getString("nombre")
                };
                lista.add(producto);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    ArrayList<String[]> Buscar_Compras_Tipo(String tipo_txt) {
        ArrayList<String[]> lista = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(jdbc, "postgres", "Admin");
            query = "SELECT prod.nombre,prod.precio,prod.tipo FROM compras AS comp "
                    + "INNER JOIN productos AS prod ON comp.productofk = prod.id_producto WHERE prod.tipo = '" + tipo_txt + "';";
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                String[] producto = new String[]{
                    rs.getString("nombre"),
                    String.valueOf(rs.getInt("precio")),
                    rs.getString("tipo")
                };
                lista.add(producto);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public ArrayList<String> Buscar_Compras_Fecha(Date date2, Date date4) {
        ArrayList<String> lista = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(jdbc, "postgres", "Admin");
            query = "SELECT prod.nombre FROM compras AS comp \n"
                    + "INNER JOIN productos AS prod ON comp.productofk = prod.id_producto WHERE comp.fecha >= '" + date2 + "' AND comp.fecha <= '" + date4 + "';";
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }
}
