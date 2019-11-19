package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class Database {
    private static final String url = "jdbc:mysql://eu-cdbr-west-02.cleardb.net/heroku_18dfa3c9b0e58eb?user=b582953b092c11&password=322b157e&useUnicode=true&characterEncoding=UTF-8";
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private List<Category> categories = new ArrayList();
    private List<Todo> todos = new ArrayList();

    Database() {}

    public Object getInfo(String query){
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Object info = resultSet.toString();
            return info;
        } catch (SQLException error) {
            error.printStackTrace();
            return null;
        } finally {
            try { connection.close(); } catch (SQLException errorConnection) {}
            try { statement.close(); } catch (SQLException errorStatement) {}
            try { resultSet.close(); } catch (SQLException errorResult) {}
        }
    }

    public List<Category> getCategories(String query) {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                this.categories.add(new Category(resultSet.getInt(1), resultSet.getString(2)));
            }
            return categories;
        } catch (SQLException error) {
            error.printStackTrace();
            return null;
        } finally {
            try { connection.close(); } catch (SQLException errorConnection) {}
            try { statement.close(); } catch (SQLException errorStatement) {}
            try { resultSet.close(); } catch (SQLException errorResult) {}
        }
    }

    public List<Todo> getTodos(String query) {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                this.todos.add(new Todo(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3)));
            }
            return todos;
        } catch (SQLException error) {
            error.printStackTrace();
            return null;
        } finally {
            try { connection.close(); } catch (SQLException errorConnection) {}
            try { statement.close(); } catch (SQLException errorStatement) {}
            try { resultSet.close(); } catch (SQLException errorResult) {}
        }
    }

    public void execute(String query) {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException error) {
            error.printStackTrace();
        } finally {
            try { connection.close(); } catch (SQLException errorConnection) {}
            try { statement.close(); } catch (SQLException errorStatement) {}
        }
    }
}

