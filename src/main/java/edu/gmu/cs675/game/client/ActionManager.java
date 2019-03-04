package edu.gmu.cs675.game.client;

import java.util.ArrayList;

public class ActionManager {
    String id;
    private long startTime;
    private long endTIme;
    private long timeTaken;
    private int count;
    private long averageTime;


    ActionManager(String name) {
        id = name;
        count = 0;
        averageTime = 0;
    }

    void setStartTime() {
        this.startTime = System.nanoTime();
    }

    void setEndTIme() {
        this.endTIme = System.nanoTime();
        timeTaken = endTIme - startTime;
        this.averageTime = ((this.averageTime * count + timeTaken) / (count + 1));
        count++;
    }

    void printReport(){
        System.out.println("_________________");
        System.out.println("Action Id = "+this.id);
        System.out.println("Average time taken = "+averageTime);
        System.out.println(count+" Completed Operations");
    }

    public static class ActionNames{
        String UPDATE_PLAYER_STATE = "Move";
        String UPDATE_PLAYER_STATUS = "UpdateplayerStatus";
        String GET_PLAYER_STATE = "GetPlayerState";
        String GET_CONNECTION = "GetConnetion";
        ArrayList<String> actionaNamesList;

        ActionNames(){
            actionaNamesList = new ArrayList<>();
            actionaNamesList.add(UPDATE_PLAYER_STATE);
            actionaNamesList.add(UPDATE_PLAYER_STATUS);
            actionaNamesList.add(GET_PLAYER_STATE);
            actionaNamesList.add(GET_CONNECTION);
        }
    }

}
