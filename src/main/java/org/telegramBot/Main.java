package org.telegramBot;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

public class Main extends TelegramLongPollingBot {

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(new Main());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        String userMessageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        System.out.println(sendMessage);

        if (userMessageText.equals("/start")){
            sendMessage.setText("Добро пожаловать напишите город В котором хотите узнать погоду");
            SendToDb.addToBase(Integer.parseInt(sendMessage.getChatId()), userMessageText, "Добро пожаловать напишите город В котором хотите узнать погоду");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }else if(userMessageText.equals("Последняя запись")){
            String response = SendToDb.getLastEntry();
            sendMessage.setText(response);
            SendToDb.addToBase(Integer.parseInt(sendMessage.getChatId()), userMessageText, response);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }else{
            String weather = Weather.getWeather(userMessageText);
            sendMessage.setText(weather);
            SendToDb.addToBase(Integer.parseInt(sendMessage.getChatId()), userMessageText, weather);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public String getBotUsername() {
        return "GisMediaJava_Bot";
    }

    @Override
    public String getBotToken() {

        return "6666129700:AAHmDigLJzo_na0BHuQlY7G03kOLrYTqLlU";
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }


    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}