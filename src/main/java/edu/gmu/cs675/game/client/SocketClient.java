package edu.gmu.cs675.game.client;

import edu.gmu.cs675.game.remoteInterface.ClientInterface;
import edu.gmu.cs675.game.remoteInterface.GameCodes;
import edu.gmu.cs675.game.remoteInterface.GameInterface;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient implements ClientInterface {
    String serverIP;
    ObjectInputStream returnMessage;
    PrintWriter outBoundMessage;
    Socket socket;

    SocketClient() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Game server ip ");
        this.serverIP = scanner.next();

        try {
            this.socket = new Socket(serverIP, GameInterface.port);
            returnMessage = new ObjectInputStream(socket.getInputStream());
            outBoundMessage = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error -- Couldn't connect to server" + e.getMessage());
            e.printStackTrace();
        }
    }

    public Object serverConnectionPoint(String[] commandList) {

        try {
            outBoundMessage.flush();
            if (commandList.length == 1) {
                outBoundMessage.println(commandList[0]);
            } else if (commandList.length > 1) {
                outBoundMessage.println(commandList[0] + " " + commandList[1]);
            }

            return returnMessage.readObject();

        } catch (IOException e) {
            System.out.println("Reading failed due to " + e.getMessage() + " " + e.getCause());
            e.printStackTrace();
            System.out.println("We will now exit the Client server");
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            System.out.println("Reading failed due to " + e.getMessage() + " " + e.getCause());
            e.printStackTrace();
            System.out.println("We will now exit the Client server");
            System.exit(-1);
        }
        return null;
    }

    @Override
    public Boolean registerPlayer(String playerName) {
        String[] returnList = new String[2];
        returnList[1] = playerName;
        returnList[0] = GameCodes.Register;
        this.serverConnectionPoint(returnList);
        return null;
    }

    @Override
    public String getPlayerWon() {
        String[] strings = new String[1];
        strings[0] = GameCodes.GetPlayerWon;
        Object retrunObject = this.serverConnectionPoint(strings);
        return (String) retrunObject;
    }

    @Override
    public Integer move(String direction) {
        String[] strings = new String[2];
        strings[0] = GameCodes.Move;
        strings[1] = direction;
        Object retrunObject = this.serverConnectionPoint(strings);
        return (Integer) retrunObject;
    }

    @Override
    public Point getCurrentPosition() {
        String[] strings = new String[1];
        strings[0] = GameCodes.GetCurrPos;
        Object retrunObject = this.serverConnectionPoint(strings);
        return (Point) retrunObject;
    }

    @Override
    public String getStats() {
        String[] strings = new String[1];
        strings[0] = GameCodes.GetStats;
        Object retrunObject = this.serverConnectionPoint(strings);
        return (String) retrunObject;
    }

    @Override
    public String getAllPlayerNames() {
        String[] strings = new String[1];
        strings[0] = GameCodes.GetAll;
        Object retrunObject = this.serverConnectionPoint(strings);
        return (String) retrunObject;
    }

    @Override
    public void deRegisterPLayer() {
        String[] strings = new String[1];
        strings[0] = GameCodes.DeRegister;
        this.serverConnectionPoint(strings);
    }
}
