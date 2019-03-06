package edu.gmu.cs675.game.client;


import edu.gmu.cs675.game.remoteInterface.ClientInterface;
import edu.gmu.cs675.game.remoteInterface.GameInterface;

import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.util.Scanner;


public class RMIClient implements ClientInterface {
    GameInterface game;

    RMIClient() throws RemoteException, NotBoundException {
        this.game = this.getGameStub();
    }

    public GameInterface getGameStub() throws RemoteException, NotBoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Game server ip ");
        String host = scanner.next();

        Registry gameRegistry = LocateRegistry.getRegistry(host, 1024);
        GameInterface gameStub = (GameInterface) gameRegistry.lookup("game");

        return gameStub;
    }

    @Override
    public Boolean registerPlayer(String playerName) throws Exception {
        this.game.registerPlayer(playerName);
        return null;
    }

    @Override
    public String getPlayerWon() throws RemoteException {
        return game.getPlayerWon();
    }

    @Override
    public Integer move(String direction) throws Exception {
        return this.game.move(direction);
    }

    @Override
    public Point getCurrentPosition() throws RemoteException {
        return this.game.getCurrentPosition();
    }

    @Override
    public String getStats() throws RemoteException {
        return this.game.getStats();
    }

    @Override
    public String getAllPlayerNames() throws RemoteException {
        return this.game.getAllPlayerNames();
    }

    @Override
    public void deRegisterPLayer() throws RemoteException, ServerNotActiveException {
        this.game.deRegisterPLayer();
    }
}
