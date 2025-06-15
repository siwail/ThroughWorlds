package com.throughworlds;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ThroughRunnable implements Runnable {
    ThroughServer server;
    Socket socket;
    int index;

    public ThroughRunnable(ThroughServer server, Socket socket, int index) {
        this.server = server;
        this.socket = socket;
        this.index = index;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)); BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            while (server.run) {
                // - - - ОТПРАВКА ИНФОРМАЦИИ - - -
                ThroughServerPacket sent = new ThroughServerPacket();
                sent.seed = server.seed;
                sent.index = index;
                int size = server.players.size();
                sent.x = new float[size];
                sent.y = new float[size];
                sent.z = new float[size];
                sent.vx = new float[size];
                sent.vy = new float[size];
                sent.vz = new float[size];
                sent.cr = new float[size];
                sent.cg = new float[size];
                sent.cb = new float[size];
                sent.health = new float[size];
                sent.maxHealth = new float[size];
                sent.name = new String[size];
                sent.t = new int[size];
                for (int i = 0; i < size; i++) {
                    ThroughPlayer player = server.players.get(i);
                    sent.x[i] = player.x;
                    sent.y[i] = player.y;
                    sent.z[i] = player.z;
                    sent.vx[i] = player.vx;
                    sent.vy[i] = player.vy;
                    sent.vz[i] = player.vz;
                    sent.cr[i] = player.cr;
                    sent.cg[i] = player.cg;
                    sent.cb[i] = player.cb;
                    sent.health[i] = player.health;
                    sent.maxHealth[i] = player.maxHealth;
                    sent.name[i] = player.name;
                    sent.t[i] = player.t;
                }
                String sentString = mapper.writeValueAsString(sent);

                out.write(sentString + "\n");
                out.flush();

                // - - - ПОЛУЧЕНИЕ ИНФОРМАЦИИ - - -
                ThroughClientPacket received = mapper.readValue(in.readLine(), ThroughClientPacket.class);
                ThroughPlayer player = server.players.get(index);
                player.x = received.x;
                player.y = received.y;
                player.z = received.z;
                player.vx = received.vx;
                player.vy = received.vy;
                player.vz = received.vz;
                player.cr = received.cr;
                player.cg = received.cg;
                player.cb = received.cb;
                player.maxHealth = received.maxHealth;
                player.health = received.health;
                player.name = received.name;
                player.t = received.t;

            }
        } catch (Exception e) {
            //throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            System.out.println("[SERVER] DISCONNECTED " + socket.getInetAddress().toString());
        }
    }
}
