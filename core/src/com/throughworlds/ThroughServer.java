package com.throughworlds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class ThroughServer {
    Random random = new Random();
    int seed;
    ArrayList<ThroughPlayer> players = new ArrayList<>();
    boolean run = true;
    String[] firstNames, secondNames, vipNames;
    public static void main(String[] args) {
        ThroughServer throughServer = new ThroughServer();
        try {
            throughServer.start();
        } catch (IOException | InterruptedException e) {
            System.out.println("[SERVER] ERROR");
            throw new RuntimeException(e);
        }

    }

    public void start() throws IOException, InterruptedException {
        readNames();
        ServerSocket server = new ServerSocket(4555);
        System.out.println("[SERVER] STARTED");
        seed = random.nextInt(100000);
        while (run) {
            Socket socket = server.accept();
            System.out.println("[SERVER] CONNECTED " + socket.getInetAddress().toString());
            players.add(new ThroughPlayer(this));
            Thread thread = new Thread(new ThroughRunnable(this, socket, players.size()-1));
            thread.start();
            Thread.sleep(1000);
        }
        server.close();
        System.out.println("[SERVER] CLOSED");
    }

    public void readNames(){
        String names = "";
        try(BufferedReader in = new BufferedReader(
                new InputStreamReader(Files.newInputStream(Paths.get("data/names.txt")), StandardCharsets.UTF_8))){
            String readed = in.readLine();
            while (readed!=null) {
                names += readed+"\n";
                readed = in.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] parts = names.split("\n");
        firstNames = parts[0].split(", ");
        secondNames = parts[1].split(", ");
        vipNames = parts[2].split(", ");
    }
}
