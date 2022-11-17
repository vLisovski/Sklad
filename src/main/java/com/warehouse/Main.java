package com.warehouse;

import com.warehouse.bot.BotInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new BotInitializer());
            System.out.println("bot started");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
