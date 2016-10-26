/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author intec
 */
public class DataSource {

    Connection connection = null;
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DataSource.class);
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "123";

    public DataSource() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException | ClassNotFoundException ex) {
            logger.error("Error connecting to database: " + ex.getMessage());
        }
        if (connection != null) {
            logger.info("Successful Connection");
        }
    }

    public ArrayList getAllNamedExpedient() {
        ArrayList allExpedient = new ArrayList();
        String query = "Select * FROM expedient";
        try {
            PreparedStatement statementQuery = connection.prepareStatement(query);
            ResultSet rs = statementQuery.executeQuery();
            while (rs.next()) {
                allExpedient.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            logger.error("Error retrieving named expedients: " + ex.getMessage());
        }
        return allExpedient;
    }

    public void createExpedient(String name, String description) {
        Statement sta = null;
        
        

    }

}
