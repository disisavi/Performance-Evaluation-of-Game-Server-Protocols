package edu.gmu.cs675.game.client;

import edu.gmu.cs675.game.remoteInterface.ClientInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Client {
    ClientInterface game;
    boolean isRegistered;
    String name;
    Map<String, ActionManager> actionManagerMap;
    ActionManager.ActionNames actionNames;

    Client() {
        isRegistered = false;
        actionManagerMap = new HashMap<>();
        actionNames = new ActionManager.ActionNames();

        for (String name : actionNames.actionaNamesList) {
            ActionManager actionManager = new ActionManager(name);
            actionManagerMap.put(name, actionManager);
        }
    }


    void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


    void getServer() {
        System.out.println("Client for Performance analysis of RMI vs Sockets");
        System.out.println("1. R for connecting to RMI Server\n2. S for connecting to  Socket server");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        actionManagerMap.get(actionNames.GET_CONNECTION).setStartTime();
        try {
            if (input.toUpperCase().equals("R")) {

                this.game = new RMIClient();
            } else {
                //todo
            }

            actionManagerMap.get(actionNames.GET_CONNECTION).setEndTIme();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.clearConsole();
        System.out.println("Welcome to the game");

        client.getServer();
        client.run();

        System.out.println("Game over!!");
    }

    void run() {
        boolean run = true;
        this.showAvailableCommands();
        Scanner scanner = new Scanner(System.in);
        while (run) {
            String[] commandArray = scanner.nextLine().split(" ", 2);
            String command = commandArray[0].toUpperCase();
            switch (command) {
                case "CLEAR":
                    this.clearConsole();
                    break;
                case "REPORT":
                    this.printReport();
                    break;
                case "UP":
                case "DOWN":
                case "LEFT":
                case "RIGHT":
                    if (isRegistered) {
                        this.move(command);
                    } else {
                        System.out.println("Kindly enter one of the following commands");
                        this.showAvailableCommands();
                    }
                    break;
                case "WHO":
                    if (isRegistered) {
                        this.getPlayerWon();
                    } else {
                        System.out.println("Kindly enter one of the following commands");
                        this.showAvailableCommands();
                    }
                    break;
                case "POS":
                    if (isRegistered) {
                        this.getCurrentPosition();
                    } else {
                        this.showAvailableCommands();
                    }
                    break;
                case "INFO":
                    if (isRegistered) {
                        this.getAllPlayerNames();
                    } else
                        System.out.println("Kindly enter one of the following commands");
                    this.showAvailableCommands();
                    break;
                case "EXIT":
                    if (isRegistered) {
                        this.deRegisterPLayer();
                    } else
                        System.out.println("Kindly enter one of the following commands");
                    this.showAvailableCommands();
                    break;
                case "SHUT":
                    if (isRegistered) {
                        this.deRegisterPLayer();
                    }
                    run = false;
                    break;
                case "REGISTER":
                    if (!isRegistered) {
                        this.registerPlayer();
                    } else {
                        System.out.println("Player already registered.");
                    }
                case "STATS":
                    this.getStats();
                    break;
                default:
                    System.out.println("Kindly enter one of the following commands");
                    this.showAvailableCommands();
            }
        }
    }

    void move(String direction) {

        boolean printPos = true;
        try {
            actionManagerMap.get(actionNames.UPDATE_PLAYER_STATE).setStartTime();
            int result = this.game.move(direction);
            actionManagerMap.get(actionNames.UPDATE_PLAYER_STATE).setEndTIme();

            switch (result) {
                case 0:
                    System.out.println("Move successfull. current position ");
                    break;
                case 1:
                    System.out.println("Got a goal!!!");
                    this.getStats();
                    printPos = false;
                    break;
                case 2:
                    System.out.println("We Won!!!");
                    break;
                case -1:
                    System.out.println("Someone was already in the position");
                    break;
                case -2:
                    System.out.println("We Lost... Someone else won");
                    break;
                case -3:
                    System.out.println("Invalid move... That move will take us out of the board");
                    break;
                default:
                    System.out.println("Unexpected error occured from the server");
            }
            if (printPos) {
                this.getCurrentPosition();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getPlayerWon() {
        actionManagerMap.get(actionNames.GET_PLAYER_STATE).setStartTime();
        try {
            System.out.println(this.game.getPlayerWon());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        actionManagerMap.get(actionNames.GET_PLAYER_STATE).setEndTIme();
    }

    void getCurrentPosition() {
        actionManagerMap.get(actionNames.GET_PLAYER_STATE).setStartTime();
        try {
            System.out.println("Current Position is " + this.game.getCurrentPosition().toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        actionManagerMap.get(actionNames.GET_PLAYER_STATE).setEndTIme();
    }

    void getAllPlayerNames() {
        actionManagerMap.get(actionNames.GET_PLAYER_STATE).setStartTime();
        try {
            System.out.println("The following players are playing on the board");
            System.out.println(this.game.getAllPlayerNames());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        actionManagerMap.get(actionNames.GET_PLAYER_STATE).setEndTIme();
    }

    void deRegisterPLayer() {
        actionManagerMap.get(actionNames.UPDATE_PLAYER_STATUS).setStartTime();
        String message = "Coudlnt register player because ";
        try {
            this.game.deRegisterPLayer();
            this.isRegistered = false;
        } catch (RemoteException e) {
            System.out.println(message + " " + e.getMessage());
            e.printStackTrace();
        } catch (ServerNotActiveException e) {
            System.out.println(message + " " + e.getMessage());
            e.printStackTrace();
        }
        actionManagerMap.get(actionNames.UPDATE_PLAYER_STATUS).setEndTIme();
    }

    void registerPlayer() {
        System.out.println("please enter a Name to register");
        Scanner scanner = new Scanner(System.in);
        this.name = scanner.nextLine();

        actionManagerMap.get(actionNames.UPDATE_PLAYER_STATUS).setStartTime();
        try {
            this.game.registerPlayer(this.name);
            this.isRegistered = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        actionManagerMap.get(actionNames.UPDATE_PLAYER_STATUS).setEndTIme();
    }

    void getStats() {
        actionManagerMap.get(actionNames.GET_PLAYER_STATE).setStartTime();
        try {
            System.out.println(this.game.getStats());
        } catch (RemoteException e) {
            System.out.println("Coudlnt get stats because " + e.getMessage());
            e.printStackTrace();
        }
        actionManagerMap.get(actionNames.GET_PLAYER_STATE).setEndTIme();
    }

    void printReport() {
        this.clearConsole();
        for (Map.Entry<String, ActionManager> entry : actionManagerMap.entrySet()) {
            entry.getValue().printReport();
        }
    }

    void showAvailableCommands() {
        if (isRegistered) {
            System.out.println(". UP --> To move up ");
            System.out.println(". Down --> To move down ");
            System.out.println(". Left --> To move left ");
            System.out.println(". Right --> To move right ");
            System.out.println(". who --> To check Who won");
            System.out.println(". pos --> Check current Position");
            System.out.println(". Stats --> Check the current Stats ");
            System.out.println(". info --> Check who all are in the game right now");
            System.out.println(". exit --> Exit the game");
        } else {
            System.out.println("Kindly enter \"register\" to register to game server");
        }


        System.out.println(". report -- Print the performance report");
        System.out.println(". Clear -- Clear the screen");
        System.out.println(". shut --> to shutdown the node");
    }
}
