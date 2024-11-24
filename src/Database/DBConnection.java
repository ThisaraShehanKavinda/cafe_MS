/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

/**
 *
 * @author PC
 */
public class DBConnection {
 private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/cafe_ms";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static Connection conn ;

    // get database connection
    public static Connection getConnection() {
        
        try {
            if (conn == null) {
                Class.forName(DRIVER_CLASS_NAME);
                conn = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            }

            return conn;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // close database connection
    public static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
   

}
