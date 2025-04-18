package com.throughworlds;

public class Flower{
    float x, y, s;
    int tq = 6;
    float[] tx = new float[tq];
    float[] ty = new float[tq];
    int[] tt = new int[tq];
    float r = 0; // Наклон цветка
    float maxRotate = 10;
    int t = 0;
    Main m;
    public Flower(Main main){
        m = main;
    }
    public void math(){
        r=Math.min(Math.max(r+m.windX/4f+(m.random.nextInt(3)-1)/10f,-maxRotate),maxRotate);

        for(int it=tq-1;it>=0;it--){
            tx[it] = x+(r*it*it/5f);
            ty[it] = it*s*3;
        }
    }
}
