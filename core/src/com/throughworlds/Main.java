package com.throughworlds;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class Main extends ApplicationAdapter {
    Random random = new Random(); // Создание генератора случайностей
    SpriteBatch batch; // Создание отрисовщика картинок
    ShapeRenderer drawer; // Создание отрисовщика фигур
    float cx = 0, cy = 0; // Позиция камеры
    float cR = 0; // Поворот камеры
    float ca = 0.66f; // Наклон камеры
    float cAccelerate = 0.6f; // Ускорение камеры
    float vx = 0, vy = 0;
    float sunRotate = 90;
    float w, h; // Ширина, длина экрана
    int FX = 1000, FY = 600, FZ = 6; // Размеры сетки локации (в клетках)
    int segments = 100;
    Field[][][] F = new Field[FX][FY][FZ]; // Сетка локации
    Texture[][][] S = new Texture[FX / segments][FY / segments][FZ]; // Текстуры локации
    float s = 5; // Размер одной клетки в сетке
    WaterPulse[] waterPulses = new WaterPulse[25];
    Particle[] parts = new Particle[25];
    Visual[] visuals = new Visual[100];
    Cloud[] clouds = new Cloud[10];
    Flower[] flowers = new Flower[1000];
    Main m;
    int seed; // Сеня генератор мира
    Color waterColor, dirtColor, sunColor;
    float wetRate, landRate, rainRate, sunRadius = 25, cloudsWidth = 100, cloudsHeight = 15;
    float windX, windY;
    float gameSpeed = 1;
    int mode = 0;
    float goW = 0, goA = 0, goS = 0, goD = 0, goUp = 0, goDown = 0;

    @Override
    public void create() {
        m = this;
        batch = new SpriteBatch();
        drawer = new ShapeRenderer();
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        seed = random.nextInt(1000);

        generateLocation(seed);


        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == 51) {
                    goW = 1;
                }
                if (keycode == 29) {
                    goA = 1;
                }
                if (keycode == 47) {
                    goS = 1;
                }
                if (keycode == 32) {
                    goD = 1;
                }
                if (keycode == 62) {
                    goUp = 1;
                }
                if (keycode == 129) {
                    goDown = -1;
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == 51) {
                    goW = 0;
                }
                if (keycode == 29) {
                    goA = 0;
                }
                if (keycode == 47) {
                    goS = 0;
                }
                if (keycode == 32) {
                    goD = 0;
                }
                if (keycode == 62) {
                    goUp = 0;
                }
                if (keycode == 129) {
                    goDown = 0;
                }

                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return false;
            }
        });
        Thread math = new Thread(() -> {
            while (true) {
                vx += (goD - goA) * cAccelerate;
                vy += (goW - goS) * cAccelerate;
                cx += vx;
                cy += vy;
                vx /= 1.05f;
                vy /= 1.05f;
                if (random.nextInt(25) == 0) {
                    windX = (random.nextInt(11) - 5) / 20f;
                }
                for (int i = 0; i < waterPulses.length; i += 1) {
                    if (waterPulses[i].t != 0) {
                        waterPulses[i].math();
                    }
                }
                for (int i = 0; i < flowers.length; i += 1) {
                    if (flowers[i].t != 0) {
                        flowers[i].math();
                    }
                }
                for (int i = 0; i < parts.length; i += 1) {
                    if (parts[i].t != 0) {
                        parts[i].math();
                    }
                }
                for (int i = 0; i < clouds.length; i += 1) {
                    if (clouds[i].t != 0) {
                        clouds[i].math();
                    }
                }
                if (random.nextInt((int) (rainRate) + 3) == 0) {
                    for (int i = 0; i < 1; i += 1) {
                        setWaterPart(cx + random.nextInt((int) (w)), cy + random.nextInt((int) (h)));
                    }
                }
                if (random.nextInt(40) == 0) {
                    setCloud(-cloudsWidth - random.nextInt((int) (cloudsWidth / 2f)), random.nextInt((int) (h / s)));
                }
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        math.start();
    }

    public void generateLocation(int seed) {
        Random random = new Random(seed);
        waterColor = new Color((random.nextInt(5) + 10) / 100f, (random.nextInt(5) + 10) / 100f, (random.nextInt(6) + 10) / 100f, (random.nextInt(10) + 65) / 100f);
        dirtColor = new Color((random.nextInt(21) + 18) / 100f, (random.nextInt(22) + 22) / 100f, (random.nextInt(24) + 8) / 100f, 1);
        sunColor = new Color((random.nextInt(22) + 140) / 100f, (random.nextInt(22) + 140) / 100f, (random.nextInt(22) + 140) / 100f, 1);

        wetRate = random.nextInt(FZ);
        rainRate = random.nextInt(3);
        landRate = random.nextInt(3) + 1;
        windX = (random.nextInt(11) - 5) / 20f;
        windY = (random.nextInt(11) - 5) / 20f;
        for (int ix = 0; ix < FX; ix++) {
            for (int iy = 0; iy < FY; iy++) {
                for (int iz = 0; iz < FZ; iz++) {
                    F[ix][iy][iz] = new Field();
                }
            }
        }

        float n = FZ;
        int e = 6;
        for (int i = 0; i < n; i++) {
            if (random.nextInt(3) == 0) {
                dirtColor.r += (random.nextInt(e * 2) - e) / 100f;
                dirtColor.g += (random.nextInt(e * 2) - e) / 100f;
                dirtColor.b += (random.nextInt(e * 2) - e) / 100f;
            }
            int n2 = (int) (FX * FY * landRate * (1 - i / FZ));
            int ix = random.nextInt(FX), iy = random.nextInt(FY);
            float mode = 1, toSwitch = 100;
            for (int i2 = 0; i2 < n2; i2++) {
                toSwitch -= 1;
                if (toSwitch < 1) {
                    toSwitch = random.nextInt(200) + 100;
                    mode = (random.nextInt(30) + 85) / 100f;
                }
                ix = Math.max(Math.min(ix + random.nextInt(3) - 1, FX - 1), 0);
                iy = Math.max(Math.min(iy + random.nextInt(3) - 1, FY - 1), 0);


                F[ix][iy][i].t = 1;
                F[ix][iy][i].r = 0.01f + i / n * dirtColor.r * mode + (random.nextInt(e)) / 200f;
                F[ix][iy][i].g = 0.01f + i / n * dirtColor.g * mode + (random.nextInt(e)) / 200f;
                F[ix][iy][i].b = 0.01f + i / n * dirtColor.b * mode + (random.nextInt(e)) / 200f;

            }
        }
        for (int iz = 0; iz < FZ; iz++) {
            for (int x = 0; x < S.length; x += 1) {
                for (int y = 0; y < S[x].length; y += 1) {
                    Pixmap pixmap = new Pixmap(segments, segments, Pixmap.Format.RGBA8888);
                    for (int iy = y * segments; iy < (y + 1) * segments; iy++) {
                        for (int ix = x * segments; ix < (x + 1) * segments; ix++) {
                            if (F[ix][iy][iz].t == 1) {
                                Field f = F[ix][iy][iz];
                                if (act(ix, iy + 1) && F[ix][iy + 1][iz].t == 0) {
                                    pixmap.setColor(f.r / 1.25f, f.g / 1.25f, f.b / 1.25f, 1);
                                    pixmap.drawPixel(ix - x * segments, segments - 1 - (iy - y * segments) + 1);
                                }
                                pixmap.setColor(f.r, f.g, f.b, 1);
                                pixmap.drawPixel(ix - x * segments, segments - 1 - (iy - y * segments));
                            } else {
                                if (iz == wetRate) {
                                    F[ix][iy][iz].t = 2;
                                    F[ix][iy][iz].m = 0.5f;
                                    F[ix][iy][iz].a = waterColor.a;
                                    F[ix][iy][iz].r = (waterColor.r);
                                    F[ix][iy][iz].g = (waterColor.g);
                                    F[ix][iy][iz].b = (waterColor.b);
                                }
                            }
                        }
                    }
                    S[x][y][iz] = new Texture(pixmap);
                    pixmap.dispose();
                }
            }
        }

        for (int i = 0; i < waterPulses.length; i++) {
            waterPulses[i] = new WaterPulse();
        }
        for (int i = 0; i < parts.length; i += 1) {
            parts[i] = new Particle(this);
        }
        for (int i = 0; i < flowers.length; i++) {
            flowers[i] = new Flower(this);
        }
        for (int i = 0; i < clouds.length; i += 1) {
            clouds[i] = new Cloud(this);
        }
        for (int i = 0; i < flowers.length; i++) {
            float x = random.nextInt((int) (FX * s));
            float y = random.nextInt((int) (FY * s));
            while (!act((int) (x / s), (int) (y / s)) || F[(int) (x / s)][(int) (y / s)][(int) wetRate].t == 2) {
                x = random.nextInt((int) (FX * s));
                y = random.nextInt((int) (FY * s));
            }
            setFlower(x, y);
        }
        for (int i = 0; i < clouds.length; i += 1) {
            setCloud(random.nextInt((int) (w / s)), random.nextInt((int) (h / s)));
        }

        //
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        ScreenUtils.clear(0, 0, 0, 1);
        sunRotate += 0.1f*gameSpeed;
        if (sunRotate > 360) {
            sunRotate -= 360;
        }
        float sunX = (cx + w / 2) / s; // Координата солнца по X
        float sunY = FY - (cy + h / 2) / s + sin(sunRotate) * (h / s); // Координата солнца по Y (Зависит от текущего поворота)
        float sunScale = sin(sunRotate - 90); // Размер солнца
        float sunRadius = (sunScale + 0.5f) * this.sunRadius; // Радиус солнца
        float cx = this.cx, cy = this.cy; // Ускоряем доступ к позиции камеры
        float dark = (sunScale + 1.35f); // Настройки уровня освещённости
        float redTimeColor = 0.12f * Math.max(sunScale, 0);  // Настройки цвета в зависимости от времени суток
        float greenTimeColor = 0.15f * Math.max(sunScale, 0);
        float blueTimeColor = 0.25f * Math.max(1 - sunScale, 0);
        batch.setColor(sunColor.r * dark + redTimeColor, sunColor.g * dark + greenTimeColor, sunColor.b * dark + blueTimeColor, 1);
        batch.begin();
        float waterSplash, waterR, waterG, waterB; // Параметры воды
        Texture waterTexture, dirtTexture; // Заранее объявляем текстуру воды, текстуру земли
        Pixmap dirtPixmap = new Pixmap(FX, FY, Pixmap.Format.RGBA8888), waterPixmap = new Pixmap(FX, FY, Pixmap.Format.RGBA8888); // Создаём пиксмап земли, пиксмап воды
        for (int iz = 0; iz < FZ; iz++) { // Перебор уровней земли
            if (iz == wetRate) { // Выполняется в том случае, если текущий уровень совпадает с уровнем воды
                int minX = Math.max((int) (cx / s), 0);
                int maxY = FY - Math.max((int) (cy / s), 0);
                int maxX = Math.min((int) ((cx + w) / s), FX - 1);
                int minY = FY - Math.min((int) ((cy + h) / s), FY - 1);
                for (int ix = minX; ix < maxX; ix++) {
                    for (int iy = minY; iy < maxY; iy++) {
                        if (F[ix][iy][iz].t == 2) { // Если на данном пикселе расположена вода
                            Field f = F[ix][iy][iz];
                            waterSplash = 0;
                            waterR = 0;
                            waterG = 0;
                            waterB = 0;
                            float q = 0;
                            for (int i = 0; i < waterPulses.length; i += 1) { // Перебираем водные пульсации
                                if (waterPulses[i].t != 0 && hit(ix, iy, waterPulses[i].x, waterPulses[i].y, 1, waterPulses[i].s)) {
                                    q += 1;
                                    float d = distance(ix, iy, waterPulses[i].x, waterPulses[i].y);
                                    float k = 4;
                                    waterSplash += (d) * k / 100f * ((waterPulses[i].p - 1) / waterPulses[i].startPower);
                                }
                            }
                            if (hit(ix, iy, sunX, sunY, 1, sunRadius)) { // Проверяем столкновения с солнцем
                                float d = distance(ix, iy, sunX, sunY);
                                //Gdx.app.log("d", ""+d);
                                if (d > sunRadius * 0.65f) {
                                    waterR += sunColor.r / 4f * (1.1f - d / sunRadius);
                                    waterG += sunColor.g / 4f * (1.1f - d / sunRadius);
                                    waterB += sunColor.b / 4f * (1.1f - d / sunRadius);
                                } else {
                                    waterR += sunColor.r / 4f;
                                    waterG += sunColor.g / 4f;
                                    waterB += sunColor.b / 4f;
                                }
                            }
                            for (int i = 0; i < clouds.length; i += 1) {  // Перебираем облака
                                if (clouds[i].t != 0 && hit(ix, iy, cx / s + clouds[i].x + cloudsWidth / 2f, FY - ((cy + h) / s) + clouds[i].y, 1, cloudsWidth)) {
                                    for (int it = 0; it < clouds[i].tq; it++) {
                                        if (hit(ix, iy, cx / s + clouds[i].x + cloudsWidth / clouds[i].tq * it, FY - ((cy + h) / s) + clouds[i].y, 1, clouds[i].ts[it])) {
                                            waterR = clouds[i].dark + 0.02f - 0.004f * (clouds[i].tq / 2f - it);
                                            waterG = clouds[i].dark + 0.02f - 0.004f * (clouds[i].tq / 2f - it);
                                            waterB = clouds[i].dark + 0.02f - 0.004f * (clouds[i].tq / 2f - it);
                                        }
                                    }
                                }
                            }
                            if (q > 0) {
                                waterSplash /= q;
                            }
                            waterPixmap.setColor((f.r + waterR) + waterSplash, (f.g + waterG) + waterSplash, (f.b + waterB) + waterSplash, (f.a));
                            waterPixmap.drawPixel(ix, iy); // Отрисовываем результат
                        }
                    }
                }
                for (int i = 0; i < flowers.length; i += 1) { // Перебираем цветы (рисуем их отражения в воде)
                    if (flowers[i].t != 0) {
                        for (int it = 1; it < flowers[i].tq; it += 1) {
                            waterPixmap.setColor(Math.max(Math.min(dirtColor.r * 1.2f + flowers[i].tt[it] / 50f,1),0), Math.max(Math.min(dirtColor.g * 1.45f + flowers[i].tt[it] / 40f,1),0), Math.max(Math.min(dirtColor.b * 1.15f + flowers[i].tt[it] / 50f,1),0), 0.25f);
                            waterPixmap.drawLine((int) ((flowers[i].x + flowers[i].tx[it]) / s), (int) (FY - (flowers[i].y - flowers[i].ty[it]) / s), (int) ((flowers[i].x + flowers[i].tx[it - 1]) / s), (int) (FY - (flowers[i].y - flowers[i].ty[it - 1]) / s));
                            if(flowers[i].tt[it]>1||flowers[i].tt[it]<-1){
                                waterPixmap.setColor(Math.max(Math.min(dirtColor.r * 1.15f + flowers[i].tt[it] / 50f,1),0), Math.max(Math.min(dirtColor.g * 1.35f + flowers[i].tt[it] / 40f,1),0), Math.max(Math.min(dirtColor.b * 1.05f + flowers[i].tt[it] / 50f,1),0), 0.25f);
                                waterPixmap.drawLine((int) ((flowers[i].x + flowers[i].tx[it]) / s), (int) (FY - (flowers[i].y - flowers[i].ty[it]) / s), (int) ((flowers[i].x + flowers[i].tx[it]+sin(65-flowers[i].r*10)*flowers[i].s*flowers[i].tt[it]) / s), (int) (FY - (flowers[i].y - flowers[i].ty[it - 1]-cos(65-flowers[i].r*10)*flowers[i].s) / s));
                            }
                        }
                    }

                }
                waterTexture = new Texture(waterPixmap); // Создаём текстуру воды
                batch.draw(waterTexture, -cx, -cy, FX * s, FY * s); // Рисуем воду
            }

            for (int x = 0; x < S.length; x += 1) { // Отрисовываем текущий сегмент земли по координатам (X, Y, Z)
                for (int y = 0; y < S[x].length; y += 1) {
                    batch.draw(S[x][y][iz], -cx + x * segments * s, -cy + y * segments * s, s * segments, s * segments);
                }
            }
            if (iz == FZ - 1) { // На самом верхнем уровне земли отрисовываем всё остальное

                /*float scale = sunScale;
                for (int i = 0; i < flowers.length; i += 1) { // Тени травы
                    if (flowers[i].t != 0) {
                        for (int it = 1; it < flowers[i].tq; it += 1) {
                            dirtPixmap.setColor(0, 0, 0, Math.abs(0.3f*sunScale));
                            dirtPixmap.drawLine((int) ((flowers[i].x + flowers[i].tx[it]) / s), (int) (FY - (flowers[i].y + flowers[i].ty[it]*scale) / s), (int) ((flowers[i].x + flowers[i].tx[it - 1]) / s), (int) (FY - (flowers[i].y + flowers[i].ty[it - 1]*scale) / s));
                            if(flowers[i].tt[it]>1||flowers[i].tt[it]<-1){
                                dirtPixmap.setColor(0, 0, 0, Math.abs(0.3f*sunScale));
                                dirtPixmap.drawLine((int) ((flowers[i].x + flowers[i].tx[it]) / s), (int) (FY - (flowers[i].y + flowers[i].ty[it]*scale) / s), (int) ((flowers[i].x + flowers[i].tx[it]+sin(65-flowers[i].r*10)*flowers[i].s*flowers[i].tt[it]) / s), (int) (FY - (flowers[i].y + (flowers[i].ty[it - 1]+cos(65-flowers[i].r*10)*flowers[i].s)*scale) / s));
                            }
                        }
                    }
                }*/

                for (int i = 0; i < flowers.length; i += 1) { // Трава
                    if (flowers[i].t != 0) {
                        for (int it = 1; it < flowers[i].tq; it += 1) {
                            dirtPixmap.setColor(Math.max(Math.min(dirtColor.r * 1.2f + flowers[i].tt[it] / 50f,1),0), Math.max(Math.min(dirtColor.g * 1.45f + flowers[i].tt[it] / 40f,1),0), Math.max(Math.min(dirtColor.b * 1.15f + flowers[i].tt[it] / 50f,1),0), 1);
                            dirtPixmap.drawLine((int) ((flowers[i].x + flowers[i].tx[it]) / s), (int) (FY - (flowers[i].y + flowers[i].ty[it]) / s), (int) ((flowers[i].x + flowers[i].tx[it - 1]) / s), (int) (FY - (flowers[i].y + flowers[i].ty[it - 1]) / s));
                            if(flowers[i].tt[it]>1||flowers[i].tt[it]<-1){
                                dirtPixmap.setColor(Math.max(Math.min(dirtColor.r * 1.12f + flowers[i].tt[it] / 45f,1),0), Math.max(Math.min(dirtColor.g * 1.3f + flowers[i].tt[it] / 55f,1),0), Math.max(Math.min(dirtColor.b * 1.05f + flowers[i].tt[it] / 50f,1),0), 1);
                                dirtPixmap.drawLine((int) ((flowers[i].x + flowers[i].tx[it]) / s), (int) (FY - (flowers[i].y + flowers[i].ty[it]) / s), (int) ((flowers[i].x + flowers[i].tx[it]+sin(65-flowers[i].r*10)*flowers[i].s*flowers[i].tt[it]) / s), (int) (FY - (flowers[i].y + flowers[i].ty[it - 1]+cos(65-flowers[i].r*10)*flowers[i].s) / s));
                            }
                        }
                    }
                }
                dirtTexture = new Texture(dirtPixmap); // Создаём текстуру земли на основе пиксмапа
                batch.draw(dirtTexture, -cx, -cy, FX * s, FY * s); // Отрисовываем текстуру земли.
                batch.end();

                waterPixmap.dispose(); // Удаляем ненужные текстуры и пиксмапы в конце
                dirtPixmap.dispose();
                waterTexture.dispose();
                dirtTexture.dispose();
            }
        }
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        drawer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < parts.length; i += 1) { // Отрисовка водных капель (дождь)
            if (parts[i].t != 0) {
                for (int it = 1; it < parts[i].tq - parts[i].state; it++) {
                    float size = Math.max((parts[i].s) * (1 - (float) it / parts[1].tq), 2);
                    drawer.setColor(waterColor.r * 4f * dark + redTimeColor, waterColor.g * 4f * dark + greenTimeColor, waterColor.b * 5f * dark + blueTimeColor, 0.8f - it * 0.12f);
                    drawer.rectLine(-cx + parts[i].tx[it], -cy + parts[i].ty[it] + parts[i].tz[it] * ca, -cx + parts[i].tx[it - 1] - size / 2f, -cy + parts[i].ty[it - 1] + parts[i].tz[it - 1] * ca - size / 2f, size);
                }
            }
        }
        drawer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void setWaterPulse(float x, float y, float power) { // Размещает частицу водной пульсации, если есть свободная
        int index = -1;
        for (int i = 0; i < waterPulses.length; i += 1) {
            if (waterPulses[i].t == 0) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            waterPulses[index].t = 1;
            waterPulses[index].p = power;
            waterPulses[index].startPower = power;
            waterPulses[index].s = 1;
            waterPulses[index].x = x;
            waterPulses[index].y = y;
        }
    }

    public void setWaterPart(float x, float y) {  // Размещает частицу воды, если есть свободный
        int index = -1;
        for (int i = 0; i < parts.length; i += 1) {
            if (parts[i].t == 0) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            parts[index].t = 1;
            parts[index].s = 5 + random.nextInt(3);
            parts[index].z = h * 2;
            parts[index].x = x;
            parts[index].y = y;
            parts[index].vz = -10;
            parts[index].state = 0;
            parts[index].vx = windX;
            parts[index].vy = windY;
            for (int it = parts[index].tq - 1; it >= 0; it--) {
                parts[index].tx[it] = x;
                parts[index].ty[it] = y;
                parts[index].tz[it] = parts[index].z;
            }
        }
    }

    public void setCloud(float x, float y) {  // Размещает облако, если есть свободное
        int index = -1;
        for (int i = 0; i < clouds.length; i += 1) {
            if (clouds[i].t == 0) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            clouds[index].t = 1;
            clouds[index].dark = (index) / 100f;
            clouds[index].x = x;
            clouds[index].y = y;
            clouds[index].state = 0;
            clouds[index].vx = (random.nextInt(2) + 1) / 10f;
            clouds[index].vy = (random.nextInt(3) - 1) / 40f;
            for (int it = clouds[index].tq - 1; it >= 0; it--) {
                clouds[index].ts[it] = random.nextInt((int) cloudsHeight) + 2;
            }

        }
    }

    public void setFlower(float x, float y) { // Размещает цветок, если есть свободный
        int index = -1;
        for (int i = 0; i < flowers.length; i += 1) {
            if (flowers[i].t == 0) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            flowers[index].t = 1;
            flowers[index].x = x;
            flowers[index].y = y;
            flowers[index].r = 0;
            flowers[index].s = random.nextInt(7)+2;
            for (int it = flowers[index].tq - 1; it >= 0; it--) {
                flowers[index].tx[it] = 0;
                flowers[index].tt[it] = random.nextInt(11) - 5;
                flowers[index].ty[it] = it * s;
            }

        }
    }

    public boolean hit(float x1, float y1, float x2, float y2, float r1, float r2) { // Проверяет, соприкасаются ли окружности
        float dx = x1 - x2;
        float dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy) <= r1 + r2;
    }

    public float distance(float x1, float y1, float x2, float y2) { // Принимает координаты двух точек, возвращает расстояние в float
        float dx = x1 - x2;
        float dy = y1 - y2;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float sin(float v) {  // Принимает градусы, возвращает значение косинуса в float
        return (float) Math.sin(v * Math.PI / 180f);
    }

    public float cos(float v) {  // Принимает градусы, возвращает значение косинуса в float
        return (float) Math.cos(v * Math.PI / 180f);
    }

    public boolean act(float x, float y) { // Проверяет, актуальна ли данная ячейка
        return x > -1 && x < FX && y > -1 && y < FY;
    }
}



