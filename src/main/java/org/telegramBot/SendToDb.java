package org.telegramBot;

import java.sql.*;

public class SendToDb {

    static final String url = "jdbc:postgresql://0.0.0.0:5438/postgres";
    static final String username = "postgres";
    static final String password = "12345678";
    public static void addToBase(int idChat, String textUser, String answerToUser) {

        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                System.out.println("Соединение установлено!");

                String createTableSQL = "CREATE TABLE IF NOT EXISTS user_messages (" +
                        "idChat INT," +
                        "textUser VARCHAR(255)," +
                        "answerToUser VARCHAR(255)," +
                        "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")";
                connection.createStatement().executeUpdate(createTableSQL);

                String insertSQL = "INSERT INTO user_messages (idChat, textUser, answerToUser) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setInt(1, idChat);
                preparedStatement.setString(2, textUser);
                preparedStatement.setString(3, answerToUser);
                preparedStatement.executeUpdate();

                System.out.println("Данные успешно добавлены в таблицу.");

            } else {
                System.out.println("Не удалось установить соединение с базой данных.");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка SQL: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер не найден");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("Соединение закрыто.");
                }
            } catch (SQLException e) {
                System.out.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }

    public static String  getLastEntry() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String lastEntryDetails = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);

            System.out.println("Connection established.");

            String selectSQL = "SELECT * FROM user_messages ORDER BY time DESC LIMIT 1";
            preparedStatement = connection.prepareStatement(selectSQL);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int idChat = resultSet.getInt("idChat");
                String textUser = resultSet.getString("textUser");
                String answerToUser = resultSet.getString("answerToUser");
                String time = resultSet.getString("time");

                lastEntryDetails = "Last entry:\n" +
                        "idChat: " + idChat + "\n" +
                        "textUser: " + textUser + "\n" +
                        "answerToUser: " + answerToUser + "\n" +
                        "time: " + time;

                System.out.println("Last entry:");
                System.out.println("idChat: " + idChat);
                System.out.println("textUser: " + textUser);
                System.out.println("answerToUser: " + answerToUser);
                System.out.println("time: " + time);
            } else {
                System.out.println("No entries found in the database.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                    System.out.println("Connection closed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lastEntryDetails;
    }

}
