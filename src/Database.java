import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    String currentDB;
    Connection connection;
    public Database(String dbName){
        currentDB = dbName;
    }

    public int countRows(String table){
        int count;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:"+currentDB);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select count(*) from "+table);
            count = rs.getInt(1);
            return count;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return 0;
    }

    public void Query(String sql){
        connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:"+currentDB);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void Query(List<String> sql){
        connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:"+currentDB);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            for(String s : sql) statement.executeUpdate(s);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }


    public List<String> singleQuery(String sql){
        List<String> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+currentDB);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    public int IDQuery(String sql){
        int res = 0;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+currentDB);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                res = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return res;
    }



    public static void main(String[] args) throws SQLException {
        Database db = new Database("data/music.db");
        //db.insert("insert into person values(3,'john')");
        System.out.println(db.singleQuery("select name from person;"));
        System.out.println(db.countRows("person"));
        int currentNumberOfUsers = 1;
        System.out.println(db.singleQuery("select name from person where name = 'johnn'").size());
        String username = "John";
        String password = "jjj";
        System.out.println("insert into users values(" + (currentNumberOfUsers + 1) + ",'"+username+"','"+password+"','"+username+".db',false)");
    }

}
