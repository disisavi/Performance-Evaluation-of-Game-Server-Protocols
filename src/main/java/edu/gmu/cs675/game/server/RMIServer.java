package edu.gmu.cs675.game.server;

import edu.gmu.cs675.game.remoteInterface.GameInterface;

import java.awt.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class RMIServer implements GameInterface {
    public static int port = 1024;
    public InetAddress inetAddress;
    public String hostName;

    // <Hostname,Name> IPPlayerMap
    Map<String, String> IPPlayerMap;
    Game game;

    RMIServer() {
        try {
            inetAddress = getSelfIP();
            hostName = inetAddress.getHostName();
            game = new Game();
            IPPlayerMap = new HashMap<String, String>();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getSelfIP() throws SocketException, UnknownHostException {

        final DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName("8.8.8.8"), RMIServer.port);
        InetAddress ip = InetAddress.getByName(socket.getLocalAddress().getHostAddress());

        return ip;
    }

    public void registerPlayer(String playerName) throws Exception {

        try {
            String clientHost = RemoteServer.getClientHost();
            IPPlayerMap.put(clientHost, playerName);
            this.game.registerPlayer(playerName);
        } catch (ServerNotActiveException e) {
            System.out.println("Attempt to add player " + playerName + " failed");
            e.printStackTrace();
            System.out.println("Still in business");
            throw e;
        }

    }

    public String getPlayerWon() throws RemoteException {
        return this.game.getPlayerWon();
    }

    public boolean move(String direction) throws Exception {
        try {
            String hostname = RemoteServer.getClientHost();
            this.game.move(direction, IPPlayerMap.get(hostname));
        } catch (ServerNotActiveException e) {
            System.out.println(e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;

        }
        return true;
    }

    public Point getCurrentPosition() throws RemoteException {
        Point point = null;
        try {
            String hostname = RemoteServer.getClientHost();
            point = this.game.getCurrentPosition(this.IPPlayerMap.get(hostname));
        } catch (ServerNotActiveException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Hostname Not found");
            throw e;
        }
        return point;
    }

    public String getAllPlayerNames() throws RemoteException {
        return this.game.getAllPlayerNames();
    }

    public boolean deRegisterPLayer() throws RemoteException, ServerNotActiveException {
        try {
            String hostname = RemoteServer.getClientHost();
            this.game.deRegisterPLayer(this.IPPlayerMap.get(hostName));
            IPPlayerMap.remove(hostname);
        } catch (ServerNotActiveException e) {
            System.out.println(e.getCause());
            throw e;
        }
        return true;
    }

    void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void shutdown(Exception exception) {
        //todo -- add it to serverServices...
        System.out.println("Shutting down Game RMI server");
        if (exception != null) {
            System.out.println("The following error lead to the shutdown");
            System.err.println(exception.getMessage());
            exception.printStackTrace();
        }
        try {

            Registry registry = LocateRegistry.getRegistry();
            registry.unbind(this.hostName);
            UnicastRemoteObject.unexportObject(this, true);
            Runtime.getRuntime().gc();
        } catch (AccessException e) {

        } catch (RemoteException e) {

        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        // otherwise we wait 60seconds for references to be removed
        Runtime.getRuntime().gc();


        System.exit(-1);

    }


    public void startRMIServer() {
        RMIServer rmiServer = new RMIServer();

        rmiServer.clearConsole();
        try {

            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(RMIServer.port);
            } catch (RemoteException e) {
                System.out.println("Unable to create registry.... Checking if registry already exist");
                registry = LocateRegistry.getRegistry(rmiServer.port);
            }
            GameInterface nodeStub = (GameInterface) UnicastRemoteObject.exportObject(rmiServer, RMIServer.port);

            registry.rebind(rmiServer.hostName, nodeStub);
            System.out.println("Game Server Startup Complete\nserver Name -- " + rmiServer.hostName);
            System.out.println("ip -- " + rmiServer.inetAddress.getHostAddress());
        } catch (RemoteException e) {
            System.out.println("Game server Startup Failure ...");
            rmiServer.shutdown(e);
        }

    }
}

