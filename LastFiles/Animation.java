package com.throughworlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Animation {
    float[][] r;
    public Animation(String filename){
        FileHandle file = Gdx.files.internal("tools/saves/"+filename+".txt");
        String text = file.readString();
        String[] frames = text.substring(1,text.length()-1).split("], \\[");
        r = new float[frames.length][14];
        for (int i=0;i<frames.length;i++){
            String[] parts = frames[i].substring(1,frames[i].length()-1).split(", ");
            for(int j=0;j<14;j+=1){
                r[i][j] = Float.parseFloat(parts[j]);
            }
        }
    }
}
