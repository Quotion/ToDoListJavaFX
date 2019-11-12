package sample;

import java.sql.*;

class Database {

    private static final String url = "jdbc:mysql://eu-cdbr-west-02.cleardb.net/heroku_18dfa3c9b0e58eb?user=b582953b092c11&password=322b157e&useUnicode=true&characterEncoding=UTF-8";

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

                                                   
    public ResultSet gerInfo(String query) {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException error) {
            error.printStackTrace();
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException connError) {
            }
            try {
                statement.close();
            } catch (SQLException statError) {
            }
            try {
                resultSet.close();
            } catch (SQLException resultError) {
            }
        }
    }

    public void execute(String query)   {
        try{
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException error){
            error.printStackTrace();
        } finally {
            try { connection.close();} catch (SQLException error) {}
            try { statement.close(); } catch (SQLException error) {}
        }
    }
}

