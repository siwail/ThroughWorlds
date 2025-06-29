package com.throughworlds;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class Main extends ApplicationAdapter {
    Random random = new Random(); // Создание генератора случайностей
    SpriteBatchRubber batch; // Создание отрисовщика картинок
    ShapeRendererRubber drawer; // Создание отрисовщика фигур
    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    BitmapFont[] font = new BitmapFont[10];
    float cx = 0, cy = 0; // Позиция камеры
    float cAccelerate = 0.5f; // Ускорение камеры
    float sunRotate = 100;
    float w, h; // Ширина, длина экрана
    int FX = 1200, FY = 800, FZ = 6; // Размеры сетки локации (в клетках) (default 1000x600x6)
    int segments = 50;
    Field[][][] F = new Field[FX][FY][FZ]; // Сетка локации
    Texture[][][] S = new Texture[FX / segments][FY / segments][FZ]; // Текстуры локации
    float s = 4; // Размер одной клетки в сетке
    float sc = 1;
    Texture cat, shadow;
    Pixmap catPixmap;
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
    float goW = 0, goA = 0, goS = 0, goD = 0;
    int pid;
    int pq = 50;
    float waterState = 0;
    float waterDir = 1;
    Person[] p = new Person[pq];
    boolean online = false;
    boolean initialized = false;
    float rotateAnim = 0;
    ThroughServerPacket initializationData;
    Texture[][] head = new Texture[1][4];
    Texture[][] hair = new Texture[1][4];
    Texture[][] hat = new Texture[2][4];
    Texture[][] beard = new Texture[2][4];
    Texture[][] eyes = new Texture[2][4];
    Texture[][] prop = new Texture[1][4];
    Texture[][] boot = new Texture[1][4];
    Texture[][] gloves = new Texture[1][4];
    HashMap<String, Animation> animations = new HashMap<String, Animation>();

    @Override
    public void create() {
        Gdx.graphics.setVSync(true);
        FileHandle[] saves = Gdx.files.local("assets/tools/saves/").list(); // Загрузка всех анимаций
        Gdx.app.log("", "" + saves.length);

        for (FileHandle save : saves) {
            animations.put(save.name().replace(".txt", ""), new Animation(save.name()));

        }
        for (int i = 0; i < head.length; i++) {
            head[i][0] = new Texture("person/head_" + i + "_left.png");
            head[i][1] = new Texture("person/head_" + i + ".png");
            head[i][2] = new Texture("person/head_" + i + "_back.png");
            head[i][3] = new Texture("person/head_" + i + "_right.png");
        }
        for (int i = 0; i < hair.length; i++) {
            hair[i][0] = new Texture("person/hair_" + i + "_left.png");
            hair[i][1] = new Texture("person/hair_" + i + ".png");
            hair[i][2] = new Texture("person/hair_" + i + "_back.png");
            hair[i][3] = new Texture("person/hair_" + i + "_right.png");
        }
        for (int i = 0; i < hat.length; i++) {
            hat[i][0] = new Texture("person/hat_" + i + "_left.png");
            hat[i][1] = new Texture("person/hat_" + i + ".png");
            hat[i][2] = new Texture("person/hat_" + i + "_back.png");
            hat[i][3] = new Texture("person/hat_" + i + "_right.png");
        }
        for (int i = 0; i < beard.length; i++) {
            beard[i][0] = new Texture("person/beard_" + i + "_left.png");
            beard[i][1] = new Texture("person/beard_" + i + ".png");
            beard[i][2] = new Texture("person/beard_" + i + "_back.png");
            beard[i][3] = new Texture("person/beard_" + i + "_right.png");
        }
        for (int i = 0; i < eyes.length; i++) {
            eyes[i][0] = new Texture("person/eyes_" + i + "_left.png");
            eyes[i][1] = new Texture("person/eyes_" + i + ".png");
            eyes[i][2] = new Texture("person/eyes_" + i + "_back.png");
            eyes[i][3] = new Texture("person/eyes_" + i + "_right.png");
        }
        for (int i = 0; i < prop.length; i++) {
            prop[i][0] = new Texture("person/prop_" + i + "_left.png");
            prop[i][1] = new Texture("person/prop_" + i + ".png");
            prop[i][2] = new Texture("person/prop_" + i + "_back.png");
            prop[i][3] = new Texture("person/prop_" + i + "_right.png");
        }
        for (int i = 0; i < boot.length; i++) {
            boot[i][0] = new Texture("person/boot_" + i + "_left.png");
            boot[i][1] = new Texture("person/boot_" + i + ".png");
            boot[i][2] = new Texture("person/boot_" + i + "_back.png");
            boot[i][3] = new Texture("person/boot_" + i + "_right.png");
        }
        for (int i = 0; i < gloves.length; i++) {
            gloves[i][0] = new Texture("person/gloves_" + i + "_left.png");
            gloves[i][1] = new Texture("person/gloves_" + i + ".png");
            gloves[i][2] = new Texture("person/gloves_" + i + "_back.png");
            gloves[i][3] = new Texture("person/gloves_" + i + "_right.png");
        }

        m = this;
        batch = new SpriteBatchRubber(this);
        drawer = new ShapeRendererRubber(this);
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font_2.ttf"));
        parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890[]-=_+./#$%?!";
        for (int i = 0; i < font.length; i++) {
            parameter.size = (int) (2.5f + i * 1.9f);
            //parameter.borderWidth = 1 + i * 0.2f;
            //parameter.borderColor = new Color(0, 0, 0, 1);
            parameter.color = new Color(1, 1, 1, 1);
            font[i] = generator.generateFont(parameter);
        }
        cat = new Texture("trash/cat.png");
        cat.getTextureData().prepare();
        catPixmap = cat.getTextureData().consumePixmap();

        shadow = new Texture("trash/shadow.png");
        if (online) {
            Thread clientThread = new Thread(() -> {
                ThroughClient client = new ThroughClient(m);
                try {
                    client.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            clientThread.start();
        } else {
            seed = random.nextInt(10000);
            generateLocation();
            for (int i = 0; i < pq; i++) {
                p[i] = new Person(m, random.nextInt(FX) * s, random.nextInt(FY) * s, random.nextInt(FZ) * s, i);
                p[i].cr = random.nextInt(255) / 255f;
                p[i].cg = random.nextInt(255) / 255f;
                p[i].cb = random.nextInt(255) / 255f;
                p[i].name = "MEGABOT #" + i;
                p[i].nameT = random.nextInt(2);
                p[i].generate(random.nextInt(10000));
            }
            pid = 0;
            initialized = true;
        }


        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.W) {
                    goW = 1;
                }
                if (keycode == Input.Keys.A) {
                    goA = -1;
                }
                if (keycode == Input.Keys.S) {
                    goS = -1;
                }
                if (keycode == Input.Keys.D) {
                    goD = 1;
                }
                if (keycode == Input.Keys.SPACE) {
                    p[pid].vz += 10 * cAccelerate;
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.W) {
                    goW = 0;
                }
                if (keycode == Input.Keys.A) {
                    goA = 0;
                }
                if (keycode == Input.Keys.S) {
                    goS = 0;
                }
                if (keycode == Input.Keys.D) {
                    goD = 0;
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
                if (!initialized) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }

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
                p[pid].vx += (goA + goD) * cAccelerate;
                p[pid].vy += (goW + goS) * cAccelerate;
                for (int i = 0; i < pq; i += 1) {
                    p[i].math();
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

    public void updateData(ThroughServerPacket data) {
        for (int i = 0; i < data.x.length; i++) {
            if (i != pid) {
                p[i].x = data.x[i];
                p[i].y = data.y[i];
                p[i].z = data.z[i];
                p[i].vx = data.vx[i];
                p[i].vy = data.vy[i];
                p[i].vz = data.vz[i];
                p[i].cr = data.cr[i];
                p[i].cg = data.cg[i];
                p[i].cb = data.cb[i];
                p[i].health = data.health[i];
                p[i].maxHealth = data.maxHealth[i];
                p[i].nameT = data.t[i];
                p[i].name = data.name[i];
            }
        }
        pid = data.index;
    }

    public void initialization() {
        ThroughServerPacket data = initializationData;
        seed = data.seed;
        generateLocation();
        for (int i = 0; i < pq; i++) {
            p[i] = new Person(m, random.nextInt(FX) * s, random.nextInt(FY) * s, random.nextInt(FZ) * s, i);
            p[i].cr = 225;
            p[i].cg = 25;
            p[i].cb = 25;
        }
        for (int i = 0; i < data.x.length; i++) {
            p[i] = new Person(m, data.x[i], data.y[i], data.z[i], i);
            p[i].vx = data.vx[i];
            p[i].vy = data.vy[i];
            p[i].vz = data.vz[i];
            p[i].cr = data.cr[i];
            p[i].cg = data.cg[i];
            p[i].cb = data.cb[i];
            p[i].health = data.health[i];
            p[i].maxHealth = data.maxHealth[i];
            p[i].nameT = data.t[i];
            p[i].name = data.name[i];
        }
        Gdx.app.log("data", "" + p[0].x);
        pid = data.index;
        initialized = true;
    }

    public void generateWay(Random random, float x1, float y1, float x2, float y2, float s, float dark) {

        float r = 270 - (float) (Math.atan2(y1 - y2, x1 - x2) * 180f / Math.PI);
        float vx = sin(r);
        float vy = cos(r);
        float ix = x1, iy = y1;
        float d = distance(x1, y1, x2, y2);
        int step = 8;
        for (int i = 0; i < d; i += step) {
            generatePlast(random, ix + (random.nextInt(11) - 5) / 3f, iy + (random.nextInt(11) - 5) / 3f, s + (random.nextInt(100) - 50) / 60f, dark);
            ix += vx * step;
            iy += vy * step;
        }
    }

    public void generatePlast(Random random, float x, float y, float s, float dark) {

        int ix, iy, iz;
        float r, g, b;
        float mode = 1;
        int toSwitch = 0;
        int e = 10;
        for (float ir = 0; ir < 360; ir++) {
            for (float is = 0; is < s; is++) {
                toSwitch--;
                if (toSwitch <= 0) {
                    toSwitch = random.nextInt(100);
                    mode = (random.nextInt(30) + 85) / 100f;
                }
                ix = (int) (x + sin(ir) * is);
                iy = (int) (y + cos(ir) * is);
                iz = FZ - 1;
                if (act(ix, iy)) {
                    if (F[ix][iy][iz].t == 1) {
                        r = F[ix][iy][iz].r;
                        g = F[ix][iy][iz].g;
                        b = F[ix][iy][iz].b;
                    } else {
                        r = 0.05f;
                        g = 0.05f;
                        b = 0.05f;
                        if (random.nextInt(30) == 0) {
                            F[ix][iy][iz].t = 1;
                        }
                    }
                    F[ix][iy][iz].r = (r + g / 4f + b / 4f + 0.01f + dirtColor.r * (mode) + (random.nextInt(e)) / 200f) / (2.5f + dark);
                    F[ix][iy][iz].g = (g + r / 4f + b / 4f + 0.01f + dirtColor.g * (mode) + (random.nextInt(e)) / 200f) / (2.5f + dark);
                    F[ix][iy][iz].b = (b + r / 4f + g / 4f + 0.01f + dirtColor.b * (mode) + (random.nextInt(e)) / 200f) / (2.5f + dark);
                }
            }
        }
    }

    public void generateLocation() {
        Random random = new Random(seed);
        int bioms = 1;
        for (int ib = 0; ib < bioms; ib++) {
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


            generatePlast(random, FX / 2f, FY / 2f, 24, -0.15f);
            generateWay(random, FX / 2f, FY / 2f, FX - FX / 8f, FY / 2f, 12, -0.15f);
            generateWay(random, FX / 2f, FY / 2f, FX / 8f, FY / 2f, 12, -0.15f);
            generateWay(random, FX / 2f, FY / 2f, FX / 2f, FY - FY / 8f, 12, -0.15f);


            generatePlast(random, FX / 2f, FY / 2f, 21, 0.25f);
            generateWay(random, FX / 2f, FY / 2f, FX - FX / 8f, FY / 2f, 9, 0.25f);
            generateWay(random, FX / 2f, FY / 2f, FX / 8f, FY / 2f, 9, 0.25f);
            generateWay(random, FX / 2f, FY / 2f, FX / 2f, FY - FY / 8f, 9, 0.25f);

            generatePlast(random, FX / 2f, FY / 2f, 18, 0.5f);
            generateWay(random, FX / 2f, FY / 2f, FX - FX / 8f, FY / 2f, 7, 0.5f);
            generateWay(random, FX / 2f, FY / 2f, FX / 8f, FY / 2f, 7, 0.5f);
            generateWay(random, FX / 2f, FY / 2f, FX / 2f, FY - FY / 8f, 7, 0.5f);


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
                                    if (iz == (int) wetRate) {
                                        F[ix][iy][iz].t = 2;
                                        F[ix][iy][iz].m = 0.5f;
                                        F[ix][iy][iz].a = waterColor.a;
                                        F[ix][iy][iz].r = (waterColor.r);
                                        F[ix][iy][iz].g = (waterColor.g);
                                        F[ix][iy][iz].b = (waterColor.b);
                                        //pixmap.setColor(1, 1, 1, 0);
                                        //pixmap.drawPixel(ix - x * segments, segments - 1 - (iy - y * segments));
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
        }
        //
    }

    @Override
    public void render() {
        if (!initialized && online) {
            if (initializationData != null) {
                initialization();
            }
            return;
        }

        rotateAnim += 2f;
        if (rotateAnim > 360f) {
            rotateAnim -= 360f;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        ScreenUtils.clear(0, 0, 0, 1);
        sunRotate += 0.2f * gameSpeed;
        if (sunRotate > 360) {
            sunRotate -= 360;
        }

        cx += (p[pid].x - w / 2f / sc - cx) / 10f;
        cy += (p[pid].y - h / 2f / sc - cy) / 10f;
        if(cx<0){
            cx=0;
        }
        if(cy<0){
            cy=0;
        }
        if(cx>FX*s-w/sc){
            cx=FX*s-w/sc;
        }
        if(cy>FY*s-h/sc){
            cy=FY*s-h/sc;
        }
        float fakeRotate = 195;
        float sunX = (cx + w / 2) / s; // Координата солнца по X
        float sunY = FY - (cy + h / 2) / s + sin(fakeRotate) * (h / s); // Координата солнца по Y (Зависит от текущего поворота)
        float sunScale = sin(fakeRotate - 90); // Размер солнца
        float sunRadius = (sunScale + 0.5f) * this.sunRadius; // Радиус солнца
        float cx = this.cx, cy = this.cy; // Ускоряем доступ к позиции камеры
        float dark = (sunScale + 1.35f); // Настройки уровня освещённости
        float redTimeColor = 0.12f * Math.max(sunScale, 0);  // Настройки цвета в зависимости от времени суток
        float greenTimeColor = 0.15f * Math.max(sunScale, 0);
        float blueTimeColor = 0.25f * Math.max(1 - sunScale, 0);
        batch.setColor(sunColor.r * dark + redTimeColor, sunColor.g * dark + greenTimeColor, sunColor.b * dark + blueTimeColor, 1);
        batch.begin();
        float waterSplash, waterR, waterG, waterB; // Параметры воды

        Pixmap dirtPixmap = new Pixmap(FX, FY, Pixmap.Format.RGBA8888); // Создаём пиксмап земли
        Pixmap waterPixmap = new Pixmap(FX, FY, Pixmap.Format.RGBA8888); // Создаём пиксмап воды
        Texture waterTexture = null, dirtTexture = null; // Заранее объявляем текстуру воды, текстуру земли
        int minX = Math.max((int) (cx / s), 0);
        int minY = Math.max((int) (cy / s), 0);
        int maxX = Math.min((int) ((cx + w / sc) / s), FX - 1);
        int maxY = Math.min((int) ((cy + h / sc) / s), FY - 1);
        for (int iz = 0; iz < FZ; iz++) { // Перебор уровней земли
            if (iz == (int) wetRate) { // Выполняется в том случае, если текущий уровень совпадает с уровнем воды
                for (int i = 0; i < pq; i++) { // Перебираем и отрисовываем отражения персонажей
                    p[i].drawMirror();
                }
                for (int ix = minX; ix < maxX; ix++) {
                    for (int fy = minY; fy < maxY; fy++) {
                        int iy = fy;
                        if (F[ix][iy][iz].t == 2) { // Если на данном пикселе расположена вода F[ix][iy][iz].t == 2
                            Field f = F[ix][iy][iz];
                            waterSplash = 0;
                            waterR = 0;
                            waterG = 0;
                            waterB = 0;
                            float scale = +sin(sunRotate * 10 + ix + iy) / 45f;
                            waterPixmap.setColor((f.r + waterR) + waterSplash + scale, (f.g + waterG) + waterSplash + scale, (f.b + waterB) + waterSplash + scale, f.a);
                            //waterPixmap.setColor(1,1,1,1);
                            waterPixmap.drawPixel(ix, FY - iy - 1); // Отрисовываем результат
                        }
                    }
                }
                float r = 0.4f;
                waterPixmap.setBlending(Pixmap.Blending.None);
                waterPixmap.setColor(sunColor.r, sunColor.g, sunColor.b, 0.14f); // Солнце рисуем
                waterPixmap.fillCircle((int) sunX, (int) sunY, (int) (sunRadius * r * 1.25f));
                waterPixmap.setColor(sunColor.r, sunColor.g, sunColor.b, 0.38f);
                waterPixmap.fillCircle((int) sunX, (int) sunY, (int) (sunRadius * r));
                for (int i = 0; i < clouds.length; i += 1) {  // Перебираем облака
                    if (clouds[i].t != 0) {
                        for (int it = 0; it < clouds[i].tq; it++) {
                            waterPixmap.setColor(0.175f + it * 0.04f + waterColor.r / 2f, 0.175f + it * 0.04f + waterColor.g / 2f, 0.175f + it * 0.04f + waterColor.b / 2f, 0.2f);
                            waterPixmap.fillCircle((int) (cx / s + clouds[i].x + it * cloudsWidth / clouds[i].tq), (int) (h / s * 2 - cy / s + clouds[i].y + clouds[i].ty[it]), (int) (clouds[i].ts[it]));
                        }
                    }
                }
                waterPixmap.setBlending(Pixmap.Blending.SourceOver);
                for (int i = 0; i < flowers.length; i += 1) { // Перебираем цветы (рисуем их отражения в воде)
                    if (flowers[i].t != 0) {
                        for (int it = 1; it < flowers[i].tq; it += 1) {
                            waterPixmap.setColor(Math.max(Math.min(dirtColor.r * 1.2f + flowers[i].tt[it] / 50f, 1), 0), Math.max(Math.min(dirtColor.g * 1.45f + flowers[i].tt[it] / 40f, 1), 0), Math.max(Math.min(dirtColor.b * 1.15f + flowers[i].tt[it] / 50f, 1), 0), 0.25f);
                            waterPixmap.drawLine((int) ((flowers[i].x + flowers[i].tx[it]) / s), (int) (FY - (flowers[i].y - flowers[i].ty[it]) / s), (int) ((flowers[i].x + flowers[i].tx[it - 1]) / s), (int) (FY - (flowers[i].y - flowers[i].ty[it - 1]) / s));
                            if (flowers[i].tt[it] > 1 || flowers[i].tt[it] < -1) {
                                waterPixmap.setColor(Math.max(Math.min(dirtColor.r * 1.15f + flowers[i].tt[it] / 50f, 1), 0), Math.max(Math.min(dirtColor.g * 1.35f + flowers[i].tt[it] / 40f, 1), 0), Math.max(Math.min(dirtColor.b * 1.05f + flowers[i].tt[it] / 50f, 1), 0), 0.25f);
                                waterPixmap.drawLine((int) ((flowers[i].x + flowers[i].tx[it]) / s), (int) (FY - (flowers[i].y - flowers[i].ty[it]) / s), (int) ((flowers[i].x + flowers[i].tx[it] + sin(65 - flowers[i].r * 10) * flowers[i].s * flowers[i].tt[it]) / s), (int) (FY - (flowers[i].y - (flowers[i].ty[it - 1] - cos(65 - flowers[i].r * 10) * flowers[i].s)) / s));
                            }
                        }
                    }
                }

                for (int i = 0; i < waterPulses.length; i += 1) { // Перебираем водные пульсации
                    if (waterPulses[i].t != 0) {

                        waterPixmap.setColor(waterColor.r * 1.25f + 0.1f, waterColor.g * 1.25f + 0.1f, waterColor.b * 1.25f + 0.1f, 0.37f * (waterPulses[i].p - 1));
                        waterPixmap.fillCircle((int) waterPulses[i].x, (int) waterPulses[i].y, (int) waterPulses[i].s);
                    }
                }
                waterTexture = new Texture(waterPixmap); // Создаём текстуру воды
                batch.draw(waterTexture, -cx, -cy, FX * s, FY * s); // Рисуем воду
            }
            for (int x = 0; x < S.length; x += 1) { // Отрисовываем текущий сегмент земли по координатам (X, Y, Z)
                for (int y = 0; y < S[x].length; y += 1) {
                    if (minX - segments < x * segments && minY - segments < y * segments && maxX + segments > x * segments && maxY + segments > y * segments) {
                        batch.draw(S[x][y][iz], -cx + x * s * segments, -cy + y * s * segments, s * segments, s * segments);
                    }
                }
            }
        }
        for (int i = 0; i < flowers.length; i += 1) { // Перебираем и отрисовываем цветы
            if (flowers[i].t != 0) {
                for (int it = 1; it < flowers[i].tq; it += 1) {
                    dirtPixmap.setColor(Math.max(Math.min(dirtColor.r * 1.2f + flowers[i].tt[it] / 50f, 1), 0), Math.max(Math.min(dirtColor.g * 1.45f + flowers[i].tt[it] / 40f, 1), 0), Math.max(Math.min(dirtColor.b * 1.15f + flowers[i].tt[it] / 50f, 1), 0), 1);
                    dirtPixmap.drawLine((int) ((flowers[i].x + flowers[i].tx[it]) / s), (int) (FY - (flowers[i].y + flowers[i].ty[it]) / s), (int) ((flowers[i].x + flowers[i].tx[it - 1]) / s), (int) (FY - (flowers[i].y + flowers[i].ty[it - 1]) / s));
                    if (flowers[i].tt[it] > 1 || flowers[i].tt[it] < -1) {
                        dirtPixmap.setColor(Math.max(Math.min(dirtColor.r * 1.12f + flowers[i].tt[it] / 45f, 1), 0), Math.max(Math.min(dirtColor.g * 1.3f + flowers[i].tt[it] / 55f, 1), 0), Math.max(Math.min(dirtColor.b * 1.05f + flowers[i].tt[it] / 50f, 1), 0), 1);
                        dirtPixmap.drawLine((int) ((flowers[i].x + flowers[i].tx[it]) / s), (int) (FY - (flowers[i].y + flowers[i].ty[it]) / s), (int) ((flowers[i].x + flowers[i].tx[it] + sin(65 - flowers[i].r * 10) * flowers[i].s * flowers[i].tt[it]) / s), (int) (FY - (flowers[i].y + flowers[i].ty[it - 1] + cos(65 - flowers[i].r * 10) * flowers[i].s) / s));
                    }
                }
            }
        }
        dirtTexture = new Texture(dirtPixmap); // Создаём текстуру земли на основе пиксмапа
        batch.draw(dirtTexture, -cx, -cy, FX * s, FY * s); // Отрисовываем текстуру земли.
        for (int i = 0; i < pq; i += 1) {
            p[i].drawShadow();
        }
        for (int i = 0; i < pq; i += 1) {
            p[i].draw();
        }
        for (int i = 0; i < pq; i += 1) {
            p[i].drawBar();
        }
        batch.end();
        waterPixmap.dispose(); // Удаляем ненужные текстуры и пиксмапы в конце
        dirtPixmap.dispose();
        waterTexture.dispose();
        dirtTexture.dispose();
        Gdx.gl.glEnable(GL20.GL_BLEND); // Переходим к отрисовке геометрических фигур
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        drawer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < parts.length; i += 1) { // Отрисовка водных капель (дождь)
            if (parts[i].t != 0) {
                for (int it = 1; it < parts[i].tq - parts[i].state; it++) {
                    float size = Math.max((parts[i].s) * (1 - (float) it / parts[1].tq), 2);
                    drawer.setColor(waterColor.r * 4f * dark + redTimeColor, waterColor.g * 4f * dark + greenTimeColor, waterColor.b * 5f * dark + blueTimeColor, 0.5f - it * 0.12f);
                    drawer.rectLine(-cx + parts[i].tx[it], -cy + parts[i].ty[it] + parts[i].tz[it], -cx + parts[i].tx[it - 1] - size / 2f, -cy + parts[i].ty[it - 1] + parts[i].tz[it - 1] - size / 2f, size);
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
            clouds[index].scale = 0;
            clouds[index].tyd = random.nextInt(5) - 2;
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
            flowers[index].s = random.nextInt(7) + 2;
            for (int it = flowers[index].tq - 1; it >= 0; it--) {
                flowers[index].tx[it] = 0;
                flowers[index].tt[it] = random.nextInt(11) - 5;
                flowers[index].ty[it] = it * s;
            }
        }
    }
    public void rectLine(float x1, float y1, float x2, float y2, float s) {
        float ix = x1;
        float iy = y1;
        float size = 4;
        float d = m.distance(x1, y1, x2, y2);
        float vx = (x2 - x1) / d;
        float vy = (y2 - y1) / d;
        for (int id = 0; id < d; id++) {
            ix += vx;
            iy += vy;
            m.drawer.rect((int) (ix / size) * size, (int) (iy / size) * size, size, size);
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

    public float[] quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float col) {
        float[] vertices = new float[20];

        final float u = 0;
        final float v = 1;
        final float u2 = 1;
        final float v2 = 0;
        int idx = 0;

        vertices[idx++] = x1;
        vertices[idx++] = y1;
        vertices[idx++] = col;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x2;
        vertices[idx++] = y2;
        vertices[idx++] = col;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = x3;
        vertices[idx++] = y3;
        vertices[idx++] = col;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = x4;
        vertices[idx++] = y4;
        vertices[idx++] = col;
        vertices[idx++] = u2;
        vertices[idx] = v;
        return vertices;
    }
}



