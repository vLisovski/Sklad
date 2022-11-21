package com.warehouse.statemachine;

public enum States {

    MAIN_MENU("MAIN_MENU"),
    CURRENT_ORDERS("CURRENT_ORDERS"),
    ORDER_COLLECTED("ORDER_COLLECTED"),
    ADD_TO_WAREHOUSE("ADD_TO_WAREHOUSE"),
    REMOVE_FROM_WAREHOUSE("REMOVE_FROM_WAREHOUSE"),
    SEARCH_PRODUCT("SEARCH_PRODUCT"),
    TASK_LIST("TASK_LIST");

    private final String state_name;

    States(String state_name) {
        this.state_name = state_name;
    }



    @Override
    public String toString() {
        return state_name;
    }
}
