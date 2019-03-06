package edu.gmu.cs675.game.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class Server {

    public static void main(String args[]) {


        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(System.getProperty("user.dir"));
        System.out.println("Performance analysis of RMI vs Sockets");
        System.out.println("1. R for RMI\n2. S for Socket");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        Properties prop = new Properties();
        InputStream fileInput = null;
        try {

            fileInput = new FileInputStream("./bootProperties.properties");

            // load a properties file
            prop.load(fileInput);
            Game.maxHeight = Integer.parseInt(prop.getProperty("height"));
            Game.maxWidth = Integer.parseInt(prop.getProperty("width"));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in getting boot properties..." + e.getCause());
            System.out.println("Default values being set");
            Game.maxHeight = 10;
            Game.maxWidth = 10;

        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        while (true) {
            if (input.toUpperCase().equals("R")) {
                RMIServer rmiGameServer = new RMIServer();
                rmiGameServer.startRMIServer();
            } else if (input.toUpperCase().equals("S")) {
                SocketServer socketServer = new SocketServer();
                socketServer.startSocketServer();
            } else {
                System.out.println("Please enter correct Command");
                input = sc.nextLine();
            }
        }
    }
}
