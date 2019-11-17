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

    Database() {
    }

    public List<Category> getCategories(String query) {
        Object var3;
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                this.categories.add(new Category(resultSet.getInt(1), resultSet.getString(2)));
            }

            List var2 = this.categories;
            return var2;
        } catch (SQLException var17) {
            var17.printStackTrace();
            var3 = null;
        } finally {
            try {
                connection.close();
            } catch (SQLException var16) {
            }

            try {
                statement.close();
            } catch (SQLException var15) {
            }

        }

        return (List)var3;
    }

    public List<Todo> getTodos(String query) {
        Object var3;
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                this.todos.add(new Todo(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3)));
            }

            List var2 = this.todos;
            return var2;
        } catch (SQLException var17) {
            var17.printStackTrace();
            var3 = null;
        } finally {
            try {
                connection.close();
            } catch (SQLException var16) {
            }

            try {
                statement.close();
            } catch (SQLException var15) {
            }

        }

        return (List)var3;
    }

    public void execute(String query) {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException var15) {
            var15.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException var14) {
            }

            try {
                statement.close();
            } catch (SQLException var13) {
            }

        }

    }
}

