package com.throughworlds;

public class Particle{
    float x, y, z, vx, vy, vz, s;
    int tq = 5;
    float[] tx = new float[tq];
    float[] ty = new float[tq];
    float[] tz = new float[tq];
    float state = 0;
    int t = 0;
    Main m;
    public Particle(Main main){
        m = main;
        for(int it=0;it<tq;it++){
            tx[it] = 0;
            ty[it] = 0;
            tz[it] = 0;
        }
    }
    public void math(){
        if(state==0) {
            x += vx;
            y += vy;
            z += vz;
            for (int it = tq - 1; it > 0; it--) {
                tx[it] = tx[it - 1];
                ty[it] = ty[it - 1];
                tz[it] = tz[it - 1];
            }
            tx[0] = x;
            ty[0] = y;
            tz[0] = z;
            //s -= 0.01f;
            if (t == 1) {
                vz -= 0.35f;
                if (z < 0) {
                    z = 0;
                    boolean access = false;
                    if (m.act((int) (x / m.s), (int) (y / m.s))) {
                        for (int iz = (int) m.wetRate; iz < m.FZ; iz += 1) {
                            if (m.F[(int) (x / m.s)][(int) (y / m.s)][iz].t == 1) {
                                access = true;
                                break;
                            }
                        }
                    }
                    if (!access) {
                        state=1;
                        m.setWaterPulse(x / m.s, m.FY - y / m.s, 5);
                    } else {
                        vz *= -0.2f;
                        s -= 2f;
                        vx += m.random.nextInt(3) - 1;
                        vy += m.random.nextInt(3) - 1;
                    }
                }
            }
        }
        if (state!=0 || s<0){
            state+=0.5f;
        }
        if(state>=tq-1){
            t = 0;
        }
    }
}
