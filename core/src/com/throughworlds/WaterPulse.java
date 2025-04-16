package com.throughworlds;

public class WaterPulse {
    float x, y, s, p, startPower;
    int t = 0;

    public void math(){
        s+=p/3.5f;
        p/=1.1f;
        if (p<=1f){
            t=0;
        }
    }
}
