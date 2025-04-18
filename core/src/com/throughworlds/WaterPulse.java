package com.throughworlds;

public class WaterPulse {
    float x, y, s, p, startPower;
    int t = 0;

    public void math(){
        s+=p/7.5f;
        p/=1.05f;
        if (p<=1f){
            t=0;
        }
    }
}
