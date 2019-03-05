package edu.gmu.cs675.game.server;

import edu.gmu.cs675.game.remoteInterface.GameCodes;
import edu.gmu.cs675.game.remoteInterface.GameInterface;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    public InetAddress inetAddress;

    // <Hostname,Name> IPPlayerMap
    static Map<String, String> IPPlayerMap;
    static Game game;

    SocketServer() {
        this.IPPlayerMap = new HashMap<>();
        game = new Game();

    }
    public InetAddress getSelfIP() throws SocketException, UnknownHostException {

        final DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName("8.8.8.8"), GameInterface.port);
        InetAddress ip = InetAddress.getByName(socket.getLocalAddress().getHostAddress());

        return ip;
    }


    public void startSocketServer() {
        try (ServerSocket listener = new ServerSocket(GameInterface.port)) {
                this.inetAddress = getSelfIP();
            System.out.println("Game Server Startup Complete");
            System.out.println("ip -- " + this.inetAddress.getHostAddress());
            ExecutorService pool = Executors.newFixedThreadPool(20);
            this.game = new Game();
            while (true) {
                pool.execute(new SocketListner(listener.accept()));
            }
        } catch (IOException e) {
            System.out.println("Problem in starting server ... " + e.getMessage());
            e.printStackTrace();
        }

    }

    private static class SocketListner implements Runnable, GameInterface {
        private Socket socket;
        private String invokerIP;

        SocketListner(Socket socket) {
            this.socket = socket;
            this.invokerIP = socket.getRemoteSocketAddress().toString();
        }

        @Override
        public void run() {
            System.out.println("Connected: " + socket.toString());
            try {
                Scanner in = new Scanner(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("DEBUG 1");
                if (in.hasNextLine()) {
                    System.out.println("DEBUG 1.3");
                    String[] command = in.nextLine().split(" ", 2);
                    System.out.println("DEBUG 2");
                    Map.Entry<Integer, Object> returnType = this.execute(command);
                    System.out.println("DEBUG 3");
                    if (GameCodes.VOID.equals(returnType.getKey())) {
                        out.writeObject(GameCodes.VOID);
                    } else {
                        out.writeObject(returnType.getValue());
                    }
                    System.out.println("DEBUG 5");
                }
            } catch (Exception e) {
                System.out.println("Error:" + socket+" "+e.getCause());
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
                System.out.println("Closed: " + socket);
            }
        }

        Map.Entry<Integer, Object> execute(String[] commandList) {
            AbstractMap.SimpleEntry<Integer, Object> returnMap = null;
            Object returnObject;

            try {
                switch (commandList[0]) {
                    case GameCodes.Register:
                        this.registerPlayer(commandList[1]);
                        returnMap = new AbstractMap.SimpleEntry<>(GameCodes.VOID, null);
                        break;
                    case GameCodes.DeRegister:
                        returnObject = this.deRegisterPLayer();
                        returnMap = new AbstractMap.SimpleEntry<>(GameCodes.NONVOID, returnObject);
                        break;
                    case GameCodes.GetAll:
                        returnObject = this.getAllPlayerNames();
                        returnMap = new AbstractMap.SimpleEntry<>(GameCodes.NONVOID, returnObject);
                        break;
                    case GameCodes.GetCurrPos:
                        returnObject = this.getCurrentPosition();
                        returnMap = new AbstractMap.SimpleEntry<>(GameCodes.NONVOID, returnObject);
                        break;
                    case GameCodes.GetPlayerWon:
                        returnObject = this.getPlayerWon();
                        returnMap = new AbstractMap.SimpleEntry<>(GameCodes.NONVOID, returnObject);
                        break;
                    case GameCodes.GetStats:
                        returnObject = this.getStats();
                        returnMap = new AbstractMap.SimpleEntry<>(GameCodes.NONVOID, returnObject);
                        break;
                    case GameCodes.Move:
                        returnObject = this.move(commandList[1]);
                        returnMap = new AbstractMap.SimpleEntry<>(GameCodes.NONVOID, returnObject);
                        break;
                    default:
                        System.out.println("Bad Request from host");
                }
                return returnMap;
            } catch (Exception e) {
                e.printStackTrace();
                return new AbstractMap.SimpleEntry<>(GameCodes.ERROR, null);
            }

        }


        @Override
        public void registerPlayer(String playerName) throws Exception {
            IPPlayerMap.put(this.invokerIP, playerName);
            game.registerPlayer(playerName);
            System.out.println("Player " + playerName + " from host " + invokerIP + " Registered");

        }

        @Override
        public String getPlayerWon() throws RemoteException {
            return game.getPlayerWon();
        }

        @Override
        public Integer move(String direction) throws Exception {
            return game.move(direction, IPPlayerMap.get(this.invokerIP));

        }

        @Override
        public Point getCurrentPosition() throws RemoteException {
            return game.getCurrentPosition(IPPlayerMap.get(this.invokerIP));
        }

        @Override
        public String getStats() throws RemoteException {
            return game.getStats(IPPlayerMap.get(this.invokerIP));
        }

        @Override
        public String getAllPlayerNames() throws RemoteException {
            return null;
        }

        @Override
        public Boolean deRegisterPLayer() throws RemoteException, ServerNotActiveException {
            return null;
        }
    }
}
/*
    Todo
    1. Spawn 2 threads, 1 for server and one for accepting commands
 */