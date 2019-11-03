package sample;

import java.sql.*;

public class database {

    private static final String url = "mysql://e@eu-cdbr-west-02.cleardb.net:3306/heroku_18dfa3c9b0e58eb";
    private static final String user = "b582953b092c11";
    private static final String password = "322b157e";

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public ResultSet gerInfo(String query){
        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException error) {
            error.printStackTrace();
            return null;
        } finally {
            //close connection ,stmt and resultset here
            try { connection.close(); } catch(SQLException connError) {}
            try { statement.close(); } catch(SQLException statError) {}
            try { resultSet.close(); } catch(SQLException resultError) {}
        }
    }
}
