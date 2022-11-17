package com.warehouse.statemachine;

import com.warehouse.entities.ProductPosition;
import com.warehouse.sqlclasses.DbManager;
import com.warehouse.strings.Strings;
import org.postgresql.util.PSQLException;
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
//TODO ������� ����� Route �� ���������
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

                try{
                    DbManager.getInstance().getSqlMethods().deleteOrderById(id);
                }catch (PSQLException e){
                    System.out.println("����� � id " + id + "������� ��� ��������� � ������ �� ������ �������.");
                    String retrieveText = "����� � id " + id + " ������� ��� ��������� � ������ �� ������ �������.\n\n������� ��������:";

                    StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());

                    return PageMap.getInstance()
                            .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                            .someProcess(retrieveText, update.getMessage().getChatId());
                }

            } else if (StateMap.getInstance().getState(update.getMessage().getChatId()).toString()
                    .equals(States.ADD_TO_WAREHOUSE.toString())) { //�������� �� �����
                String receivedText = update.getMessage().getText();
                String productName = receivedText.replaceAll("\\d", "").trim();
                String countAsString = receivedText.replaceAll("\\D", "").trim();
                int count = Integer.parseInt(countAsString);
                if (!(DbManager.getInstance().getSqlMethods().findProductByName(productName))) {
                    try{
                        DbManager.getInstance().getSqlMethods().insertProduct(productName, count);
                    }catch (PSQLException e){
                        System.out.println("���������� ������ " + productName + " " + count + " ���������.");
                        String retrieveText = "���������� ������ " + productName + " " + count + " ���������.\n\n������� ��������:";
                        StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());
                        return PageMap.getInstance()
                                .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                                .someProcess(retrieveText, update.getMessage().getChatId());
                    }
                } else {
                    int currentCount = DbManager.getInstance().getSqlMethods().selectProductCount(productName);
                    try{
                        DbManager.getInstance().getSqlMethods().updateProduct(productName, currentCount+count);
                    }catch (PSQLException e){
                        System.out.println("���������� ������ " + productName + " " + count + " ���������.");
                        String retrieveText = "���������� ������ " + productName + " " + count + " ���������.\n\n������� ��������:";

                        StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());

                        return PageMap.getInstance()
                                .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                                .someProcess(retrieveText, update.getMessage().getChatId());
                    }
                }

            } else if (StateMap.getInstance().getState(update.getMessage().getChatId()).toString()
                    .equals(States.REMOVE_FROM_WAREHOUSE.toString())) { //������ �� ������

                String receivedText = update.getMessage().getText();
                String productName = receivedText.replaceAll("\\d", "").trim();
                System.out.println(productName);
                String countAsString = receivedText.replaceAll("\\D", "").trim();
                int count = Integer.parseInt(countAsString);
                int currenCount = DbManager.getInstance().getSqlMethods().selectProductCount(productName);
                System.out.println(currenCount);
                String retrieveText;

                if (!(count < currenCount)) {
                    DbManager.getInstance().getSqlMethods().deleteProduct(productName);
                    System.out.println("�������� ������ " + productName + " " + count + " ���������.");
                    retrieveText = "�������� ������ " + productName + " " + count + " ���������.\n\n������� ��������:";
                } else {
                    try{
                        DbManager.getInstance().getSqlMethods().updateProduct(productName, currenCount - count);
                    }catch (PSQLException e){
                        System.out.println("���������� ���������� ������ " + productName + " �� " +  count + " ���������.");
                        retrieveText = "���������� ���������� ������ " + productName + " �� " +  count + " ���������.\n\n������� ��������:";
                        StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());
                        return PageMap.getInstance()
                                .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                                .someProcess(retrieveText, update.getMessage().getChatId());
                    }

                    System.out.println("���������� ���������� ������ " + productName + " �� " + count + " ���������.");
                    retrieveText = "���������� ���������� ������ " + productName + " �� " + (currenCount - count) + " ���������.\n\n������� ��������:";
                }

                StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());

                return PageMap.getInstance()
                        .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                        .someProcess(retrieveText, update.getMessage().getChatId());
            } else if (StateMap.getInstance().getState(update.getMessage().getChatId()).toString()
                    .equals(States.SEARCH_PRODUCT.toString())) {//����� �����

                String receivedText = update.getMessage().getText();
                String productName = receivedText.replaceAll("\\d", "").trim();
                String idAsString = receivedText.replaceAll("\\D", "").trim();
                List<ProductPosition> products = null;

                if (productName.length() != 0) {
                    products = DbManager.getInstance().getSqlMethods().selectProductsByName(productName);
                    System.out.println("����� �� ����� ������ " + productName + " ��������.");
                } else if (idAsString.length() != 0) {
                    int id = Integer.parseInt(idAsString);
                    products = DbManager.getInstance().getSqlMethods().selectProductsById(id);
                    System.out.println("����� �� id ������ " + id + " ��������.");
                }

                StringBuilder retrieveText = new StringBuilder();

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

                retrieveText.append("\n�������� ��������:");
                StateMap.getInstance().replaceState(update.getMessage().getChatId(), States.MAIN_MENU.toString());

                return PageMap.getInstance()
                        .getPageByKey(StateMap.getInstance().getState(update.getMessage().getChatId()).toString())
                        .someProcess(retrieveText.toString(), update.getMessage().getChatId());
            }
        } else if (update.hasCallbackQuery()) {

            StateMap.getInstance().replaceState(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getData());
            chat_id = update.getCallbackQuery().getMessage().getChatId().toString();

            if(StateMap.getInstance().getState(update.getCallbackQuery().getMessage().getChatId()).toString()
                    .equals(States.MAIN_MENU.toString())){
                return PageMap.getInstance()
                        .getPageByKey(StateMap.getInstance().getState(Long.parseLong(chat_id)).toString())
                        .someProcess(Strings.MAIN_MENU_TEXT,
                                update.getCallbackQuery().getMessage().getChatId());
            }else{
                return PageMap.getInstance()
                        .getPageByKey(StateMap.getInstance().getState(Long.parseLong(chat_id)).toString())
                        .someProcess("",
                                update.getCallbackQuery().getMessage().getChatId());
            }
        }

        return new SendMessage(chat_id, "");
    }

}
