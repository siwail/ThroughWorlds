package com.throughworlds;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

public class Person extends Entity {
    float cr, cg, cb;
    float r = 0;
    float mx, my, mz;
    float health = 0;
    float lastHealth = 0;
    float maxHealth = 100;

    String name = "";
    int nameT; // Тип имени (шутливое, сгенерированное)

    float skinR, skinG, skinB; // Цвет кожи

    float eyeR, eyeG, eyeB; // Цвет глаз
    int eyeT; // Тип глаз

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

    float gloverR, gloverG, gloverB; // Цвет перчаток
    int gloverT; // Тип перчаток

    float legR, legG, legB; // Цвет штанов
    int legT; // Тип штанов

    float bootR, bootG, bootB; // Цвет обуви
    int bootT; // Тип обуви

    public Person(Main m, float x, float y, float z) {
        super(m, x, y, z);
        cr = 1;
        cg = 0.9f;
        cb = 0.9f;
        s = 60;
    }

    public void math() {
        x += vx;
        y += vy;
        z += vz;

        vz -= 0.4f;
        vx /= 1.05f;
        vy /= 1.05f;

        if (z <= 0) {
            if (Math.abs(vz) > 20) {
                health -= Math.abs(vz / 15f);
                vx += (m.random.nextInt(3) - 1) * vz / 4f;
                vy += (m.random.nextInt(3) - 1) * vz / 4f;
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
        lastHealth += (health - lastHealth) / 40f;
        mx += (x - mx) / 3f;
        my += (y - my) / 3f;
        mz += (z - mz) / 3f;
        r = (mz * 1.5f);
        m.batch.draw(m.shadow, -cx + mx - s - mz / 12f, -cy + my - s - s / 3f - mz / 12f, s * 2 + mz / 6f, s * 2 + mz / 6f);
        m.batch.setColor(cr, cg, cb, 1);
        m.batch.draw(new TextureRegion(m.cat), -cx + mx - s + vz / 2f - Math.abs(vx) / 2f, -cy + my - s + mz, (s * 2 - vz + Math.abs(vx)) / 2f, (s * 2 + vz) / 2f, s * 2 - vz + Math.abs(vx), s * 2 + vz, 1, 1, r);
        m.batch.setColor(1, 1, 1, 1);

    }

    public void drawMirror() {
        float cx = m.cx, cy = m.cy;

        //m.batch.draw(m.shadow, -cx+mx-s-mz/12f, -cy+my-s-s/3f-mz/12f, s*2+mz/6f,s*2+mz/6f);
        m.batch.setColor(cr, cg, cb, 0.45f);
        m.batch.draw(new TextureRegion(m.cat), -cx + mx - s + vz / 2f - Math.abs(vx) / 2f, -cy + my - s - mz - (s * 2 + vz) * 0.8f, (s * 2 - vz + Math.abs(vx)) / 2f, (s * 2 + vz) / 2f, s * 2 - vz + Math.abs(vx), s * 2 + vz, 1, -1, -r);
        m.batch.setColor(1, 1, 1, 1);

    }

    public void drawBar() {
        float bs = s * 2.45f;
        float cx = m.cx, cy = m.cy;
        float bx = -cx + x, by = -cy + y + z + s;
        m.batch.end();
        m.drawer.begin(ShapeRenderer.ShapeType.Filled);

        if (nameT == 0) {
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


        m.drawer.end();
        m.batch.begin();
        if (nameT == 0) {
            String str = name;
            int size = 4;
            if (name.length() < 7) {
                size = 5;
            }
            if (name.length() > 16) {
                size = 3;
            }
            for (int i = 0; i < str.length(); i++) {
                m.font[size].setColor((cr / 2f + 0.25f) * 1.45f, (cg / 2f + 0.25f) * 1.45f, (cb / 2f + 0.25f) * 1.45f, 1);
                m.font[size].draw(m.batch, str.substring(i, i + 1), bx - bs / 1.5f + i * bs / 0.75f / str.length(), by + m.sin(m.rotateAnim + i * 10) + bs / 7.4f);
            }
        } else {
            String str = name;
            int size = 4;
            if (name.length() < 7) {
                size = 6;
            }
            if (name.length() > 16) {
                size = 3;
            }
            for (int i = 0; i < str.length(); i++) {
                float d = m.sin(m.rotateAnim + 30 * i) / 4f;
                m.font[size].setColor((cr / 1.25f + 0.1f + d / 2f) * 1.55f, (cg / 1.25f + 0.1f + d) * 1.55f, (cb / 1.25f + 0.1f + d / 5f) * 1.55f, 1);
                m.font[size].draw(m.batch, str.substring(i, i + 1), bx - bs / 1.5f + i * bs / 0.75f / str.length(), by + m.cos(m.rotateAnim + i * 15) * 4 + bs / 5f);
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
            skinR = color(random, 210, 15);
            skinG = color(random, 210, 15);
            skinB = color(random, 210, 15);
            hairR = color(random, 210, 15);
            hairG = color(random, 210, 15);
            hairB = color(random, 210, 15);
            eyeR = color(random, 210, 15);
            eyeG = color(random, 210, 15);
            eyeB = color(random, 210, 15);
            beardR = color(random, 210, 15);
            beardG = color(random, 210, 15);
            beardB = color(random, 210, 15);
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
            gloverR = color(random, 210, 15);
            gloverG = color(random, 210, 15);
            gloverB = color(random, 210, 15);
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
            float dark;

            dark = (random.nextInt(50) - 25) / 255f;
            skinR = solidColor(random, cp, dark);
            skinG = solidColor(random, cp, dark);
            skinB = solidColor(random, cp, dark);
            dark = (random.nextInt(50) - 25) / 255f;
            hairR = solidColor(random, cp, dark);
            hairG = solidColor(random, cp, dark);
            hairB = solidColor(random, cp, dark);
            dark = (random.nextInt(50) - 25) / 255f;
            eyeR = solidColor(random, cp, dark);
            eyeG = solidColor(random, cp, dark);
            eyeB = solidColor(random, cp, dark);
            dark = (random.nextInt(50) - 25) / 255f;
            beardR = solidColor(random, cp, dark);
            beardG = solidColor(random, cp, dark);
            beardB = solidColor(random, cp, dark);
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
            gloverR = solidColor(random, cp, dark);
            gloverG = solidColor(random, cp, dark);
            gloverB = solidColor(random, cp, dark);
            dark = (random.nextInt(50) - 25) / 255f;
            legR = solidColor(random, cp, dark);
            legG = solidColor(random, cp, dark);
            legB = solidColor(random, cp, dark);
            dark = (random.nextInt(50) - 25) / 255f;
            bootR = solidColor(random, cp, dark);
            bootG = solidColor(random, cp, dark);
            bootB = solidColor(random, cp, dark);
        }

        eyeT = 0;
        hairT = 0;
        beardT = 0;
        propT = 0;
        hatT = 0;
        dressT = 0;
        bodyT = 0;
        handT = 0;
        gloverT = 0;
        legT = 0;
        bootT = 0;
    }

    public float color(Random random, int a, int b) { // Генерация цвета с ограничениями
        return (random.nextInt(a) + b) / 255f;
    }

    public float solidColor(Random random, float[] cp, float dark) { // Генерация цвета с ограничениями
        return Math.min(Math.max(((random.nextInt((int) cp[random.nextInt(cp.length)])) / 255f + dark), 0), 1);
    }
}
