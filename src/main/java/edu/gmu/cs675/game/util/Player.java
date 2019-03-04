package edu.gmu.cs675.game.util;

import java.awt.*;
import java.util.ArrayList;

public class Player {

    public int numberofGoalsReached;
    public String playerName;
    public Point point;
    public boolean isWon;
    public ArrayList<Point> goalsReached;

    public Player(String name) {
        this.playerName = name;
        isWon = false;
        goalsReached = new ArrayList<>();
    }

    public boolean equals(Object player2) {
        if(! (player2 instanceof Player)){
            return false;
        }

        if(this.playerName.equals(((Player) player2).playerName)){
            return true;
        }
        return false;
    }

    public String toString(){
        StringBuilder string = new StringBuilder();

        string.append("*******\nPlayer -- "+this.playerName);
        string.append("\n Current Position -- "+this.point.toString());
        if (this.isWon){
            string.append("\nAnd "+this.playerName+" has won");
        }
        else
        {
            string.append("\nAnd the number of goals Reached is "+this.numberofGoalsReached);
        }

        return string.toString();
    }
}
