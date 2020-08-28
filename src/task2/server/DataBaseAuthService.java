package task2.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseAuthService implements AuthService {
    private Connection connection;

    public DataBaseAuthService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Record findRecord(String login, String password) throws RuntimeException {
        /*language=SQL*/
        String sql = "SELECT id, name FROM CHAT_USERS where login = ? and password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, login);
            statement.setString(2, password);

            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.first()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                return new Record(id, name, login, password);
            }

        } catch (SQLException sqlException) {
            throw new RuntimeException("Не удалось прочитать запись о пользователе из БД!", sqlException);
        }

        return null;
    }
}
