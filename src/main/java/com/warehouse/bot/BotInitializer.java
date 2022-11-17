package com.warehouse.bot;

import com.warehouse.statemachine.Router;
import com.warehouse.statemachine.StateMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BotInitializer extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(BotInitializer.class);

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

        if (update.hasCallbackQuery()) {
            try {
                logger.info(String.format("INPUT CALLBACK FROM chat_id %d WITH STATE %s: %s", update.getCallbackQuery().getMessage().getChatId(),
                        StateMap.getInstance().getState(update.getCallbackQuery().getMessage().getChatId()),
                        update.getCallbackQuery().getData()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                logger.info(String.format("INPUT MESSAGE FROM chat_id %d WITH STATE %s: %s", update.getMessage().getChatId(),StateMap.getInstance().getState(update.getMessage().getChatId()),
                        update.getMessage().getText()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        SendMessage message;

        try {
            message = Router.getInstance().route(update);
            logger.info(String.format("OUTPUT TEXT FOR chat_id %s: %s", message.getChatId(),
                    message.getText()));
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
