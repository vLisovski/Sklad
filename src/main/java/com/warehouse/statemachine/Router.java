package com.warehouse.statemachine;

import com.warehouse.entities.ProductPosition;
import com.warehouse.sqlclasses.UserService;
import com.warehouse.strings.Strings;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class Router {
    private Router() throws Exception {
        userService = new UserService();
    }

    private static Router instance;
    private UserService userService;

    public static Router getInstance() throws Exception {
        if (instance == null) {
            instance = new Router();

        }
        return instance;
    }

    private SendMessage routeStart(Update update) throws Exception {
        String chat_id = "";
        StateMap.getInstance().putState(update.getMessage().getChatId(), States.MAIN_MENU.toString());
        chat_id = update.getMessage().getChatId().toString();

        return PageMap.getInstance()
                .getPageByKey(StateMap.getInstance().getState(Long.parseLong(chat_id)).toString())
                .someProcess(update.getMessage().getText(), update.getMessage().getChatId());
    }

    private SendMessage routeOrderCollected(Update update) throws Exception {
        int id = Integer.parseInt(update.getMessage().getText());

        userService.deleteOrderById(id);

        String retrieveText = "Order with " + id + " identification as collected and removed from the list of current.\n\nChoose action:";

        StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());

        return PageMap.getInstance()
                .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                .someProcess(retrieveText, update.getMessage().getChatId());


    }

    private SendMessage routeAddToWarehouse(Update update) throws Exception {
        String receivedText = update.getMessage().getText();
        String productName = receivedText.replaceAll("\\d", "").trim();
        String countAsString = receivedText.replaceAll("\\D", "").trim();
        int count = Integer.parseInt(countAsString);

        if (!(userService.findProductByName(productName))) {

            userService.insertProduct(productName, count);

            String retrieveText = "Product added " + productName + " " + count + " completed.\n\nChoose action:";
            StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());
            return PageMap.getInstance()
                    .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                    .someProcess(retrieveText, update.getMessage().getChatId());

        } else {

            int currentCount = userService.selectProductCount(productName);

            userService.updateProduct(productName, currentCount + count);
            String retrieveText = "Product added " + productName + " " + count + " completed.\n\nChoose action:";
            StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());

            return PageMap.getInstance()
                    .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                    .someProcess(retrieveText, update.getMessage().getChatId());

        }
    }

    private SendMessage routeRemoveFromWarehouse(Update update) throws Exception {
        String receivedText = update.getMessage().getText();
        String productName = receivedText.replaceAll("\\d", "").trim();
        String countAsString = receivedText.replaceAll("\\D", "").trim();

        int count = Integer.parseInt(countAsString);
        int currenCount = userService.selectProductCount(productName);
        String retrieveText;

        if (!(count < currenCount)) {
            userService.deleteProduct(productName);
            retrieveText = "Reducing the quantity of goods " + productName + " by " + count + " units completed. Product has deleted.\n\nChoose action:";
        } else {
            userService.updateProduct(productName, currenCount - count);
            retrieveText = "Reducing the quantity of goods " + productName + " by " + count + " units completed.\n\nChoose action";
        }

        StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());

        return PageMap.getInstance()
                .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                .someProcess(retrieveText, update.getMessage().getChatId());
    }

    private SendMessage routeSearchProduct(Update update) throws Exception {
        String receivedText = update.getMessage().getText();
        String productName = receivedText.replaceAll("\\d", "").trim();
        String idAsString = receivedText.replaceAll("\\D", "").trim();
        List<ProductPosition> products = null;

        if (productName.length() != 0) {
            products = userService.selectProductsByName(productName);
        } else if (idAsString.length() != 0) {
            int id = Integer.parseInt(idAsString);
            products = userService.selectProductsById(id);
        }

        StringBuilder retrieveText = new StringBuilder();

        if (products != null) {
            for (int i = 0; i < products.size(); i++) {
                String name = products.get(i).getName();
                int count = products.get(i).getCount();
                int pId = products.get(i).getId();

                retrieveText.append(pId)
                        .append(" ")
                        .append(name)
                        .append(" ")
                        .append(count)
                        .append("\n");
            }
        }
        retrieveText.append("\nChoose action:");
        StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());

        return PageMap.getInstance()
                .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                .someProcess(retrieveText.toString(), update.getMessage().getChatId());
    }

    public SendMessage route(Update update) throws Exception {

        String chat_id = "";

        if (!update.hasCallbackQuery()) {

            if (update.getMessage().getText().equals("/start")) {

                return routeStart(update);
            } else if (StateMap.getInstance().getState(update.getMessage().getChatId()).toString()
                    .equals(States.ORDER_COLLECTED.toString())) {

                return routeOrderCollected(update);
            } else if (StateMap.getInstance().getState(update.getMessage().getChatId()).toString()
                    .equals(States.ADD_TO_WAREHOUSE.toString())) {

                return routeAddToWarehouse(update);
            } else if (StateMap.getInstance().getState(update.getMessage().getChatId()).toString()
                    .equals(States.REMOVE_FROM_WAREHOUSE.toString())) {

                return routeRemoveFromWarehouse(update);
            } else if (StateMap.getInstance().getState(update.getMessage().getChatId()).toString()
                    .equals(States.SEARCH_PRODUCT.toString())) {

                return routeSearchProduct(update);
            }
        } else if (update.hasCallbackQuery()) {

            StateMap.getInstance().replaceState(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getData());
            chat_id = update.getCallbackQuery().getMessage().getChatId().toString();

            if (StateMap.getInstance().getState(update.getCallbackQuery().getMessage().getChatId()).toString()
                    .equals(States.MAIN_MENU.toString())) {

                return PageMap.getInstance()
                        .getPageByKey(StateMap.getInstance().getState(Long.parseLong(chat_id)).toString())
                        .someProcess(Strings.MAIN_MENU_TEXT,
                                update.getCallbackQuery().getMessage().getChatId());
            } else {

                return PageMap.getInstance()
                        .getPageByKey(StateMap.getInstance().getState(Long.parseLong(chat_id)).toString())
                        .someProcess("",
                                update.getCallbackQuery().getMessage().getChatId());
            }
        }

        return new SendMessage(chat_id, "");
    }

}
