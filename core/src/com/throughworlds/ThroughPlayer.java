package com.throughworlds;

public class ThroughPlayer {
    boolean active = true;
    float x, y, z;
    float vx = 0, vy = 0, vz = 0;
    float cr, cg, cb;
    float health;
    float maxHealth;
    String name;
    int t;
    public ThroughPlayer(ThroughServer server){
        health = 100;
        maxHealth = 100;
        t = server.random.nextInt(100);
        if (t<75){
            t = 0;
            name = server.firstNames[server.random.nextInt(server.firstNames.length)]+" "+server.secondNames[server.random.nextInt(server.secondNames.length)];
        }else{
            t = 1;
            name = server.vipNames[server.random.nextInt(server.vipNames.length)];
        }
        x = server.random.nextInt(400);
        y = server.random.nextInt(400);
        z = server.random.nextInt(100);
        cr = server.random.nextInt(255)/255f;
        cg = server.random.nextInt(255)/255f;
        cb = server.random.nextInt(255)/255f;
    }
}
