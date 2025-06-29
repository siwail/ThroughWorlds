package com.throughworlds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteBatchRubber {
    SpriteBatch batch;
    Main m;
    SpriteBatchRubber(Main main){
        m = main;
        batch = new SpriteBatch();
    }

    public void begin(){
        batch.begin();
    }
    public void setColor(float r,float g, float b, float a){
        batch.setColor(r,g,b,a);
    }
    public void draw(Texture texture, float x, float y, float w, float h){
        batch.draw(texture, x*m.sc, y*m.sc, w*m.sc, h*m.sc);
    }
    public void end(){
        batch.end();
    }

}
