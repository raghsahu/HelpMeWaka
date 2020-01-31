package com.helpmewaka.ui.model;

/**
 * Created by Ravindra Birla on 23/09/2019.
 */
public class StateData {
    public String STATE_ID;
    public String State_Name;


    public StateData(String s){
        State_Name = s;
        STATE_ID = "-1";

    }

    @Override
    public String toString() {
        return State_Name;
    }

}
