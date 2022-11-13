package com.warehouse.statemachine;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Processed {
    SendMessage someProcess(String text, long chat_id);
}
