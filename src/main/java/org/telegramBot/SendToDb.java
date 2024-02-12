package org.telegramBot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class SendToDb {
    public static void addToBase(int idChat, String textUser, String answerToUser) {
        // Задаем параметры подключения к базе данных
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "username";
        String password = "password";

        // Объект для подключения к базе данных
        Connection connection = null;

        try {
            // Регистрируем драйвер JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Устанавливаем соединение с базой данных
            connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                System.out.println("Соединение установлено!");

                String createTableSQL = "CREATE TABLE IF NOT EXISTS user_messages (" +
                        "idUser INT," +
                        "idChat INT," +
                        "textUser VARCHAR(255)," +
                        "answerToUser VARCHAR(255)," +
                        "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")";
                connection.createStatement().executeUpdate(createTableSQL);

                // Вставка данных в таблицу
                String insertSQL = "INSERT INTO user_messages (idChat, textUser, answerToUser) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setInt(1, idChat); // Пример значения для idChat
                preparedStatement.setString(2, textUser); // Пример текста от пользователя
                preparedStatement.setString(3, answerToUser); // Пример ответа пользователю
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
            // Всегда закрываем соединение в блоке finally
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
}
