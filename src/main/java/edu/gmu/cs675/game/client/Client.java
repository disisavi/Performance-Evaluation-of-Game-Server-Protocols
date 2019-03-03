package edu.gmu.cs675.game.client;

import edu.gmu.cs675.game.remoteInterface.GameInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.util.Scanner;

public class Client {
    public GameInterface game;
    boolean isRegistered;
    String name;

    Client() {
        isRegistered = false;
    }

    public GameInterface getGameStub() throws RemoteException, NotBoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Game server ip ");
        String host = scanner.next();

        Registry gameRegistry = LocateRegistry.getRegistry(host, 1024);
        GameInterface gameStub = (GameInterface) gameRegistry.lookup("game");
        return gameStub;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the game");
        Client client = new Client();
        try {
            client.game = client.getGameStub();
            System.out.println("please enter the player Name");
            Scanner scanner = new Scanner(System.in);
            client.name = scanner.nextLine();
            client.run();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        System.out.println("Game over!!");
    }

    void run() {
        boolean run = true;
        this.showAvailableCommands();
        Scanner scanner = new Scanner(System.in);
        while (run) {
            String[] commandArray = scanner.nextLine().split(" ", 2);
            String command = commandArray[0].toUpperCase();
            System.out.println(command);
            switch (command) {
                case "UP":
                case "DOWN":
                case "LEFT":
                case "RIGHT":
                    if (isRegistered) {
                        this.move(command);
                    } else
                        System.out.println("Kindly enter one of the following commands");
                    this.showAvailableCommands();
                    break;
                case "WHO":
                    if (isRegistered) {
                        this.getPlayerWon();
                    } else
                        System.out.println("Kindly enter one of the following commands");
                    this.showAvailableCommands();
                    break;
                case "POS":
                    if (isRegistered) {
                        this.getCurrentPosition();
                    } else
                        this.showAvailableCommands();
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
                    break;
                default:
                    System.out.println("Kindly enter one of the following commands");
                    this.showAvailableCommands();
            }
        }
    }

    void move(String direction) {

        try {
            int result = this.game.move(direction);

            switch (result) {
                case 0:
                    System.out.println("Move successfull. current position ");
                    break;
                case 1:
                    System.out.println("Got a goal!!!");
                    //todo --> check how many goals left;
                    break;
                case 2:
                    System.out.println("We Won!!!");
                    break;
                case -1:
                    System.out.println("Someone was already in the position");
                    break;
                case -2:
                    System.out.println("We Lost... Someone else won");
                case -3:
                    System.out.println("Invalid move... That move will take us out of the board");
                    break;
                    default:
                        System.out.println("Unexpected error occured from the server");
            }
            this.getCurrentPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getPlayerWon() {
        try {
            System.out.println(this.game.getPlayerWon());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void getCurrentPosition() {
        try {
            System.out.println("Current Position is " + this.game.getCurrentPosition().toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void getAllPlayerNames() {
        try {
            System.out.println(this.game.getAllPlayerNames());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void deRegisterPLayer() {
        try {
            this.game.deRegisterPLayer();
            this.isRegistered = false;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }

    }

    void registerPlayer() {
        try {
            this.game.registerPlayer(this.name);
            this.isRegistered = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showAvailableCommands() {
        if (isRegistered) {
            System.out.println("The game has now begun...");
            System.out.println(". UP --> To move up ");
            System.out.println(". Down --> To move down ");
            System.out.println(". Left --> To move left ");
            System.out.println(". Right --> To move right ");
            System.out.println(". who --> To check Who won");
            System.out.println(". pos --> Check current Position");
            System.out.println(". info --> Check who all are in the game right now");
            System.out.println(". exit --> Exit the game");
        } else {
            System.out.println("Kindly enter \"register\" to register to game server");
        }
        System.out.println(". shut --> to shutdown the node");
    }
}
