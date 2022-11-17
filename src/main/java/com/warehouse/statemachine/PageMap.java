package com.warehouse.statemachine;

import com.warehouse.entities.Order;
import com.warehouse.sqlclasses.DbManager;
import com.warehouse.strings.Strings;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageMap {
    private Map<String, Processed> processes;

    private PageMap() {
        this.processes = new HashMap<>();
        this.processes.put(States.MAIN_MENU.toString(), this::mainMenuPage);
        this.processes.put(States.CURRENT_ORDERS.toString(), this::currentOrdersMenu);
        this.processes.put(States.ORDER_COLLECTED.toString(), this::orderCollected);
        this.processes.put(States.ADD_TO_WAREHOUSE.toString(), this::addToWarehouse);
        this.processes.put(States.REMOVE_FROM_WAREHOUSE.toString(), this::removeFromWarehouse);
        this.processes.put(States.SEARCH_PRODUCT.toString(), this::searchProduct);
        this.processes.put(States.TASK_LIST.toString(), this::tasksList);
    }

    private static PageMap instance;

    public static PageMap getInstance() throws Exception {
        if (instance == null) {
            instance = new PageMap();
        }
        return instance;
    }

    public Processed getPageByKey(String key) {
        return processes.get(key);
    }

    private SendMessage mainMenuPage(String text, long chat_id) {
        SendMessage message = new SendMessage();

        message.setText(text);
        message.setChatId(chat_id);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(Strings.MAIN_MENU_BUTTON_1);
        button.setCallbackData("CURRENT_ORDERS");
        rowInline.add(button);
        rowsInline.add(rowInline);

        rowInline = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText(Strings.MAIN_MENU_BUTTON_2);
        button.setCallbackData("ADD_TO_WAREHOUSE");
        rowInline.add(button);
        rowsInline.add(rowInline);

        rowInline = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText(Strings.MAIN_MENU_BUTTON_3);
        button.setCallbackData("REMOVE_FROM_WAREHOUSE");
        rowInline.add(button);
        rowsInline.add(rowInline);

        rowInline = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText(Strings.MAIN_MENU_BUTTON_4);
        button.setCallbackData("SEARCH_PRODUCT");
        rowInline.add(button);
        rowsInline.add(rowInline);

        rowInline = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText(Strings.MAIN_MENU_BUTTON_5);
        button.setCallbackData("TASK_LIST");
        rowInline.add(button);
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        message.setReplyMarkup(markupInline);

        return message;
    }

    private SendMessage currentOrdersMenu(String text, long chat_id) throws SQLException {
        SendMessage message = new SendMessage();

        List<Order> orders = DbManager.getInstance().getSqlMethods().selectOrders();

        StringBuilder output = new StringBuilder();
        output.append("Current orders:\n");
        for (int i = 0; i < orders.size(); i++) {
            String description = orders.get(i).getDescription();
            int id = orders.get(i).getId();

            output.append(id)
                    .append(" ")
                    .append(description)
                    .append("\n");
        }

        message.setText(output.toString());
        message.setChatId(chat_id);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(Strings.CURRENT_ORDERS_BUTTON_1);
        button.setCallbackData(States.ORDER_COLLECTED.toString());
        rowInline.add(button);
        rowsInline.add(rowInline);

        rowInline = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText(Strings.CURRENT_ORDERS_BUTTON_2);
        button.setCallbackData(States.MAIN_MENU.toString());
        rowInline.add(button);
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        message.setReplyMarkup(markupInline);

        return message;

    }

    /**
     * text is not used by this method, but must be passed
     **/
    private SendMessage orderCollected(String text, long chat_id) {
        SendMessage message = new SendMessage();
        message.setText(Strings.ORDER_COLLECTED_TEXT);
        message.setChatId(chat_id);
        return message;
    }

    /**
     * text is not used by this method, but must be passed
     **/
    private SendMessage addToWarehouse(String text, long chat_id) {
        SendMessage message = new SendMessage();
        message.setText(Strings.ADD_TO_WAREHOUSE_TEXT);
        message.setChatId(chat_id);
        return message;
    }

    /**
     * text is not used by this method, but must be passed
     **/
    private SendMessage removeFromWarehouse(String text, long chat_id) {
        SendMessage message = new SendMessage();
        message.setText(Strings.REMOVE_FROM_WAREHOUSE_TEXT);
        message.setChatId(chat_id);
        return message;
    }

    /**
     * text is not used by this method, but must be passed
     **/
    private SendMessage searchProduct(String text, long chat_id) {
        SendMessage message = new SendMessage();
        message.setText(Strings.SEARCH_PRODUCT);
        message.setChatId(chat_id);
        return message;
    }

    private SendMessage tasksList(String text, long chat_id) throws SQLException {
        SendMessage message = new SendMessage();

        List<String> tasks = DbManager.getInstance().getSqlMethods().selectTasks();
        String output="";
        for (int i = 0; i < tasks.size(); i++) {
            output = output + "\n" + tasks.get(i);
        }
        message.setText(output);
        message.setChatId(chat_id);
        return message;
    }

}
