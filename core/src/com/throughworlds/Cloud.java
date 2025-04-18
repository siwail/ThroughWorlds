package com.throughworlds;

public class Cloud {
    int tq = 20;
    float x, y, vx, vy, state;
    float dark;
    float[] ts = new float[tq];
    int t = 0;
    Main m;
    public Cloud(Main main){
        m = main;
    }
    public void math(){
        x+=vx*m.gameSpeed;
        y+=vy*m.gameSpeed;
        for(int it=tq-1;it>=0;it--){
            float max = tq/2f-Math.abs(it-tq/2f)*m.cloudsHeight/(tq/2f)+2;
            ts[it] = Math.max(Math.min(ts[it]+ (m.random.nextInt(3)-1)/20f,max),1);
        }
        if (x>m.w/m.s){
            t = 0;
        }
    }
}
