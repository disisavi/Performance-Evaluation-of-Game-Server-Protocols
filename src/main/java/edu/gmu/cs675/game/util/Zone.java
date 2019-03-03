package edu.gmu.cs675.game.util;

import edu.gmu.cs675.game.server.Game;

import java.awt.*;
import java.util.*;


public class Zone {

    final static int maxHeight = Game.maxHeight, maxWidth = Game.maxWidth, numberPrize = 1;
    public int height, widht;
    public Point basePoint;
    public Map<String, Player> playerMap;
    public Set<Point> goalSet;

    public Zone() {
        height = 5;
        widht = 5;
        basePoint = new Point(0, 0);
        goalSet = generategoalSet(numberPrize);
        playerMap = new HashMap<String, Player>();
    }


    public static void main(String[] args) {
        Zone zone = new Zone();
        zone.addPlayerToBoard("avi");
        zone.addPlayerToBoard("abhijeet");
        zone.printZone();
        try {
            zone.moveUp("avi");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        zone.removePlayerFromBoard("avi");
        zone.printZone();
    }


    public void printZone() {
        if (height == -1) {
            System.out.println("The zone doesnt exist or hasent been initialised");
        } else {
            System.out.println("****************\n");
            System.out.println("The Zone's boundries are... \n\t[{" + this.basePoint.x + ", " + this.basePoint.y + "}, {"
                    + this.basePoint.x + ", " + (this.basePoint.y + height) + "}, {"
                    + (this.basePoint.x + widht) + ", " + (this.basePoint.y + height) + "}, {"
                    + (this.basePoint.x + widht) + ", " + this.basePoint.y + "}]");
            System.out.println("The players are are.. ");
            for (Map.Entry<String, Player> entry : playerMap.entrySet()) {
                System.out.println("Player " + entry.getKey() + " is at " + entry.getValue().point.toString() + " and the number of goals reached is " + entry.getValue().numberofGoalsReached);
            }
            System.out.println("The Prizes are in the following positions");
            for (Point p : goalSet) {
                System.out.println(p.toString());
                goalSet.toString();
            }

        }
    }

    public void moveUp(String playerName) throws Exception {
        Point point = playerMap.get(playerName).point;

        boolean notOverappingPlayer = true;
        for (Map.Entry<String, Player> entry : playerMap.entrySet()) {
            Player player = entry.getValue();
            if (player.point.equals(new Point(point.x, point.y + 1))) {
                notOverappingPlayer = false;
            }
        }
        if (point.y < this.height && notOverappingPlayer) {
            point.y++;
        } else {
            String exception;
            if (!notOverappingPlayer) {

                exception = "Another Player in the Point";
            } else {
                exception = "Illegal Move";
            }
            throw new Exception(exception);

        }
    }

    public void moveDown(String playerName) throws Exception {
        Point point = playerMap.get(playerName).point;

        boolean notOverappingPlayer = true;
        for (Map.Entry<String, Player> entry : playerMap.entrySet()) {
            Player player = entry.getValue();
            if (player.point.equals(new Point(point.x, point.y - 1))) {
                notOverappingPlayer = false;
            }
        }
        if (point.y - 1 >= this.basePoint.y && notOverappingPlayer) {
            point.y--;
        } else {
            String exception;
            if (!notOverappingPlayer) {

                exception = "Another Player in the Point";
            } else {
                exception = "Illegal Move";
            }
            throw new Exception(exception);
        }
    }

    public void moveLeft(String playerName) throws Exception {
        Point point = playerMap.get(playerName).point;

        boolean notOverappingPlayer = true;
        for (Map.Entry<String, Player> entry : playerMap.entrySet()) {
            Player player = entry.getValue();
            if (player.point.equals(new Point(point.x - 1, point.y))) {
                notOverappingPlayer = false;
            }
        }
        if (point.x - 1 > this.basePoint.x && notOverappingPlayer) {
            point.x--;
        } else {
            String exception;
            if (!notOverappingPlayer) {

                exception = "Another Player in the Point";
            } else {
                exception = "Illegal Move";
            }
            throw new Exception(exception);
        }
    }

    public void moveRight(String playerName) throws Exception {
        Point point = playerMap.get(playerName).point;

        boolean notOverappingPlayer = true;
        for (Map.Entry<String, Player> entry : playerMap.entrySet()) {
            Player player = entry.getValue();
            if (player.point.equals(new Point(point.x + 1, point.y))) {
                notOverappingPlayer = false;
            }
        }
        if (point.x < this.widht && notOverappingPlayer) {
            point.x++;
        } else {
            String exception;
            if (!notOverappingPlayer) {

                exception = "Another Player in the Point";
            } else {
                exception = "Illegal Move";
            }
            throw new Exception(exception);
        }
    }

    public void addPlayerToBoard(String playerName) {
        Random random = new Random();

        int x = random.nextInt(height);
        int y = random.nextInt(widht);

        Player player = new Player(playerName);
        player.point = new Point(x, y);
        playerMap.put(playerName, player);
    }

    public Set<Point> generategoalSet(int number) {
        Random random = new Random();
        Set<Point> returnSet = new HashSet<Point>();
        int x, y;
        for (int i = 0; i < number; i++) {
            x = random.nextInt(widht);
            y = random.nextInt(height);
            returnSet.add(new Point(x, y));
        }
        return returnSet;
    }

    public void removePlayerFromBoard(String playerName) {
        playerMap.remove(playerName);

    }


}

