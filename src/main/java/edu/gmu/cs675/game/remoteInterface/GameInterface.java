package edu.gmu.cs675.game.remoteInterface;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface GameInterface extends Remote {

    void registerPlayer(String playerName) throws Exception;

    String getPlayerWon() throws RemoteException;

    int move(String direction) throws Exception;

    Point getCurrentPosition() throws RemoteException;

    String getStats () throws RemoteException;

    String getAllPlayerNames() throws RemoteException;

    boolean deRegisterPLayer() throws RemoteException, ServerNotActiveException;
}
