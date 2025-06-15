package com.throughworlds;

public class Cloud {
    int tq = 20;
    float x, y, vx, vy, state;
    float dark;
    float[] ts = new float[tq];
    float[] ty = new float[tq];
    float scale = 1;
    int t = 0;
    float tyd = 1;
    Main m;
    public Cloud(Main main){
        m = main;
    }
    public void math(){
        x+=vx*m.gameSpeed;
        y+=vy*m.gameSpeed;
        ty[0] += ((m.random.nextInt(3)-1)/3f+tyd)/15f;
        if(ty[0]>30){
            ty[0]=30;
            tyd = -1;
        }
        if(ty[0]<-30){
            ty[0]=-30;
            tyd = 1;
        }
        for(int it=tq-1;it>=0;it--){
            float max = tq/2f-Math.abs(it-tq/2f)*m.cloudsHeight/(tq/2f)+2;
            ts[it] = Math.max(Math.min(ts[it]+ (m.random.nextInt(3)-1)/20f,max),1+scale);
            if(it!=0) {
                ty[it] = ty[it - 1];
            }
        }
        if (x>m.w/m.s){
            t = 0;
        }

    }
}
