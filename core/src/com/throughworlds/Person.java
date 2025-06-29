package com.throughworlds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Random;

public class Person extends Entity {
    float cr, cg, cb;
    float r = 0;
    float mx, my, mz;
    float health = 0;
    float lastHealth = 0;
    float maxHealth = 100;
    int seed = 0;
    String name = "";
    int nameT; // Тип имени (шутливое, сгенерированное)

    float skinR, skinG, skinB; // Цвет кожи
    int headT; // Тип головы

    float eyesR, eyesG, eyesB; // Цвет глаз
    int eyesT; // Тип глаз

    float hairR, hairG, hairB; // Цвет волос
    int hairT; // Тип волос

    float beardR, beardG, beardB; // Цвет бороды
    int beardT; // Тип бороды

    float propR, propG, propB; // Цвет аксессуара
    int propT; // Тип аксессуара

    float hatR, hatG, hatB; // Цвет шляпы
    int hatT; // Тип шляпы

    float dressR, dressG, dressB; // Цвет накидки
    int dressT; // Тип накидки

    float bodyR, bodyG, bodyB; // Цвет нагрудника
    int bodyT; // Тип нагрудника

    float handR, handG, handB; // Цвет рукавов
    int handT; // Тип рукавов

    float glovesR, glovesG, glovesB; // Цвет перчаток
    int glovesT; // Тип перчаток

    float legR, legG, legB; // Цвет штанов
    int legT; // Тип штанов

    float bootR, bootG, bootB; // Цвет обуви
    int bootT; // Тип обуви

    int index = 0;
    int dir = 1;
    int frame = 0;
    float frameAnim = 0;
    int minFrame = 0;
    int maxFrame = 50;
    int tq = 16;
    float[] tx = new float[tq];
    float[] ty = new float[tq];
    float[] tz = new float[tq];
    float[] t2x = new float[tq];
    float[] t2y = new float[tq];
    float[] t2z = new float[tq];
    Texture[] head = new Texture[4];
    Texture[] eyes = new Texture[4];
    Texture[] hat = new Texture[4];
    Texture[] beard = new Texture[4];
    Texture[] prop = new Texture[4];
    Texture[] hair = new Texture[4];
    Texture[] boot = new Texture[4];
    Texture[] gloves = new Texture[4];
    ArrayList<ArrayList<Texture>> anim = new ArrayList<>(); // Анимации персонажа (количество анимаций, стороны, количество кадров)

    public Person(Main m, float x, float y, float z, int index) {
        super(m, x, y, z);
        cr = 1;
        cg = 0.9f;
        cb = 0.9f;
        s = 192;
        anim.add(new ArrayList<>()); // Анимации на камеру
        anim.add(new ArrayList<>()); // Анимации слева
        anim.add(new ArrayList<>()); // Анимации жопой к камере
        anim.add(new ArrayList<>()); // Анимации справа
        this.index = index;
        for (int it = 0; it < tq; it++) {
            tx[it] = x;
            ty[it] = y;
            tz[it] = z;
            t2x[it] = x;
            t2y[it] = y;
            t2z[it] = z;
        }
    }

    public void math() {
        x += vx;
        y += vy;
        z += vz;

        vz -= 0.4f;
        vx /= 1.05f;
        vy /= 1.05f;
        if (index != m.pid) {
            vx += m.sin(m.rotateAnim + m.random.nextInt(180) - 90) / 2f;
            vy += m.cos(m.rotateAnim + m.random.nextInt(180) - 90) / 2f;
        }

        if (z <= 0) {
            if (Math.abs(vz) > 20) {
                health -= Math.abs(vz / 15f);
                vx += (m.random.nextInt(3) - 1) * vz / 2f;
                vy += (m.random.nextInt(3) - 1) * vz / 2f;
            }
            z = 0;
            vz *= -0.5f;
            if (Math.abs(vz) < 0.5f) {
                vz = 0;
            } else {
                //int px = (int)(x/m.s);
                //int py = (int)(y/m.s);
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
                    m.setWaterPulse(x / m.s, m.FY - y / m.s, Math.abs(vz));
                }
                vz *= 0.5f;
            }
        }


    }

    public void draw() {
        float cx = m.cx, cy = m.cy;
        if (dir != 2) {
            if (dressT == 0) {
                drawHood(cx, cy, 1);
            }
        }
        if (frame < anim.get(dir).size()) {
            m.batch.draw(anim.get(dir).get(frame), -cx + mx - s / 2, -cy + my + mz, s, s);
        } else {
            m.batch.draw(anim.get(dir).get(anim.get(dir).size()-1), -cx + mx - s / 2, -cy + my + mz, s, s);
        }
        if (dir == 2) {
            if (dressT == 0) {
                drawHood(cx, cy, 1);
            }
        }
    }

    public void drawHood(float cx, float cy, float d) {
        m.batch.end();
        m.drawer.begin(ShapeRenderer.ShapeType.Filled);
        for (int it = tq - 1; it > 0; it--) {
            float dop = 0;
            if (d == -1) {
                dop = -s;
            }
            float r = Math.max(dressR - it / 60f + (m.random.nextInt(5) - 2) / 50f, 0);
            float g = Math.max(dressG - it / 60f + (m.random.nextInt(5) - 2) / 50f, 0);
            float b = Math.max(dressB - it / 60f + (m.random.nextInt(5) - 2) / 50f, 0);
            m.drawer.setColor(r * 0.75f, g * 0.75f, b * 0.75f, 1);
            m.rectLine(tx[it] - cx, ty[it] + tz[it] * d + dop - cy, t2x[it] - cx, t2y[it] + t2z[it] * d + dop - cy, 3 + it);
            m.drawer.setColor(r * 0.85f, g * 0.85f, b * 0.95f, 1);
            m.rectLine(tx[it - 1] - cx, ty[it - 1] + tz[it] * d + dop - cy, t2x[it] - cx, t2y[it] + t2z[it] * d + dop - cy, 3 + it);
            m.rectLine(tx[it] - cx, ty[it] + tz[it] * d + dop - cy, t2x[it - 1] - cx, t2y[it - 1] + t2z[it - 1] * d + dop - cy, 3 + it);
            m.drawer.setColor(r * 0.9f, g * 0.9f, b * 0.9f, 1);
            m.rectLine(tx[it] - cx, ty[it] + tz[it] * d + dop - cy, tx[it - 1] - cx, ty[it - 1] + tz[it - 1] * d + dop - cy, 3 + it);
            m.rectLine(t2x[it] - cx, t2y[it] + t2z[it] * d + dop - cy, t2x[it - 1] - cx, t2y[it - 1] + t2z[it - 1] * d + dop - cy, 3 + it);

        }
        m.drawer.end();
        m.batch.begin();
    }


    public void drawShadow() {
        float cx = m.cx, cy = m.cy;
        m.batch.draw(m.shadow, -cx + mx - s / 4f - mz / 16f, -cy + my - s / 8f - mz / 16f, mz / 8f + s / 2f, mz / 8f + s / 2f);
    }

    public void drawMirror() {


        float cx = m.cx, cy = m.cy;
        lastHealth += (health - lastHealth) / 40f;
        mx += (x - mx) / 3f;
        my += (y - my) / 3f;
        mz += (z - mz) / 3f;
        frameAnim += (Math.abs(vx) + Math.abs(vy)) / 10f;
        if (Math.abs(vx) + Math.abs(vy) <= 1.5f) {
            frameAnim += (minFrame - frameAnim) / 6f;
        }

        if (frame >= anim.get(dir).size() || frame >= maxFrame) {

            frameAnim = minFrame;
        }
        if (frame < minFrame) {

            frameAnim += (minFrame - frameAnim) / 4f;
        }
        frame = (int) frameAnim;
        if (Math.abs(vx) > Math.abs(vy)) {
            if (vx < 0) {
                dir = 3;
                minFrame = 32;
                maxFrame = 55;
            } else {
                dir = 0;
                minFrame = 32;
                maxFrame = 55;
            }
        }
        if (Math.abs(vx) < Math.abs(vy)) {
            if (vy < 0) {
                dir = 1;
                minFrame = 0;
                maxFrame = 32;
            } else {
                dir = 2;
                minFrame = 0;
                maxFrame = 32;
            }
        }

        for (int it = tq - 1; it > 0; it--) {
            tx[it] += (tx[it - 1] - tx[it]) / 2.2f + m.random.nextInt(7) - 3;
            ty[it] += (ty[it - 1] - ty[it]) / 2.2f + m.random.nextInt(7) - 3;
            tz[it] = tz[it - 1] - 3f;
            t2x[it] += (t2x[it - 1] - t2x[it]) / 1.75f + m.random.nextInt(5) - 2;
            t2y[it] += (t2y[it - 1] - t2y[it]) / 1.75f + m.random.nextInt(5) - 2;
            t2z[it] = t2z[it - 1] - 2.9f;
        }
        tx[0] = mx - 4;
        ty[0] = my + s / 2f;
        tz[0] = mz;
        t2x[0] = mx + 4;
        t2y[0] = my + s / 2f;
        t2z[0] = mz;
        if (dir != 2) {
            if (dressT == 0) {
                drawHood(cx, cy, -1);
            }
        }
        if (frame < anim.get(dir).size()) {
            m.batch.draw(anim.get(dir).get(frame), -cx + mx - s / 2, -cy + my - mz, s, -s);
        } else {
            m.batch.draw(anim.get(dir).get(anim.get(dir).size() - 1), -cx + mx - s / 2, -cy + my - mz, s, -s);
        }
        if (dir == 2) {
            if (dressT == 0) {
                drawHood(cx, cy, -1);
            }
        }
    }

    public void drawBar() {
        float bs = s * 2.45f / 5f;
        float cx = m.cx, cy = m.cy;
        float bx = -cx + mx, by = -cy + my + mz + s * 0.89f;
        m.batch.end();
        m.drawer.begin(ShapeRenderer.ShapeType.Filled);

        /*if (nameT == 0) {
            m.drawer.setColor(0.01f, 0.01f, 0.01f, 1);
            m.drawer.rectLine(bx - bs / 2f, by, bx + bs / 2f, by, 5);
            m.drawer.setColor(0.7f + cg / 16f, 0.7f + cg / 16f, 0.7f + cg / 16f, 1);
            m.drawer.rectLine(bx - bs / 2f + 2, by, bx + bs / 2f - 2, by, 3);
            m.drawer.setColor(0.01f, 0.01f, 0.01f, 1);
            m.drawer.circle(bx - bs / 2f - 2, by, 6);
            m.drawer.circle(bx + bs / 2f + 2, by, 6);
            m.drawer.setColor(0.7f + cr / 16f, 0.7f + cg / 16f, 0.7f + cb / 16f, 1);
            m.drawer.circle(bx - bs / 2f - 2, by, 4);
            m.drawer.circle(bx + bs / 2f + 2, by, 4);
        } else {
            int n = 20;
            for (int i = 0; i < n; i++) {
                m.drawer.setColor(0.01f, 0.01f, 0.01f, 1);
                m.drawer.rectLine(bx - bs / 2f + bs / n * i + 1, by, bx - bs / 2f + bs / n * (i + 1) - 1, by, 5);
                m.drawer.setColor(0.6f + cg / 8f, 0.8f + cg / 8f, 0.35f + cg / 8f, 1);
                m.drawer.rectLine(bx - bs / 2f + bs / n * i + 2, by, bx - bs / 2f + bs / n * (i + 1) - 2, by, 3);
            }
            m.drawer.setColor(0.01f, 0.01f, 0.01f, 1);
            m.drawer.circle(bx - bs / 2f - 2, by, 6);
            m.drawer.circle(bx + bs / 2f + 2, by, 6);
            m.drawer.setColor(0.6f + cr / 16f, 0.7f + cg / 16f, 0.35f + cb / 16f, 1);
            m.drawer.circle(bx - bs / 2f - 2, by, 4);
            m.drawer.circle(bx + bs / 2f + 2, by, 4);
        }

        m.drawer.setColor(0.01f, 0.01f, 0.01f, 1);
        m.drawer.rectLine(bx - bs / 2f, by - s / 6f, bx + bs / 2f, by - s / 6f, 8);

        m.drawer.setColor((0.1f + (1 - lastHealth / maxHealth) * 0.85f) * 1.5f, (0.1f + lastHealth / maxHealth * 0.85f) * 1.5f, 0.35f, 1);
        m.drawer.rectLine(bx - bs / 2f + 2, by - s / 6f, bx - bs / 2f + (bs - 2) * lastHealth / maxHealth, by - s / 6f, 6);

        m.drawer.setColor(0.1f + (1 - health / maxHealth) * 0.85f, 0.1f + health / maxHealth * 0.85f, 0.25f, 1);
        m.drawer.rectLine(bx - bs / 2f + 2, by - s / 6f, bx - bs / 2f + (bs - 2) * health / maxHealth, by - s / 6f, 6);

*/
        m.drawer.end();
        m.batch.begin();
        if (nameT == 0) {
            String str = name;
            int size = 8;
            if (name.length() < 7) {
                size = 11;
            }
            if (name.length() > 16) {
                size = 3;
            }
            for (int i = 0; i < str.length(); i++) {
                m.font[size].setColor((cr / 2f + 0.25f) * 1.45f, (cg / 2f + 0.25f) * 1.45f, (cb / 2f + 0.25f) * 1.45f, 1);
                m.font[size].draw(m.batch.batch, str.substring(i, i + 1), (bx - bs / 1.5f + i * bs / 0.75f / str.length()) * m.sc, (by + m.sin(m.rotateAnim + i * 10) + bs / 7.4f) * m.sc);
            }
        } else {
            String str = name;
            int size = 8;
            if (name.length() < 7) {
                size = 11;
            }
            if (name.length() > 15) {
                size = 3;
            }
            for (int i = 0; i < str.length(); i++) {
                float d = m.sin(m.rotateAnim + 30 * i) / 4f;
                m.font[size].setColor((cr / 1.25f + 0.1f + d / 2f) * 1.55f, (cg / 1.25f + 0.1f + d) * 1.55f, (cb / 1.25f + 0.1f + d / 5f) * 1.55f, 1);
                m.font[size].draw(m.batch.batch, str.substring(i, i + 1), (bx - bs / 1.5f + i * bs / 0.75f / str.length()) * m.sc, (by + m.cos(m.rotateAnim + i * 15) * 4 + bs / 5f) * m.sc);
            }
        }
    }

    public void generate(int seed) {
        Random random = new Random(seed);
        int dressMode = 0; // Хаотичный / строгий стиль
        if (nameT == 0) { // Если случайное имя
            if (random.nextInt(100) < 10) { // Шанс на строгий стиль 10%
                dressMode = 1;
            }
        } else { // Если легендарное имя
            if (random.nextInt(100) >= 10) { // Шанс на строгий стиль 90%
                dressMode = 1;
            }
        }
        if (dressMode == 0) { // Хаотичные цвета одежды
            if (random.nextInt(3) == 0) {
                skinR = color(random, 215, 15);
                skinG = color(random, 215, 15);
                skinB = color(random, 215, 15);
            } else {
                skinR = color(random, 70, 165);
                skinG = color(random, 65, 155);
                skinB = color(random, 65, 120);
            }


            hairR = color(random, 210, 15);
            hairG = color(random, 210, 15);
            hairB = color(random, 210, 15);
            eyesR = color(random, 210, 15);
            eyesG = color(random, 210, 15);
            eyesB = color(random, 210, 15);
            if (random.nextInt(3) == 0) {
                beardR = color(random, 210, 15);
                beardG = color(random, 210, 15);
                beardB = color(random, 210, 15);
            } else {
                beardR = hairR;
                beardG = hairG;
                beardB = hairB;
            }
            propR = color(random, 210, 15);
            propG = color(random, 210, 15);
            propB = color(random, 210, 15);
            hatR = color(random, 210, 15);
            hatG = color(random, 210, 15);
            hatB = color(random, 210, 15);
            dressR = color(random, 210, 15);
            dressG = color(random, 210, 15);
            dressB = color(random, 210, 15);
            bodyR = color(random, 210, 15);
            bodyG = color(random, 210, 15);
            bodyB = color(random, 210, 15);
            handR = color(random, 210, 15);
            handG = color(random, 210, 15);
            handB = color(random, 210, 15);
            glovesR = color(random, 210, 15);
            glovesG = color(random, 210, 15);
            glovesB = color(random, 210, 15);
            legR = color(random, 210, 15);
            legG = color(random, 210, 15);
            legB = color(random, 210, 15);
            bootR = color(random, 210, 15);
            bootG = color(random, 210, 15);
            bootB = color(random, 210, 15);
        } else { // Строгие цвета одежды
            int cpq;
            float[] cp; // Пул цветов
            cpq = random.nextInt(4) + 2;
            cp = new float[cpq];
            cp[0] = random.nextInt(2) * 210 + 20f;
            for (int i = 1; i < cpq; i++) {
                cp[i] = color(random, 235, 10);
            }
            float dark = (random.nextInt(50) - 25) / 255f;
            if (random.nextInt(2) == 0) {
                skinR = solidColor(random, cp, dark);
                skinG = solidColor(random, cp, dark);
                skinB = solidColor(random, cp, dark);
            } else {
                skinR = color(random, 75, 165);
                skinG = color(random, 70, 155);
                skinB = color(random, 70, 120);
            }
            dark = (random.nextInt(50) - 25) / 255f;
            hairR = solidColor(random, cp, dark);
            hairG = solidColor(random, cp, dark);
            hairB = solidColor(random, cp, dark);
            dark = (random.nextInt(50) - 25) / 255f;
            eyesR = solidColor(random, cp, dark);
            eyesG = solidColor(random, cp, dark);
            eyesB = solidColor(random, cp, dark);
            if (random.nextInt(3) == 0) {
                dark = (random.nextInt(50) - 25) / 255f;
                beardR = solidColor(random, cp, dark);
                beardG = solidColor(random, cp, dark);
                beardB = solidColor(random, cp, dark);
            } else {
                beardR = hairR;
                beardG = hairG;
                beardB = hairB;
            }
            dark = (random.nextInt(50) - 25) / 255f;
            propR = solidColor(random, cp, dark);
            propG = solidColor(random, cp, dark);
            propB = solidColor(random, cp, dark);
            dark = (random.nextInt(50) - 25) / 255f;
            hatR = solidColor(random, cp, dark);
            hatG = solidColor(random, cp, dark);
            hatB = solidColor(random, cp, dark);
            dark = (random.nextInt(50) - 25) / 255f;
            dressR = solidColor(random, cp, dark);
            dressG = solidColor(random, cp, dark);
            dressB = solidColor(random, cp, dark);
            dark = (random.nextInt(50) - 25) / 255f;
            bodyR = solidColor(random, cp, dark);
            bodyG = solidColor(random, cp, dark);
            bodyB = solidColor(random, cp, dark);
            dark = (random.nextInt(50) - 25) / 255f;
            handR = solidColor(random, cp, dark);
            handG = solidColor(random, cp, dark);
            handB = solidColor(random, cp, dark);
            dark = (random.nextInt(50) - 25) / 255f;
            glovesR = solidColor(random, cp, dark);
            glovesG = solidColor(random, cp, dark);
            glovesB = solidColor(random, cp, dark);
            dark = (random.nextInt(50) - 25) / 255f;
            legR = solidColor(random, cp, dark);
            legG = solidColor(random, cp, dark);
            legB = solidColor(random, cp, dark);
            dark = (random.nextInt(50) - 25) / 255f;
            bootR = solidColor(random, cp, dark);
            bootG = solidColor(random, cp, dark);
            bootB = solidColor(random, cp, dark);
        }
        headT = random.nextInt(m.head.length);
        eyesT = random.nextInt(m.eyes.length);
        hairT = random.nextInt(m.hair.length + 1) - 1;
        beardT = random.nextInt(m.beard.length + 1) - 1;
        propT = random.nextInt(m.prop.length + 1) - 1;
        hatT = random.nextInt(m.hat.length + 1) - 1;
        dressT = random.nextInt(2) - 1;
        bodyT = 0;
        handT = 0;
        glovesT = 0;
        legT = 0;
        bootT = 0;
        //tattoYou(); // !
        //wilson();
        for (int i = 0; i < 4; i++) {
            if (headT != -1) {
                head[i] = colorize(m.head[headT][i], skinR, skinG, skinB);
            }
            if (hatT != -1) {
                hat[i] = colorize(m.hat[hatT][i], hatR, hatG, hatB);
            }
            if (propT != -1) {
                prop[i] = colorize(m.prop[propT][i], propR, propG, propB);
            }
            if (eyesT != -1) {
                eyes[i] = colorize(m.eyes[eyesT][i], eyesR, eyesG, eyesB);
            }
            if (hairT != -1) {
                hair[i] = colorize(m.hair[hairT][i], hairR, hairG, hairB);
            }
            if (beardT != -1) {
                beard[i] = colorize(m.beard[beardT][i], beardR, beardG, beardB);
            }
            if (bootT != -1) {
                boot[i] = colorize(m.boot[bootT][i], bootR, bootG, bootB);
            }
            if (glovesT != -1) {
                gloves[i] = colorize(m.gloves[glovesT][i], glovesR, glovesG, glovesB);
            }
        }

        prerenderAnimation("walk");
        prerenderAnimation("Rwalk");
        prerenderAnimation("run");
    }
    public void wilson() { // !

        skinR = 255 / 255f;
        skinG = 255 / 255f;
        skinB = 255 / 255f;


        hairR = 52/ 255f;
        hairG = 52/ 255f;
        hairB = 52/ 255f;
        eyesR = 78 / 255f;
        eyesG = 78 / 255f;
        eyesB = 78 / 255f;

        beardR = 52 / 255f;
        beardG = 52 / 255f;
        beardB = 52 / 255f;

        propR = 1;
        propG = 1;
        propB = 1;
        hatR = 1 / 255f;
        hatG = 1 / 255f;
        hatB = 1 / 255f;
        dressR = 1 / 255f;
        dressG = 1 / 255f;
        dressB = 1 / 255f;
        bodyR = 171 / 255f;
        bodyG = 64 / 255f;
        bodyB = 61 / 255f;
        handR = 245 / 255f;
        handG = 245 / 255f;
        handB = 245 / 255f;
        glovesR = 5 / 255f;
        glovesG = 5 / 255f;
        glovesB = 5 / 255f;
        legR = 2 / 255f;
        legG = 2 / 255f;
        legB = 2 / 255f;
        bootR = 1 / 255f;
        bootG = 1 / 255f;
        bootB = 1 / 255f;

        headT = 0;
        eyesT = 1;
        hairT = 0;
        beardT = -1;
        propT = -1;
        hatT = -1;
        dressT = -1;
        bodyT = 0;
        handT = 0;
        glovesT = 0;
        legT = 0;
        bootT = 0;
    }
    public void tattoYou() { // !

        skinR = 255 / 255f;
        skinG = 223 / 255f;
        skinB = 181 / 255f;


        hairR = 1;
        hairG = 1;
        hairB = 1;
        eyesR = 35 / 255f;
        eyesG = 9 / 255f;
        eyesB = 13 / 255f;

        beardR = 248 / 255f;
        beardG = 248 / 255f;
        beardB = 235 / 255f;

        propR = 1;
        propG = 1;
        propB = 1;
        hatR = 149 / 255f;
        hatG = 68 / 255f;
        hatB = 78 / 255f;
        dressR = 92 / 255f;
        dressG = 122 / 255f;
        dressB = 94 / 255f;
        bodyR = 220 / 255f;
        bodyG = 206 / 255f;
        bodyB = 220 / 255f;
        handR = 104 / 255f;
        handG = 126 / 255f;
        handB = 105 / 255f;
        glovesR = 170 / 255f;
        glovesG = 145 / 255f;
        glovesB = 170 / 255f;
        legR = 135 / 255f;
        legG = 88 / 255f;
        legB = 96 / 255f;
        bootR = 175 / 255f;
        bootG = 148 / 255f;
        bootB = 146 / 255f;

        headT = 0;
        eyesT = 1;
        hairT = 0;
        beardT = 1;
        propT = -1;
        hatT = 1;
        dressT = 0;
        tq=12;
        bodyT = 0;
        handT = 0;
        glovesT = 0;
        legT = 0;
        bootT = 0;
    }

    public void prerenderAnimation(String name) {
        float step = 5;
        int w = 80, h = 80;
        Texture texture;
        Pixmap pixmap;
        for (int id = 0; id < 4; id++) {
            int dir = id;
            for (int i = 0; i < m.animations.get(name).r.length; i++) {
                float[] struct = new float[m.animations.get(name).r[i].length];
                for (int j = 0; j < struct.length; j++) {
                    struct[j] = m.animations.get(name).r[i][j];
                }

                if (id == 1 || id == 3) {
                    struct[0] = -m.animations.get(name).r[i][0];
                    struct[1] = -m.animations.get(name).r[i][2];
                    struct[2] = -m.animations.get(name).r[i][1];
                    struct[3] = -m.animations.get(name).r[i][4];
                    struct[4] = -m.animations.get(name).r[i][3];
                    struct[5] = -m.animations.get(name).r[i][6];
                    struct[6] = -m.animations.get(name).r[i][5];
                    struct[7] = -m.animations.get(name).r[i][8];
                    struct[8] = -m.animations.get(name).r[i][7];
                    struct[9] = -m.animations.get(name).r[i][10];
                    struct[10] = -m.animations.get(name).r[i][9];
                    struct[11] = -m.animations.get(name).r[i][12];
                    struct[12] = -m.animations.get(name).r[i][11];
                    struct[13] = -m.animations.get(name).r[i][13];
                }
                float cx = w / 2f, cy = h / 2f + h / 4.2f; // Главная точка тела
                float nx = cx + step * 3.5f * m.sin(struct[0]), ny = cy + step * 3.5f * m.cos(struct[0]); // Шея
                float lpx = nx + step * 1.2f * m.sin(struct[0] + 90), lpy = ny + step * 1.2f * m.cos(struct[0] + 90); // Левое плечо
                float rpx = nx + step * 1.2f * m.sin(struct[0] - 90), rpy = ny + step * 1.2f * m.cos(struct[0] - 90); // Правое плечо
                if (dir == 0 || dir == 3) {
                    lpx = nx + step * 0.7f * m.sin(struct[0] + 90);
                    lpy = ny + step * 0.7f * m.cos(struct[0] + 90); // Левое плечо
                    rpx = nx + step * 0.7f * m.sin(struct[0] - 90);
                    rpy = ny + step * 0.7f * m.cos(struct[0] - 90); // Правое плечо
                }
                float lbx = cx + step / 1.35f * m.sin(struct[0] + 90), lby = cy + step / 1.5f * m.cos(struct[0] + 90); // Левое бедро
                float rbx = cx + step / 1.35f * m.sin(struct[0] - 90), rby = cy + step / 1.5f * m.cos(struct[0] - 90); // Правое бедро
                if (dir == 0 || dir == 3) {
                    lbx = cx + step / 1.5f * m.sin(struct[0] + 90);
                    lby = cy + step / 1.5f * m.cos(struct[0] + 90); // Левое бедро
                    rbx = cx + step / 1.5f * m.sin(struct[0] - 90);
                    rby = cy + step / 1.5f * m.cos(struct[0] - 90); // Правое бедро
                }
                float hx = nx + step * 0.7f * m.sin(struct[13]), hy = ny + step * 0.7f * m.cos(struct[13]); // Голова
                float lhx = lpx + step * 2 * m.sin(struct[7]), lhy = lpy + step * 2 * m.cos(struct[7]); // Левый локоть
                float rhx = rpx + step * 2 * m.sin(struct[8]), rhy = rpy + step * 2 * m.cos(struct[8]); // Правый локоть
                float lgx = lhx + step * 2 * m.sin(struct[9]), lgy = lhy + step * 2 * m.cos(struct[9]), lgt; // Левая кисть
                float rgx = rhx + step * 2 * m.sin(struct[10]), rgy = rhy + step * 2 * m.cos(struct[10]), rgt; // Правая кисть
                float lix = lbx + step * 2 * m.sin(struct[1]), liy = lby + step * 2 * m.cos(struct[1]); // Левое колено
                float rix = rbx + step * 2 * m.sin(struct[2]), riy = rby + step * 2 * m.cos(struct[2]); // Правое колено
                float lfx = lix + step * 2 * m.sin(struct[3]), lfy = liy + step * 2 * m.cos(struct[3]), lft; // Левая стопа
                float rfx = rix + step * 2 * m.sin(struct[4]), rfy = riy + step * 2 * m.cos(struct[4]), rft; // Правая стопа
                pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
                pixmap.setColor(skinR * 0.4f, skinG * 0.4f, skinB * 0.4f, 1);
                drawLine(pixmap, nx, ny, hx, hy, 3);
                if (dir == 3) {
                    drawLeftHand(pixmap, nx, ny, lpx, lpy, rpx, rpy, lhx, lhy, rhx, rhy, lgx, lgy, rgx, rgy);
                }
                if (dir == 0) {
                    drawRightHand(pixmap, nx, ny, lpx, lpy, rpx, rpy, lhx, lhy, rhx, rhy, lgx, lgy, rgx, rgy);
                }
                if (dir == 2) {
                    drawHands(pixmap, nx, ny, lpx, lpy, rpx, rpy, lhx, lhy, rhx, rhy, lgx, lgy, rgx, rgy);
                }
                float scale = 1;
                if (dir == 3 || dir == 0) {
                    scale = 0.7f;
                }
                // ГРУДЬ
                if (bodyT == -1) {
                    pixmap.setColor(skinR * 0.7f, skinG * 0.7f, skinB * 0.7f, 1);
                    drawLine(pixmap, cx, cy, nx, ny, 3);
                    pixmap.setColor(skinR * 0.75f, skinG * 0.75f, skinB * 0.75f, 1);
                    drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.65f, ny + (cy - ny) * 0.65f, 4 * scale);

                    pixmap.setColor(skinR * 0.77f, skinG * 0.77f, skinB * 0.77f, 1);
                    drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.6f, ny + (cy - ny) * 0.6f, 5 * scale);

                    pixmap.setColor(skinR * 0.8f, skinG * 0.8f, skinB * 0.8f, 1);
                    drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.5f, ny + (cy - ny) * 0.5f, 6 * scale);

                    pixmap.setColor(skinR * 0.85f, skinG * 0.85f, skinB * 0.85f, 1);
                    drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.45f, ny + (cy - ny) * 0.45f, 7 * scale);

                    pixmap.setColor(skinR * 0.9f, skinG * 0.9f, skinB * 0.9f, 1);
                    drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.42f, ny + (cy - ny) * 0.42f, 8 * scale);

                    pixmap.setColor(skinR * 0.95f, skinG * 0.95f, skinB * 0.95f, 1);
                    drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.4f, ny + (cy - ny) * 0.4f, 9 * scale);

                    pixmap.setColor(skinR * 0.97f, skinG * 0.97f, skinB * 0.97f, 1);
                    drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.3f, ny + (cy - ny) * 0.3f, 12 * scale);

                    pixmap.setColor(skinR * 0.8f, skinG * 0.8f, skinB * 0.8f, 1);
                    drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.35f, ny + (cy - ny) * 0.35f, 1);

                    //ЖИВОТ
                    pixmap.setColor(skinR * 0.55f, skinG * 0.55f, skinB * 0.55f, 1);
                    drawLine(pixmap, cx, cy, cx + (nx - cx) * 0.2f, cy + (ny - cy) * 0.2f, 4 * scale);
                } else {
                    if (bodyT == 0) {
                        pixmap.setColor(bodyR * 0.7f, bodyG * 0.7f, bodyB * 0.7f, 1);
                        drawLine(pixmap, cx, cy, nx, ny, 4);

                        pixmap.setColor(bodyR * 0.75f, bodyG * 0.75f, bodyB * 0.75f, 1);
                        drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.65f, ny + (cy - ny) * 0.65f, 5 * scale);

                        pixmap.setColor(bodyR * 0.8f, bodyG * 0.8f, bodyB * 0.8f, 1);
                        drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.5f, ny + (cy - ny) * 0.5f, 6 * scale);

                        pixmap.setColor(bodyR * 0.85f, bodyG * 0.85f, bodyB * 0.85f, 1);
                        drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.45f, ny + (cy - ny) * 0.45f, 7 * scale);

                        pixmap.setColor(bodyR * 0.9f, bodyG * 0.9f, bodyB * 0.9f, 1);
                        drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.42f, ny + (cy - ny) * 0.42f, 8 * scale);

                        pixmap.setColor(bodyR * 0.95f, bodyG * 0.95f, bodyB * 0.95f, 1);
                        drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.4f, ny + (cy - ny) * 0.4f, 9 * scale);

                        pixmap.setColor(bodyR * 0.97f, bodyG * 0.97f, bodyB * 0.97f, 1);
                        drawLine(pixmap, nx, ny, nx + (cx - nx) * 0.3f, ny + (cy - ny) * 0.3f, 12 * scale);

                        //ЖИВОТ
                        pixmap.setColor(bodyR * 0.65f, bodyG * 0.65f, bodyB * 0.65f, 1);
                        drawLine(pixmap, cx, cy, cx + (nx - cx) * 0.2f, cy + (ny - cy) * 0.2f, 5 * scale);
                    }
                }
                // НОГИ
                pixmap.setColor(legR * 0.9f, legG * 0.9f, legB * 0.9f, 1);
                drawLine(pixmap, cx, cy, lbx, lby, 3);
                drawLine(pixmap, cx, cy, rbx, rby, 3);
                pixmap.setColor(legR * 0.85f, legG * 0.85f, legB * 0.85f, 1);
                drawLine(pixmap, cx, cy, cx + (lbx - cx) * 0.25f, cy + (lby - cy) * 0.25f, 4);
                drawLine(pixmap, cx, cy, cx + (rbx - cx) * 0.25f, cy + (rby - cy) * 0.25f, 4);
                pixmap.setColor(legR * 0.95f, legG * 0.95f, legB * 0.95f, 1);
                drawLine(pixmap, lbx, lby, lix, liy, 4);
                drawLine(pixmap, rbx, rby, rix, riy, 4);
                drawMuscle(pixmap, lbx, lby, lix, liy, 5, 0.6f);
                drawMuscle(pixmap, rbx, rby, rix, riy, 5, 0.6f);
                pixmap.setColor(legR * 0.75f, legG * 0.75f, legB * 0.75f, 1);
                drawLine(pixmap, lix, liy, lfx, lfy, 2);
                drawLine(pixmap, rix, riy, rfx, rfy, 2);

                // РУКИ
                if (dir == 1) {
                    drawHands(pixmap, nx, ny, lpx, lpy, rpx, rpy, lhx, lhy, rhx, rhy, lgx, lgy, rgx, rgy);
                }
                if (dir == 0) {
                    drawLeftHand(pixmap, nx, ny, lpx, lpy, rpx, rpy, lhx, lhy, rhx, rhy, lgx, lgy, rgx, rgy);
                }
                if (dir == 3) {
                    drawRightHand(pixmap, nx, ny, lpx, lpy, rpx, rpy, lhx, lhy, rhx, rhy, lgx, lgy, rgx, rgy);
                }

                drawPixmap(pixmap, head[dir].getTextureData().consumePixmap(), hx, hy, 16, 8, struct[13]);
                drawPixmap(pixmap, eyes[dir].getTextureData().consumePixmap(), hx, hy, 16, 8, struct[13]);
                if (beardT != -1) {
                    drawPixmap(pixmap, beard[dir].getTextureData().consumePixmap(), hx, hy, 16, 8, struct[13]);
                }

                if (hairT != -1 && hatT == -1) {
                    drawPixmap(pixmap, hair[dir].getTextureData().consumePixmap(), hx, hy, 16, 9, struct[13]);
                }
                if (propT != -1) {
                    drawPixmap(pixmap, prop[dir].getTextureData().consumePixmap(), hx, hy, 16, 8, struct[13]);
                }
                if (hatT != -1) {
                    drawPixmap(pixmap, hat[dir].getTextureData().consumePixmap(), hx, hy, 16, 9, struct[13]);
                }
                if (bootT != -1) {
                    drawPixmap(pixmap, boot[dir].getTextureData().consumePixmap(), lfx + m.sin(struct[3]) * step, lfy + m.cos(struct[3]) * step, 8, 8, struct[3] + 180);
                    drawPixmap(pixmap, boot[dir].getTextureData().consumePixmap(), rfx + m.sin(struct[4]) * step, rfy + m.cos(struct[4]) * step, 8, 8, struct[4] + 180);
                }
                if (glovesT != -1) {
                    drawPixmap(pixmap, gloves[dir].getTextureData().consumePixmap(), lgx + m.sin(struct[9]) * step, lgy + m.cos(struct[9]) * step, 8, 8, struct[9] + 180 - 10);
                    drawPixmap(pixmap, gloves[dir].getTextureData().consumePixmap(), rgx + m.sin(struct[10]) * step, rgy + m.cos(struct[10]) * step, 8, 8, struct[10] + 180 + 10);
                }
                texture = new Texture(shaderize(pixmap));
                anim.get(id).add(texture);
                pixmap.dispose();
            }
        }
    }

    public void drawHands(Pixmap pixmap, float nx, float ny, float lpx, float lpy, float rpx, float rpy, float lhx, float lhy, float rhx, float rhy, float lgx, float lgy, float rgx, float rgy) {
        if (bodyT == -1) {
            pixmap.setColor(skinR, skinG, skinB, 1); // ПЛЕЧИ
            drawLine(pixmap, nx, ny, lpx, lpy, 3);
            drawLine(pixmap, nx, ny, rpx, rpy, 3);
        } else {
            pixmap.setColor(bodyR, bodyG, bodyB, 1); // ПЛЕЧИ
            drawLine(pixmap, nx, ny, lpx, lpy, 3);
            drawLine(pixmap, nx, ny, rpx, rpy, 3);
        }
        pixmap.setColor(handR * 0.95f, handG * 0.95f, handB * 0.95f, 1); // МУСКУЛЫ НА ПЛЕЧАХ
        drawLine(pixmap, lpx, lpy, lpx + (lhx - lpx) * 0.2f, lpy + (lhy - lpy) * 0.2f, 5);
        drawLine(pixmap, rpx, rpy, rpx + (rhx - rpx) * 0.2f, rpy + (rhy - rpy) * 0.2f, 5);
        pixmap.setColor(handR * 0.95f, handG * 0.95f, handB * 0.95f, 1);
        drawLine(pixmap, lpx, lpy, lhx, lhy, 3);
        drawLine(pixmap, rpx, rpy, rhx, rhy, 3);
        pixmap.setColor(handR * 0.9f, handG * 0.9f, handB * 0.9f, 1);
        drawMuscle(pixmap, lpx, lpy, lhx, lhy, 5, 0.7f);
        drawMuscle(pixmap, rpx, rpy, rhx, rhy, 5, 0.7f);
        pixmap.setColor(handR * 0.8f, handG * 0.8f, handB * 0.8f, 1);
        drawLine(pixmap, lhx, lhy, lgx, lgy, 2);
        drawLine(pixmap, rhx, rhy, rgx, rgy, 2);
    }

    public void drawLeftHand(Pixmap pixmap, float nx, float ny, float lpx, float lpy, float rpx, float rpy, float lhx, float lhy, float rhx, float rhy, float lgx, float lgy, float rgx, float rgy) {
        if (bodyT == -1) {
            pixmap.setColor(skinR, skinG, skinB, 1); // ПЛЕЧИ
            drawLine(pixmap, nx, ny, lpx, lpy, 3);
        } else {
            pixmap.setColor(bodyR, bodyG, bodyB, 1); // ПЛЕЧИ
            drawLine(pixmap, nx, ny, lpx, lpy, 3);
        }
        pixmap.setColor(handR * 0.95f, handG * 0.95f, handB * 0.95f, 1); // МУСКУЛЫ НА ПЛЕЧАХ
        drawLine(pixmap, lpx, lpy, lpx + (lhx - lpx) * 0.2f, lpy + (lhy - lpy) * 0.2f, 5);
        pixmap.setColor(handR * 0.95f, handG * 0.95f, handB * 0.95f, 1);
        drawLine(pixmap, lpx, lpy, lhx, lhy, 3);
        pixmap.setColor(handR * 0.9f, handG * 0.9f, handB * 0.9f, 1);
        drawMuscle(pixmap, lpx, lpy, lhx, lhy, 5, 0.7f);
        pixmap.setColor(handR * 0.8f, handG * 0.8f, handB * 0.8f, 1);
        drawLine(pixmap, lhx, lhy, lgx, lgy, 2);
    }

    public void drawRightHand(Pixmap pixmap, float nx, float ny, float lpx, float lpy, float rpx, float rpy, float lhx, float lhy, float rhx, float rhy, float lgx, float lgy, float rgx, float rgy) {
        if (bodyT == -1) {
            pixmap.setColor(skinR, skinG, skinB, 1); // ПЛЕЧИ
            drawLine(pixmap, nx, ny, rpx, rpy, 3);
        } else {
            pixmap.setColor(bodyR, bodyG, bodyB, 1); // ПЛЕЧИ
            drawLine(pixmap, nx, ny, rpx, rpy, 3);
        }
        pixmap.setColor(handR * 0.95f, handG * 0.95f, handB * 0.95f, 1); // МУСКУЛЫ НА ПЛЕЧАХ
        drawLine(pixmap, rpx, rpy, rpx + (rhx - rpx) * 0.2f, rpy + (rhy - rpy) * 0.2f, 5);
        pixmap.setColor(handR * 0.95f, handG * 0.95f, handB * 0.95f, 1);
        drawLine(pixmap, rpx, rpy, rhx, rhy, 3);
        pixmap.setColor(handR * 0.9f, handG * 0.9f, handB * 0.9f, 1);
        drawMuscle(pixmap, rpx, rpy, rhx, rhy, 5, 0.7f);
        pixmap.setColor(handR * 0.8f, handG * 0.8f, handB * 0.8f, 1);
        drawLine(pixmap, rhx, rhy, rgx, rgy, 2);
    }

    public Pixmap shaderize(Pixmap pixmap) {
        int mw = pixmap.getWidth();
        int mh = pixmap.getHeight();
        float r, g, b;
        float d = 5;
        for (int x = 0; x < mw; x++) {
            int fired = 0;
            for (int y = mh - 1; y >= 0; y--) {
                Color c = new Color(pixmap.getPixel(x, y));
                if (c.a != 0) {
                    fired += 1;
                    r = Math.max(c.r - 0.05f / fired * d, 0);
                    g = Math.max(c.g - 0.05f / fired * d, 0);
                    b = Math.max(c.b - 0.05f / fired * d, 0);
                    pixmap.setColor(new Color(r, g, b, c.a));
                    pixmap.drawPixel(x, y);
                } else {
                    fired = 0;
                }
            }
        }
        for (int x = 0; x < mw; x++) {
            int fired = 0;
            for (int y = 0; y < mh; y++) {
                Color c = new Color(pixmap.getPixel(x, y));
                if (c.a != 0) {
                    fired += 1;
                    r = Math.max(c.r + 0.025f / fired * d, 0);
                    g = Math.max(c.g + 0.025f / fired * d, 0);
                    b = Math.max(c.b + 0.025f / fired * d, 0);
                    pixmap.setColor(new Color(r, g, b, c.a));
                    pixmap.drawPixel(x, y);
                } else {
                    fired = 0;
                }
            }
        }
        return pixmap;
    }

    public void drawPixmap(Pixmap p, Pixmap preference, float px, float py, float ox, float oy, float rotate) {
        int cx = (int) (px + ox * m.sin(rotate)), cy = (int) (py + oy * m.cos(rotate));
        int mw = preference.getWidth();
        int mh = preference.getHeight();
        for (int x = 0; x < mw; x++) {
            for (int y = 0; y < mh; y++) {
                float ix = x - mw / 2f;
                float iy = y - mh / 2f;
                p.setColor(new Color(preference.getPixel(x, y)));
                p.drawRectangle(cx + (int) (m.cos(rotate) * ix + m.cos(rotate - 90) * iy), cy + (int) (m.sin(rotate) * ix + m.sin(rotate - 90) * iy), 1, 1);
            }
        }
    }

    /*public void drawLocon(Pixmap pixmap, float x, float y, float r, float g, float b, float a, float s){
        float ix = x;
        float iy = y;
        for(float is=0;is<s;is++){

            ix+=(m.random.nextInt(3)-1)/10f;
            iy+=1;
            Color c = new Color(pixmap.getPixel((int)ix, (int)iy));
            if(c.a!=0){
                ix+=m.random.nextInt(3)-1;
            }
            pixmap.setColor(Math.max(r-is/100f+(m.random.nextInt(5)-2)/80f,0),Math.max(g-is/100f+(m.random.nextInt(5)-2)/80f,0),Math.max(b-is/100f+(m.random.nextInt(5)-2)/80f,0),a);
            pixmap.drawPixel((int)ix, (int)iy);
        }
    }*/
    public void drawLine(Pixmap pixmap, float x1, float y1, float x2, float y2, float s) {
        for (int is = 0; is < s; is++) {
            pixmap.drawLine((int) x1, (int) (y1 + is / 2f), (int) x2, (int) (y2 + is / 2f));
            pixmap.drawLine((int) x1, (int) (y1 - is / 2f), (int) x2, (int) (y2 - is / 2f));
            pixmap.drawLine((int) (x1 + is / 2f), (int) y1, (int) (x2 + is / 2f), (int) y2);
            pixmap.drawLine((int) (x1 - is / 2f), (int) y1, (int) (x2 - is / 2f), (int) y2);
        }
    }

    public void drawMuscle(Pixmap pixmap, float px1, float py1, float px2, float py2, float s, float k) {
        float x1 = px1 * (1f - k) + px2 * k;
        float x2 = px1 * k + px2 * (1f - k);
        float y1 = py1 * (1f - k) + py2 * k;
        float y2 = py1 * k + py2 * (1f - k);
        for (int is = 0; is < s; is++) {
            pixmap.drawLine((int) x1, (int) (y1 + is / 2f), (int) x2, (int) (y2 + is / 2f));
            pixmap.drawLine((int) x1, (int) (y1 - is / 2f), (int) x2, (int) (y2 - is / 2f));
            pixmap.drawLine((int) (x1 + is / 2f), (int) y1, (int) (x2 + is / 2f), (int) y2);
            pixmap.drawLine((int) (x1 - is / 2f), (int) y1, (int) (x2 - is / 2f), (int) y2);
        }
    }

    public float color(Random random, int a, int b) { // Генерация цвета с ограничениями
        return (random.nextInt(a) + b) / 255f;
    }

    public float solidColor(Random random, float[] cp, float dark) { // Генерация цвета с ограничениями
        return minmax(random.nextInt((int) (cp[random.nextInt(cp.length)] * 255f)) / 255f + dark, 0, 1);
    }

    public Texture colorize(Texture texture, float r, float g, float b) { // Разукрасить картинку в цвет
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        float w = pixmap.getWidth();
        float h = pixmap.getHeight();
        float medium;
        float normal = 0;
        Color c;
        int q = 0;
        for (int ix = 0; ix < w; ix++) {
            for (int iy = 0; iy < h; iy++) {
                c = new Color(pixmap.getPixel(ix, iy));
                if (c.a != 0) {
                    normal += (c.r + c.g + c.b) / 3f;
                    q++;
                }
            }
        }
        normal /= q;
        for (int ix = 0; ix < w; ix++) {
            for (int iy = 0; iy < h; iy++) {
                c = new Color(pixmap.getPixel(ix, iy));
                medium = (c.r + c.g + c.b) / 3f;
                medium -= (normal - medium) / 5f;
                pixmap.setColor(medium * r, medium * g, medium * b, c.a);
                pixmap.drawPixel(ix, iy);
            }
        }
        return new Texture(pixmap);
    }

    public float minmax(float v, float min, float max) {
        return Math.min(Math.max(v, min), max);
    }
}
