package edu.gmu.cs675.game.server;


import edu.gmu.cs675.game.util.Player;
import edu.gmu.cs675.game.util.Zone;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.Map;

public class Game {
    public static int maxHeight, maxWidth;
    Zone zone;
    boolean didSomeoneWin = false;


    Game() {
        zone = new Zone();
    }

    public void registerPlayer(String playerName) {
        this.zone.addPlayerToBoard(playerName);
    }

    public String getPlayerWon() {
        StringBuilder returnString = new StringBuilder();
        if (!didSomeoneWin) {
            returnString.append("Error :- No One Won till now");
        }
        for (Map.Entry<String, Player> entry : this.zone.playerMap.entrySet()) {
            Player player = entry.getValue();
            if (player.isWon) {
                returnString.append(player.playerName);
            }
        }
        return returnString.toString();
    }

    public int move(String direction, String playerName) {
        if (this.didSomeoneWin) {
            if (this.getPlayerWon().equals(playerName)) {
                return 2;
            }
            return -2;
        }
        int value = -10;
        switch (direction.toUpperCase()) {
            case "UP":
                value = this.zone.moveUp(playerName);
                break;
            case "DOWN":
                value = this.zone.moveDown(playerName);
                break;
            case "LEFT":
                value = this.zone.moveLeft(playerName);
                break;
            case "RIGHT":
                value = this.zone.moveRight(playerName);
                break;
        }

        if (value >= 0
                && this.zone.playerMap.get(playerName).numberofGoalsReached == Zone.numberPrize) {
            this.didSomeoneWin = true;
            this.zone.playerMap.get(playerName).isWon = true;
            value = 2;
        }
        return value;
    }

    public Point getCurrentPosition(String playerName) throws RemoteException {
        return this.zone.playerMap.get(playerName).point;
    }

    public String getAllPlayerNames() throws RemoteException {
        StringBuilder returnString = new StringBuilder();
        int i = 0;
        for (String key : this.zone.playerMap.keySet()) {
            i++;
            returnString.append(i + " " + key + "\n");
        }
        return returnString.toString();
    }

    public boolean deRegisterPLayer(String playerName) throws RemoteException {
        this.zone.playerMap.remove(playerName);
        return true;
    }

    public String getStats(String playerName) {
        return this.zone.returnplayerStats(playerName);
    }
}
