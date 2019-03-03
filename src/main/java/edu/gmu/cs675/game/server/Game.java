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

    public String getPlayerWon() throws RemoteException {
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

    public boolean move(String direction, String playerName) throws Exception {
        if (this.didSomeoneWin) {
            return false;
        }

        switch (direction.toUpperCase()) {
            case "UP":
                this.zone.moveUp(playerName);
                break;
            case "DOWN":
                this.zone.moveDown(playerName);
                break;
            case "LEFT":
                this.zone.moveDown(playerName);
                break;
            case "RIGHT":
                this.zone.moveRight(playerName);
                break;
            default:
                throw new Exception("Incorrect Direction");
        }
        return true;
    }

    public Point getCurrentPosition(String playerName) throws RemoteException {
        return this.zone.playerMap.get(playerName).point;
    }

    public String getAllPlayerNames() throws RemoteException {
        StringBuilder returnString = new StringBuilder();
        int i = 0;
        for (String key : this.zone.playerMap.keySet()) {
            i++;
            returnString.append("i " + key + "\n");
        }
        return returnString.toString();
    }

    public boolean deRegisterPLayer(String playerName) throws RemoteException {
        this.zone.playerMap.remove(playerName);
        return true;
    }


}
