package task4;

import java.sql.*;

public class ClientRecord {
    private Connection connection;

    public ClientRecord() {

        /*подкление драйвера*/
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Connection succesfull!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver not found");
        }

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/client",
                    "root",
                    "123456");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Driver Registration error");
        }
    }

    public AuthService.Record getRecord(String login) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    String.format("SELECT * FROM client.record WHERE login = '%s';", login));
            if (resultSet.next()) {
                AuthService.Record record = new AuthService.Record(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("login"),
                        resultSet.getString("password")
                );

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return record;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Statement error");
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean commit(AuthService.Record record, String newName) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE client.record SET `name` = ? WHERE (`id` = ?);"
            );
            statement.setString(1, newName);
            statement.setString(2, String.valueOf(record.getId()));
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            throw new RuntimeException("Statement error");
        }

        boolean isItOk = isCommit(record.getId(), newName);

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isItOk;
    }

    private boolean isCommit(long id, String newName) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    String.format("SELECT name FROM client.record WHERE id = '%s';", id));
            if (resultSet.next()) return resultSet.getString("name").equals(newName);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Statement error");
        }
        return false;
    }


}
