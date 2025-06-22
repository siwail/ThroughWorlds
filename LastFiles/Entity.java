package com.throughworlds;

public class Entity {
    float x = 0, y = 0, z = 0; // Позиция
    float vx = 0, vy = 0, vz = 0; // Скорости
    float s = 150; // Размер
    int t = 0; // Тип объекта
    Main m; // Ссылка на главный класс
    public Entity(Main main, float x, float y, float z){
        m = main;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
