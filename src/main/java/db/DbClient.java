package db;

import configs.Config;

import java.sql.*;

// ПУ-9: SQL-запросы к базе стенда через JDBC.
// Каждый метод открывает соединение, выполняет один запрос и закрывает соединение (try-with-resources).
// Значения подставляются через «?» (PreparedStatement), а не склейкой строк — так нет SQL-инъекций.
public class DbClient {

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                Config.getProperty("db.url"),
                Config.getProperty("db.user"),
                Config.getProperty("db.password"));
    }

    // Количество строк, которые вернул запрос
    public static int countRows(String sql, String uuid) {
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                int count = 0;
                while (resultSet.next()) {
                    count++;
                }
                return count;
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL failed: " + sql, e);
        }
    }

    // Значение колонки column из первой строки результата (null, если строк нет)
    public static String getValue(String sql, String uuid, String column) {
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(column);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL failed: " + sql, e);
        }
    }
}
