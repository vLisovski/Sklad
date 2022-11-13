package com.warehouse.statemachine;

import java.util.HashMap;
import java.util.Map;

public class StateMap {

    private Map<Long,States> statesMap;
    private StateMap(){
        statesMap = new HashMap<>();
    };
    private static StateMap instance;

    public static StateMap getInstance() throws Exception {
        if (instance == null) {
            instance = new StateMap();
        }
        return instance;
    }

    public States getState(long chat_id){
      return statesMap.get(chat_id);
    }

    private States switchState(String text){
        switch (text){
            case "MAIN_MENU":return States.MAIN_MENU;
            case "CURRENT_ORDERS":return States.CURRENT_ORDERS;
            case "ORDER_COLLECTED":return States.ORDER_COLLECTED;
            case "ADD_TO_WAREHOUSE":return States.ADD_TO_WAREHOUSE;
            case "REMOVE_FROM_WAREHOUSE":return States.REMOVE_FROM_WAREHOUSE;
            case "SEARCH_PRODUCT":return States.SEARCH_PRODUCT;
            case "TASK_LIST":return States.TASK_LIST;
            default:return States.MAIN_MENU;
        }
    }

    public void putState(long chat_id,String state_name){
        statesMap.put(chat_id,switchState(state_name));
    }

    public void replaceState(long chat_id,String state_name){
        statesMap.replace(chat_id,switchState(state_name));
    }




}
