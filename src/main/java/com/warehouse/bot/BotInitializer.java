package com.warehouse.bot;

import com.warehouse.statemachine.Router;
import com.warehouse.statemachine.StateMap;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BotInitializer extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "SkladJava2022Bot";
    }

    @Override
    public String getBotToken() {
        return "5791899974:AAENpG24JEL84irr9qISrA6xoKq4yg0VIGU";
    }

    @Override
    public void onUpdateReceived(Update update) {


            SendMessage message;

            try {
                message = Router.getInstance().route(update);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
               execute(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }
}
