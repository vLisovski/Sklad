package com.warehouse.statemachine;

import com.warehouse.entities.ProductPosition;
import com.warehouse.sqlclasses.DbManager;
import com.warehouse.sqlclasses.UserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class Router {
    private Router(){};
    private static Router instance;

    public static Router getInstance() throws Exception {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }

    public SendMessage route(Update update) throws Exception {

        String chat_id = "";

        if (!update.hasCallbackQuery()) {
            if (update.getMessage().getText().equals("/start")) {

                StateMap.getInstance().putState(update.getMessage().getChatId(), States.MAIN_MENU.toString());
                chat_id = update.getMessage().getChatId().toString();

                return PageMap.getInstance()
                        .getPageByKey(StateMap.getInstance().getState(Long.parseLong(chat_id)).toString())
                        .someProcess(update.getMessage().getText(), update.getMessage().getChatId());

            } else if (StateMap.getInstance().getState(update.getMessage().getChatId()).toString()
                    .equals(States.ORDER_COLLECTED.toString())) { //����� ������

                int id = Integer.parseInt(update.getMessage().getText());

                DbManager.getInstance().getSqlMethods().deleteOrderById(id);

                System.out.println("����� � id " + id + "������� ��� ��������� � ������ �� ������ �������.");
                String retreiveText = "����� � id " + id + "������� ��� ��������� � ������ �� ������ �������.\n\n������� ��������:";

                StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());

                return PageMap.getInstance()
                        .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                        .someProcess(retreiveText, update.getMessage().getChatId());
            } else if (StateMap.getInstance().getState(update.getMessage().getChatId()).toString()
                    .equals(States.ADD_TO_WAREHOUSE.toString())) { //�������� �� �����

                String receivedText = update.getMessage().getText();
                String productName = receivedText.replaceAll("\\d", "").trim();
                String countAsString = receivedText.replaceAll("\\D", "").trim();
                int count = Integer.parseInt(countAsString);

                if (DbManager.getInstance().getSqlMethods().findProductByName(productName)) {
                    DbManager.getInstance().getSqlMethods().insertProduct(productName, count);
                } else {
                    DbManager.getInstance().getSqlMethods().updateProduct(productName, count);
                }

                System.out.println("���������� ������ " + productName + " " + count + " ���������.");
                String retreiveText = "���������� ������ " + productName + " " + count + " ���������.\n\n������� ��������:";

                StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());

                return PageMap.getInstance()
                        .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                        .someProcess(retreiveText, update.getMessage().getChatId());
            } else if (StateMap.getInstance().getState(update.getMessage().getChatId()).toString()
                    .equals(States.REMOVE_FROM_WAREHOUSE.toString())) { //������ �� ������

                String receivedText = update.getMessage().getText();
                String productName = receivedText.replaceAll("\\d", "").trim();
                String countAsString = receivedText.replaceAll("\\D", "").trim();
                int count = Integer.parseInt(countAsString);
                int currenCount = DbManager.getInstance().getSqlMethods().selectProductCount(productName);
                String retreiveText;

                if (!(currenCount < count)) {
                    DbManager.getInstance().getSqlMethods().deleteProduct(productName);
                    System.out.println("�������� ������ " + productName + " " + count + " ���������.");
                    retreiveText = "�������� ������ " + productName + " " + count + " ���������.\n\n������� ��������:";
                } else {
                    DbManager.getInstance().getSqlMethods().updateProduct(productName, currenCount - count);
                    System.out.println("���������� ���������� ������ " + productName + " �� " + (currenCount - count) + " ���������.");
                    retreiveText = "���������� ���������� ������ " + productName + " �� " + (currenCount - count) + " ���������.\n\n������� ��������:";
                }

                StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());

                return PageMap.getInstance()
                        .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                        .someProcess(retreiveText, update.getMessage().getChatId());
            } else if (StateMap.getInstance().getState(update.getMessage().getChatId()).toString()
                    .equals(States.SEARCH_PRODUCT.toString())) {//����� �����

                String receivedText = update.getMessage().getText();
                String productName = receivedText.replaceAll("\\d", "").trim();
                String idAsString = receivedText.replaceAll("\\D", "").trim();
                int id = Integer.parseInt(idAsString);
                List<ProductPosition> products = null;

                if (productName.length() != 0) {
                    products = DbManager.getInstance().getSqlMethods().selectProductsByName(productName);
                    System.out.println("����� �� ����� ������ " + productName + " ��������.");
                } else if (idAsString.length() != 0) {
                    products = DbManager.getInstance().getSqlMethods().selectProductsById(id);
                    System.out.println("����� �� id ������ " + id + " ��������.");
                }

                StringBuilder retreiveText = new StringBuilder();

                for (int i = 0; i < products.size(); i++) {
                    String name = products.get(i).getName();
                    int count = products.get(i).getCount();
                    int pId = products.get(i).getId();

                    retreiveText.append(pId)
                            .append(" ")
                            .append(name)
                            .append(" ")
                            .append(count)
                            .append("\n");
                }

                retreiveText.append("\n�������� ��������:");
                StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());

                return PageMap.getInstance()
                        .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                        .someProcess(retreiveText.toString(), update.getMessage().getChatId());
            }
        } else if (update.hasCallbackQuery()) {

            StateMap.getInstance().replaceState(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getData());
            chat_id = update.getCallbackQuery().getMessage().getChatId().toString();

            return PageMap.getInstance()
                    .getPageByKey(StateMap.getInstance().getState(Long.parseLong(chat_id)).toString())
                    .someProcess(update.getCallbackQuery().getMessage().getText(), update.getCallbackQuery().getMessage().getChatId());
        }

        return new SendMessage(chat_id, "");
    }

}
