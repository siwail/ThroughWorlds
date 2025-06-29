package com.throughworlds;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ShapeRendererRubber {
    ShapeRenderer drawer;
    Main m;
    ShapeRendererRubber(Main main){
        m = main;
        drawer = new ShapeRenderer();
    }
    public void begin(ShapeRenderer.ShapeType type){
        drawer.begin(type);
    }
    public void setColor(float r,float g, float b, float a){
        drawer.setColor(r,g,b,a);
    }
    public void rectLine(float x1, float y1, float x2, float y2, float s){
        drawer.rectLine(x1*m.sc, y1*m.sc, x2*m.sc, y2*m.sc, s*m.sc);
    }
    public void rect(float x, float y, float w, float h){
        drawer.rect(x*m.sc, y*m.sc, w*m.sc, h*m.sc);
    }
    public void end(){
        drawer.end();
    }
}
