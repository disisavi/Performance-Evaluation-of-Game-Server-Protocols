package edu.gmu.cs675.game.remoteInterface;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface ClientInterface {

    void registerPlayer(String playerName) throws Exception;

    String getPlayerWon() throws RemoteException;

    Integer move(String direction) throws Exception;

    Point getCurrentPosition() throws RemoteException;

    String getStats() throws RemoteException;

    String getAllPlayerNames() throws RemoteException;

    Boolean deRegisterPLayer() throws RemoteException, ServerNotActiveException;


}
