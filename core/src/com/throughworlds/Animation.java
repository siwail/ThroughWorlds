package com.throughworlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Animation {
    float[][] r;
    public Animation(String filename){
        FileHandle file = Gdx.files.internal("tools/saves/"+filename);
        String text = file.readString();
        String[] frames = text.substring(2,text.length()-2).split("], \\[");
        r = new float[frames.length][14];
        Gdx.app.log(""+filename, ""+frames.length);
        for (int i=0;i<frames.length;i++){
            String[] parts = frames[i].split(", ");
            for(int j=0;j<14;j+=1){
                if(filename.substring(0,1).equals("R")){
                    if(j==0){
                        r[i][0] = -Float.parseFloat(parts[j]);
                    }
                    if(j==1){
                        r[i][2] = 0-Float.parseFloat(parts[j]);
                    }
                    if(j==2){
                        r[i][1] = 0-Float.parseFloat(parts[j]);
                    }
                    if(j==3){
                        r[i][4] = 0-Float.parseFloat(parts[j]);
                    }
                    if(j==4){
                        r[i][3] = 0-Float.parseFloat(parts[j]);
                    }
                    if(j==5){
                        r[i][6] = 0-Float.parseFloat(parts[j]);
                    }
                    if(j==6){
                        r[i][5] = 0-Float.parseFloat(parts[j]);
                    }
                    if(j==7){
                        r[i][8] = 0-Float.parseFloat(parts[j]);
                    }
                    if(j==8){
                        r[i][7] = 0-Float.parseFloat(parts[j]);
                    }
                    if(j==9){
                        r[i][10] = 0-Float.parseFloat(parts[j]);
                    }
                    if(j==10){
                        r[i][9] = 0-Float.parseFloat(parts[j]);
                    }
                    if(j==11){
                        r[i][12] = 0-Float.parseFloat(parts[j]);
                    }
                    if(j==12){
                        r[i][11] = 0-Float.parseFloat(parts[j]);
                    }
                    if(j==13){
                        r[i][13] = -Float.parseFloat(parts[j]);
                    }
                }else{
                    r[i][j] = Float.parseFloat(parts[j]);
                }
            }
        }
    }
}
