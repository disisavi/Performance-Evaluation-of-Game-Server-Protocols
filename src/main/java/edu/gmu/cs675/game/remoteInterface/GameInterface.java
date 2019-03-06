package edu.gmu.cs675.game.remoteInterface;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface GameInterface extends Remote {
    public static final int port = 1024;

    void registerPlayer(String playerName) throws Exception;

    String getPlayerWon() throws RemoteException;

    Integer move(String direction) throws Exception;

    Point getCurrentPosition() throws RemoteException;

    String getStats () throws RemoteException;

    String getAllPlayerNames() throws RemoteException;

    void deRegisterPLayer() throws RemoteException, ServerNotActiveException;
}
