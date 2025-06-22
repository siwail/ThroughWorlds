import pygame, time, math, random

def save():
    global savedState, lastSaved
    print("Сохранено!")
    filename = "saves/save_"+str(len(frames))+"_"+str(int(time.time()))+".txt"
    f = open(filename, "w", encoding="UTF-8")
    f.write(str(frames))
    f.close()
    lastSaved = filename
    savedState=10
def start():
    global play, frame
    play = True
    frame = 0
def newFrame():
    global frames
    frames.append(blank)
def sin(v):
    return math.sin(v/180*math.pi)
def cos(v):
    return math.cos(v/180*math.pi)
def deg(x1,y1,x2,y2):
    return 90-math.atan2(y1-y2,x1-x2)/math.pi*180
def hit(x1,y1,r1,x2,y2,r2):
    dx = x1-x2
    dy = y1-y2
    return (dx*dx+dy*dy)**0.5<r1+r2
def next():
    global frame, play
    if frame<len(frames)-1:
        frame+=1
    else:
        play = False
def prev():
    global frame
    if frame>0:
        frame-=1
def copy():
    global frames, copiedState
    copiedState = 10
    copied = []
    for e in frames[frame]:
        copied.append(e)
    frames.append(copied)
def remove():
    global frames, copiedState
    if len(frames)>1:
        frames.pop(frame)
    frame = 0
white = (240,240,240)

"""
НАПРАВЛЕНИЯ:
0 - НАПРАВО
1 - СПИНОЙ К КАМЕРЕ
2 - НАЛЕВО
3 - НА КАМЕРУ

ИНДЕКСЫ РЁБЕР:
0 - КОРПУС
1 - БОЛЬШАЯ ИКРА ЛЕВАЯ
2 - БОЛЬШАЯ ИКРА ПРАВАЯ
3 - МАЛЕНЬКАЯ ИКРА ЛЕВАЯ
4 - МАЛЕНЬКАЯ ИКРА ПРАВАЯ
5 - СТУПНЯ ЛЕВАЯ
6 - СТУПНЯ ПРАВАЯ
7 - ПЛЕЧО ЛЕВОЕ
8 - ПЛЕЧО ПРАВОЕ
9 - ЛОКОТЬ ЛЕВЫЙ
10 - ЛОКОТЬ ПРАВЫЙ
11 - КИСТЬ ЛЕВАЯ
12 - КИСТЬ ПРАВАЯ
13 - ГОЛОВА
"""

blank = [180, -45, 45, 10, -10, 0, 0, -20, 20, 10, -10, 0, 0, 180, 0]
moved = [0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
fixed = -2
lastFixed = [0,0]
lastSaved = ""
size = 40
frames = []
frame = 0
play = False
toPlay = 2
newFrame()

pygame.init()
w = 900
h = 900
mx = 0
my = 0
savedState = 0
copiedState = 0
lClicked = False
sc = pygame.display.set_mode((w,h), pygame.NOFRAME)
pygame.mouse.set_visible(False)
pygame.font.init() 
bfont = pygame.font.SysFont('Consolas', 35)
nfont = pygame.font.SysFont('Consolas', 20)
sfont = pygame.font.SysFont('Consolas', 15)
run = True
while run:
    for e in pygame.event.get():
        if e.type == pygame.MOUSEMOTION:
            mx, my = e.pos
        if e.type == pygame.QUIT:
            run = False
        if e.type == pygame.MOUSEBUTTONDOWN:
            if e.button == 1:
                lClicked = True
        if e.type == pygame.MOUSEBUTTONUP:
            fixed = -2
            if e.button == 1:
                lClicked = False
        if e.type == pygame.KEYDOWN:
            if not play:
                if e.key == pygame.K_s:
                    save()
                if e.key == pygame.K_d:
                    next()
                if e.key == pygame.K_a:
                    prev()
                if e.key == pygame.K_c:
                    copy()
                if e.key == pygame.K_p:
                    remove()
                if e.key == pygame.K_o:
                    start()
    
    exitTouched = 0
    #ЗАКРЫВАЕМ ПРИЛОЖЕНИЕ ПОСЛЕ НАЖАТИЯ НА КНОПКУ
    if mx>w-45 and mx<w-5 and my > 5 and my < 45:
        exitTouched+=50
        if lClicked:
            exitTouched+=50
            run = False
    
    #ОТОБРАЖЕНИЕ ЗАДНЕГО ФОНА
    sc.fill((4,4,8))  
    pygame.draw.rect(sc, (10,10,20), (50,50,w-100,h-100))
    ws = 800
    hs = 800
    ix = 0
    while ix < 40:
        iy = 0
        while iy < 40:
            if ix % 2 == (iy+1)%2:
                pygame.draw.rect(sc, (5,5,10), (50+ix*ws/40+2.5,50+iy*hs/40+2.5,ws/40-5,hs/40-5))
            iy+=1
        ix+=1
    pygame.draw.rect(sc, (100,100,100), (0,0,w,h), 1)

    #ОТОБРАЖЕНИЕ ПРЕДЫДУЩЕГО КАДРА
    f = 0
    while f < frame:
        k = frame-f-0.5
        struct = frames[f]
        cx = 50+ws/2 
        cy = 50+hs/2+struct[14]
        nx = cx+sin(struct[0])*size*3
        ny = cy+cos(struct[0])*size*3
        lpx = nx+sin(struct[0]+90)*size
        lpy = ny+cos(struct[0]+90)*size
        rpx = nx+sin(struct[0]-90)*size
        rpy = ny+cos(struct[0]-90)*size
        lbx = cx+sin(struct[0]+90)*size
        lby = cy+cos(struct[0]+90)*size
        rbx = cx+sin(struct[0]-90)*size
        rby = cy+cos(struct[0]-90)*size
        hx = nx+sin(struct[13])*size
        hy = ny+cos(struct[13])*size
        rix = rbx+sin(struct[2])*size*2
        riy = rby+cos(struct[2])*size*2
        lix = lbx+sin(struct[1])*size*2
        liy = lby+cos(struct[1])*size*2
        rfx = rix+sin(struct[4])*size*2
        rfy = riy+cos(struct[4])*size*2
        lfx = lix+sin(struct[3])*size*2
        lfy = liy+cos(struct[3])*size*2
        rhx = rpx+sin(struct[8])*size*2
        rhy = rpy+cos(struct[8])*size*2
        lhx = lpx+sin(struct[7])*size*2
        lhy = lpy+cos(struct[7])*size*2
        rgx = rhx+sin(struct[10])*size*2
        rgy = rhy+cos(struct[10])*size*2
        lgx = lhx+sin(struct[9])*size*2
        lgy = lhy+cos(struct[9])*size*2
        pygame.draw.line(sc,(30/k,30/k,60/k), [cx, cy],[nx, ny],4)
        pygame.draw.line(sc,(30/k,30/k,60/k), [cx, cy],[lbx, lby],4)
        pygame.draw.line(sc,(30/k,30/k,60/k), [cx, cy],[rbx, rby],4)
        pygame.draw.line(sc,(30/k,30/k,60/k), [nx, ny],[lpx, lpy],4)
        pygame.draw.line(sc,(30/k,30/k,60/k), [nx, ny],[rpx, rpy],4)
        pygame.draw.line(sc,(30/k,30/k,60/k), [nx, ny],[hx, hy],4)
        pygame.draw.line(sc,(30/k,30/k,60/k), [lbx, lby],[lix, liy],4)
        pygame.draw.line(sc,(30/k,30/k,60/k), [rbx, rby],[rix, riy],4)
        pygame.draw.line(sc,(30/k,30/k,60/k), [lpx, lpy],[lhx, lhy],4)
        pygame.draw.line(sc,(30/k,30/k,60/k), [rpx, rpy],[rhx, rhy],4)
        pygame.draw.line(sc,(30/k,30/k,60/k), [lgx, lgy],[lhx, lhy],4)
        pygame.draw.line(sc,(30/k,30/k,60/k), [rgx, rgy],[rhx, rhy],4)
        pygame.draw.line(sc,(30/k,30/k,60/k), [lix, liy],[lfx, lfy],4)
        pygame.draw.line(sc,(30/k,30/k,60/k), [rix, riy],[rfx, rfy],4)
        f+=1
    
    #ОТОБРАЖЕНИЕ СКЕЛЕТА
    struct = frames[frame]
    cx = 50+ws/2 
    cy = 50+hs/2+struct[14]
    if hit(cx, cy, size/2, mx,my,2) and fixed == -2 or fixed == -1:
        if lClicked:
            fixed = -1
            lastFixed = [50+ws/2,50+hs/2]
        moved[0]+=(30-moved[0])/3
    else:
        moved[0]+=(-moved[0])/3
    nx = cx+sin(struct[0])*size*3
    ny = cy+cos(struct[0])*size*3
    if hit(nx, ny, size/2, mx,my,2) and fixed == -2 or fixed == 0:
        if lClicked:
            fixed = 0
            lastFixed = [cx,cy]
        moved[14]+=(30-moved[14])/2
    else:
        moved[14]+=(-moved[14])/2
    lpx = nx+sin(struct[0]+90)*size
    lpy = ny+cos(struct[0]+90)*size
    rpx = nx+sin(struct[0]-90)*size
    rpy = ny+cos(struct[0]-90)*size
    lbx = cx+sin(struct[0]+90)*size
    lby = cy+cos(struct[0]+90)*size
    rbx = cx+sin(struct[0]-90)*size
    rby = cy+cos(struct[0]-90)*size
    hx = nx+sin(struct[13])*size
    hy = ny+cos(struct[13])*size
    if hit(hx, hy, size/2, mx,my,2) and fixed == -2 or fixed == 13:
        if lClicked:
            fixed = 13
            lastFixed = [nx,ny]
        moved[13]+=(30-moved[13])/2
    else:
        moved[13]+=(-moved[13])/2
    lhx = lpx+sin(struct[7])*size*2
    lhy = lpy+cos(struct[7])*size*2
    if hit(lhx, lhy, size/2, mx,my,2) and fixed == -2 or fixed == 7:
        if lClicked:
            fixed = 7
            lastFixed = [lpx,lpy]
        moved[7]+=(30-moved[7])/2
    else:
        moved[7]+=(-moved[7])/2
    rhx = rpx+sin(struct[8])*size*2
    rhy = rpy+cos(struct[8])*size*2
    if hit(rhx, rhy, size/2, mx,my,2) and fixed == -2 or fixed == 8:
        if lClicked:
            fixed = 8
            lastFixed = [rpx,rpy]
        moved[8]+=(30-moved[8])/2
    else:
        moved[8]+=(-moved[8])/2
    lgx = lhx+sin(struct[9])*size*2
    lgy = lhy+cos(struct[9])*size*2
    if hit(lgx, lgy, size/2, mx,my,2) and fixed == -2 or fixed == 9:
        if lClicked:
            fixed = 9
            lastFixed = [lhx,lhy]
        moved[9]+=(30-moved[9])/2
    else:
        moved[9]+=(-moved[9])/2
    rgx = rhx+sin(struct[10])*size*2
    rgy = rhy+cos(struct[10])*size*2
    if hit(rgx, rgy, size/2, mx,my,2) and fixed == -2 or fixed == 10:
        if lClicked:
            fixed = 10
            lastFixed = [rhx,rhy]
        moved[10]+=(30-moved[10])/2
    else:
        moved[10]+=(-moved[10])/2
    lix = lbx+sin(struct[1])*size*2
    liy = lby+cos(struct[1])*size*2
    if hit(lix, liy, size/2, mx,my,2) and fixed == -2 or fixed == 1:
        if lClicked:
            fixed = 1
            lastFixed = [lbx,lby]
        moved[1]+=(30-moved[1])/2
    else:
        moved[1]+=(-moved[1])/2
    rix = rbx+sin(struct[2])*size*2
    riy = rby+cos(struct[2])*size*2
    if hit(rix, riy, size/2, mx,my,2) and fixed == -2 or fixed == 2:
        if lClicked:
            fixed = 2
            lastFixed = [rbx,rby]
        moved[2]+=(30-moved[2])/2
    else:
        moved[2]+=(-moved[2])/2
    lfx = lix+sin(struct[3])*size*2
    lfy = liy+cos(struct[3])*size*2
    if hit(lfx, lfy, size/2, mx,my,2) and fixed == -2 or fixed == 3:
        if lClicked:
            fixed = 3
            lastFixed = [lix,liy]
        moved[3]+=(30-moved[3])/2
    else:
        moved[3]+=(-moved[3])/2
    rfx = rix+sin(struct[4])*size*2
    rfy = riy+cos(struct[4])*size*2
    if hit(rfx, rfy, size/2, mx,my,2) and fixed == -2 or fixed == 4:
        if lClicked:
            fixed = 4
            lastFixed = [rix,riy]
        moved[4]+=(30-moved[4])/2
    else:
        moved[4]+=(-moved[4])/2
    
    if fixed>=0 and fixed<14:
        frames[frame][fixed] = deg(mx,my,lastFixed[0], lastFixed[1])
    if fixed == -1:
        frames[frame][14] = my-hs/2-50
    
    
    pygame.draw.line(sc,(100,100,120), [cx, cy],[nx, ny],3)
    pygame.draw.line(sc,(100,100,120), [cx, cy],[lbx, lby],3)
    pygame.draw.line(sc,(100,100,120), [cx, cy],[rbx, rby],3)
    pygame.draw.line(sc,(100,100,120), [nx, ny],[lpx, lpy],3)
    pygame.draw.line(sc,(100,100,120), [nx, ny],[rpx, rpy],3)
    pygame.draw.line(sc,(100,100,120), [nx, ny],[hx, hy],3)
    pygame.draw.line(sc,(100,100,120), [lbx, lby],[lix, liy],3)
    pygame.draw.line(sc,(100,100,120), [rbx, rby],[rix, riy],3)
    pygame.draw.line(sc,(100,100,120), [lpx, lpy],[lhx, lhy],3)
    pygame.draw.line(sc,(100,100,120), [rpx, rpy],[rhx, rhy],3)
    pygame.draw.line(sc,(100,100,120), [lgx, lgy],[lhx, lhy],3)
    pygame.draw.line(sc,(100,100,120), [rgx, rgy],[rhx, rhy],3)
    pygame.draw.line(sc,(100,100,120), [lix, liy],[lfx, lfy],3)
    pygame.draw.line(sc,(100,100,120), [rix, riy],[rfx, rfy],3)
    
    #ЦЕНТРАЛЬНАЯ ТОЧКА
    pygame.draw.rect(sc, (20,170,0), (cx-7, cy-5,14,10))
    pygame.draw.rect(sc, (20,170,0), (cx-5, cy-7,10,14))
    
    #ТОЧКА ШЕИ
    pygame.draw.rect(sc, (150,110,0), (nx-7, ny-5,14,10))
    pygame.draw.rect(sc, (150,110,0), (nx-5, ny-7,10,14))
    
    #ТОЧКИ ПЛЕЧЕЙ
    pygame.draw.rect(sc, (70,70,70), (lpx-7, lpy-5,14,10))
    pygame.draw.rect(sc, (70,70,70), (lpx-5, lpy-7,10,14))
    pygame.draw.rect(sc, (70,70,70), (rpx-7, rpy-5,14,10))
    pygame.draw.rect(sc, (70,70,70), (rpx-5, rpy-7,10,14))
    
    #ТОЧКИ БЁДЕР
    pygame.draw.rect(sc, (70,70,70), (lbx-7, lby-5,14,10))
    pygame.draw.rect(sc, (70,70,70), (lbx-5, lby-7,10,14))
    pygame.draw.rect(sc, (70,70,70), (rbx-7, rby-5,14,10))
    pygame.draw.rect(sc, (70,70,70), (rbx-5, rby-7,10,14))
    
    #ТОЧКА ГОЛОВЫ
    pygame.draw.rect(sc, (240,60,0), (hx-7, hy-5,14,10))
    pygame.draw.rect(sc, (240,60,0), (hx-5, hy-7,10,14))
    pygame.draw.line(sc, (180,30,0), [hx+sin(struct[13]-90)*size*2,hy+cos(struct[13]-90)*size*2],[hx+sin(struct[13]+90)*size*2,hy+cos(struct[13]+90)*size*2],5)
    pygame.draw.line(sc, (180,30,0), [hx,hy],[hx+sin(struct[13])*size/2,hy+cos(struct[13])*size/2],30)
    pygame.draw.line(sc, (120,20,0), [hx+sin(struct[13])*4,hy+cos(struct[13])*4],[hx+sin(struct[13])*size/4,hy+cos(struct[13])*size/4],30)
    
    #ТОЧКИ РУК
    pygame.draw.rect(sc, (150,110,0), (lhx-7, lhy-5,14,10))
    pygame.draw.rect(sc, (150,110,0), (lhx-5, lhy-7,10,14))
    pygame.draw.rect(sc, (150,110,0), (rhx-7, rhy-5,14,10))
    pygame.draw.rect(sc, (150,110,0), (rhx-5, rhy-7,10,14))
    
    #ТОЧКИ КОЛЕНЕЙ
    pygame.draw.rect(sc, (150,110,0), (lix-7, liy-5,14,10))
    pygame.draw.rect(sc, (150,110,0), (lix-5, liy-7,10,14))
    pygame.draw.rect(sc, (150,110,0), (rix-7, riy-5,14,10))
    pygame.draw.rect(sc, (150,110,0), (rix-5, riy-7,10,14))
    
    #ТОЧКИ КИСТЕЙ
    pygame.draw.line(sc, (120,0,80), [lgx,lgy], [lgx+sin(struct[9]-45)*size/2,lgy+cos(struct[9]-45)*size/2], 5)
    pygame.draw.line(sc, (120,0,80), [lgx,lgy], [lgx+sin(struct[9]+45)*size/2,lgy+cos(struct[9]+45)*size/2], 5)
    pygame.draw.rect(sc, (150,0,110), (lgx-7, lgy-5,14,10))
    pygame.draw.rect(sc, (150,0,110), (lgx-5, lgy-7,10,14))
    pygame.draw.line(sc, (120,0,80), [rgx,rgy], [rgx+sin(struct[10]-45)*size/2,rgy+cos(struct[10]-45)*size/2], 5)
    pygame.draw.line(sc, (120,0,80), [rgx,rgy], [rgx+sin(struct[10]+45)*size/2,rgy+cos(struct[10]+45)*size/2], 5)
    pygame.draw.rect(sc, (150,0,110), (rgx-7, rgy-5,14,10))
    pygame.draw.rect(sc, (150,0,110), (rgx-5, rgy-7,10,14))
    
    #ТОЧКИ СТУПНЕЙ
    pygame.draw.line(sc, (120,0,80), [lfx,lfy], [lfx+sin(struct[3])*size/6,lfy+cos(struct[3])*size/6], 30)
    pygame.draw.rect(sc, (150,0,110), (lfx-7, lfy-5,14,10))
    pygame.draw.rect(sc, (150,0,110), (lfx-5, lfy-7,10,14))
    pygame.draw.line(sc, (120,0,80), [rfx,rfy], [rfx+sin(struct[4])*size/6,rfy+cos(struct[4])*size/6], 30)
    pygame.draw.rect(sc, (150,0,110), (rfx-7, rfy-5,14,10))
    pygame.draw.rect(sc, (150,0,110), (rfx-5, rfy-7,10,14))
    
    #СКАНЕР
    pygame.draw.rect(sc, (20,170,0), (cx-moved[0]/2, cy-moved[0]/2, moved[0], moved[0]),2)
    if fixed == -1:
        pygame.draw.line(sc, (20,170,255), [cx-moved[0], cy],[cx-moved[0]/3, cy],3)
        pygame.draw.line(sc, (20,170,255), [cx, cy-moved[0]],[cx, cy-moved[0]/3],3)
        pygame.draw.line(sc, (20,170,255), [cx, cy+moved[0]],[cx, cy+moved[0]/3],3)
    if moved[0]>2:
        text = nfont.render("ЦЕНТР", True, (20,170,0))
        sc.blit(text, (cx+moved[0]/2+2, cy-moved[0]/4))
    pygame.draw.rect(sc, (150,110,0), (nx-moved[14]/2, ny-moved[14]/2, moved[14], moved[14]),2)
    if fixed == 0:
        pygame.draw.line(sc, (150,110,255), [nx-moved[14], ny],[nx-moved[14]/3, ny],3)
        pygame.draw.line(sc, (150,110,255), [nx, ny-moved[14]],[nx, ny-moved[14]/3],3)
        pygame.draw.line(sc, (150,110,255), [nx, ny+moved[14]],[nx, ny+moved[14]/3],3)
    if moved[14]>2:
        text = nfont.render("ШЕЯ", True, (150,110,0))
        sc.blit(text, (nx+moved[14]/2+2, ny-moved[14]/4))
    pygame.draw.rect(sc, (240,60,0), (hx-moved[13]/2, hy-moved[13]/2, moved[13], moved[13]),2)
    if fixed == 13:
        pygame.draw.line(sc, (240,60,255), [hx-moved[13], hy],[hx-moved[13]/3, hy],3)
        pygame.draw.line(sc, (240,60,255), [hx, hy-moved[13]],[hx, hy-moved[13]/3],3)
        pygame.draw.line(sc, (240,60,255), [hx, hy+moved[13]],[hx, hy+moved[13]/3],3)
    if moved[13]>2:
        text = nfont.render("ГОЛОВА", True, (240,60,0))
        sc.blit(text, (hx+moved[13]/2+2, hy-moved[13]/4))
    pygame.draw.rect(sc, (150,110,0), (lhx-moved[7]/2, lhy-moved[7]/2, moved[7], moved[7]),2)
    if fixed == 7:
        pygame.draw.line(sc, (150,110,255), [lhx-moved[7], lhy],[lhx-moved[7]/3, lhy],3)
        pygame.draw.line(sc, (150,110,255), [lhx, lhy-moved[7]],[lhx, lhy-moved[7]/3],3)
        pygame.draw.line(sc, (150,110,255), [lhx, lhy+moved[7]],[lhx, lhy+moved[7]/3],3)
    if moved[7]>2:
        text = nfont.render("ЛЕВЫЙ ЛОКОТЬ", True, (150,110,0))
        sc.blit(text, (lhx+moved[7]/2+2, lhy-moved[7]/4))
    pygame.draw.rect(sc, (150,110,0), (rhx-moved[8]/2, rhy-moved[8]/2, moved[8], moved[8]),2)
    if fixed == 8:
        pygame.draw.line(sc, (150,110,255), [rhx-moved[8], rhy],[rhx-moved[8]/3, rhy],3)
        pygame.draw.line(sc, (150,110,255), [rhx, rhy-moved[8]],[rhx, rhy-moved[8]/3],3)
        pygame.draw.line(sc, (150,110,255), [rhx, rhy+moved[8]],[rhx, rhy+moved[8]/3],3)
    if moved[8]>2:
        text = nfont.render("ПРАВЫЙ ЛОКОТЬ", True, (150,110,0))
        sc.blit(text, (rhx+moved[8]/2+2, rhy-moved[8]/4))
    pygame.draw.rect(sc, (150,0,110), (lgx-moved[9]/2, lgy-moved[9]/2, moved[9], moved[9]),2)
    if fixed == 9:
        pygame.draw.line(sc, (150,255,110), [lgx-moved[9], lgy],[lgx-moved[9]/3, lgy],3)
        pygame.draw.line(sc, (150,255,110), [lgx, lgy-moved[9]],[lgx, lgy-moved[9]/3],3)
        pygame.draw.line(sc, (150,255,110), [lgx, lgy+moved[9]],[lgx, lgy+moved[9]/3],3)
    if moved[9]>2:
        text = nfont.render("ЛЕВАЯ КИСТЬ", True, (150,0,110))
        sc.blit(text, (lgx+moved[9]/2+2, lgy-moved[9]/4))
    pygame.draw.rect(sc, (150,0,110), (rgx-moved[10]/2, rgy-moved[10]/2, moved[10], moved[10]),2)
    if fixed == 10:
        pygame.draw.line(sc, (150,255,110), [rgx-moved[10], rgy],[rgx-moved[10]/3, rgy],3)
        pygame.draw.line(sc, (150,255,110), [rgx, rgy-moved[10]],[rgx, rgy-moved[10]/3],3)
        pygame.draw.line(sc, (150,255,110), [rgx, rgy+moved[10]],[rgx, rgy+moved[10]/3],3)
    if moved[10]>2:
        text = nfont.render("ПРАВАЯ КИСТЬ", True, (150,0,110))
        sc.blit(text, (rgx+moved[10]/2+2, rgy-moved[10]/4))
    pygame.draw.rect(sc, (150,110,0), (lix-moved[1]/2, liy-moved[1]/2, moved[1], moved[1]),2)
    if fixed == 1:
        pygame.draw.line(sc, (150,110,255), [lix-moved[1], liy],[lix-moved[1]/3, liy],3)
        pygame.draw.line(sc, (150,110,255), [lix, liy-moved[1]],[lix, liy-moved[1]/3],3)
        pygame.draw.line(sc, (150,110,255), [lix, liy+moved[1]],[lix, liy+moved[1]/3],3)
    if moved[1]>2:
        text = nfont.render("ЛЕВОЕ КОЛЕНО", True, (150,110,0))
        sc.blit(text, (lix+moved[1]/2+2, liy-moved[1]/4))
    pygame.draw.rect(sc, (150,110,0), (rix-moved[2]/2, riy-moved[2]/2, moved[2], moved[2]),2)
    if fixed == 2:
        pygame.draw.line(sc, (150,110,255), [rix-moved[2], riy],[rix-moved[2]/3, riy],3)
        pygame.draw.line(sc, (150,110,255), [rix, riy-moved[2]],[rix, riy-moved[2]/3],3)
        pygame.draw.line(sc, (150,110,255), [rix, riy+moved[2]],[rix, riy+moved[2]/3],3)
    if moved[2]>2:
        text = nfont.render("ПРАВОЕ КОЛЕНО", True, (150,110,0))
        sc.blit(text, (rix+moved[2]/2+2, riy-moved[2]/4))
    pygame.draw.rect(sc, (150,0,110), (lfx-moved[3]/2, lfy-moved[3]/2, moved[3], moved[3]),2)
    if fixed == 3:
        pygame.draw.line(sc, (150,255,110), [lfx-moved[3], lfy],[lfx-moved[3]/3, lfy],3)
        pygame.draw.line(sc, (150,255,110), [lfx, lfy-moved[3]],[lfx, lfy-moved[3]/3],3)
        pygame.draw.line(sc, (150,255,110), [lfx, lfy+moved[3]],[lfx, lfy+moved[3]/3],3)
    if moved[3]>2:
        text = nfont.render("ЛЕВАЯ СТУПНЯ", True, (150,0,110))
        sc.blit(text, (lfx+moved[3]/2+2, lfy-moved[3]/4))
    pygame.draw.rect(sc, (150,0,110), (rfx-moved[4]/2, rfy-moved[4]/2, moved[4], moved[4]),2)
    if fixed == 4:
        pygame.draw.line(sc, (150,255,110), [rfx-moved[4], rfy],[rfx-moved[4]/3, rfy],3)
        pygame.draw.line(sc, (150,255,110), [rfx, rfy-moved[4]],[rfx, rfy-moved[4]/3],3)
        pygame.draw.line(sc, (150,255,110), [rfx, rfy+moved[4]],[rfx, rfy+moved[4]/3],3)
    if moved[4]>2:
        text = nfont.render("ПРАВАЯ СТУПНЯ", True, (150,0,110))
        sc.blit(text, (rfx+moved[4]/2+2, rfy-moved[4]/4))
    
    #ОТОБРАЖЕНИЕ КАДРОВ
    i = 0
    for f in frames:
        pygame.draw.rect(sc, (20,20,40), (60+i*40,h-40,30,30))
        if frame == i:
            pygame.draw.rect(sc, (50,50,100), (60+i*40+5,h-40+5,20,20))
            text = sfont.render(str(i), True, (200,200,200))
            sc.blit(text, (60+i*40+5+5,h-40+5+3,20,20))
        i+=1
    
    if play:
        toPlay -= 1
        if toPlay <= 0:
            next()
            toPlay = 2
        pygame.draw.rect(sc, (200,200,200), (10, 80, 10+toPlay*2, 10+toPlay*2))
        pygame.draw.rect(sc, (100,200,100), (12.5, 82.5, 5, 5))
    
    #КНОПКА ВЫХОДА ИЗ ПРОГРАММЫ
    pygame.draw.rect(sc, (120+exitTouched,10+exitTouched,10+exitTouched), (w-45,5,40,40))
    pygame.draw.line(sc, white, [w-35,35],[w-15,15],3)
    pygame.draw.line(sc, white, [w-35,15],[w-15,35],3)
    
    #ОТОБРАЖЕНИЕ СОХРАНЕНИЙ
    if savedState>0.5:
        pygame.draw.rect(sc, (100,200,100), (0,0,w,h), 2)
        text = bfont.render("Сохранено! "+lastSaved, True, (40,200,40))
        sc.blit(text, (20, 20))
    savedState+=(-savedState)/10
    
    #ОТОБРАЖЕНИЕ КОПИЙ
    if copiedState>0.5:
        pygame.draw.rect(sc, (100,100,200), (0,0,w,h), 2)
        text = bfont.render("Скопировано!", True, (40,40,200))
        sc.blit(text, (20, 50))
    copiedState+=(-copiedState)/10
    
    #КУРСОР
    s = 8
    if lClicked:
        s+=4
    pygame.draw.line(sc, white, [mx-s,my], [mx-s/4,my], 2)
    pygame.draw.line(sc, white, [mx,my-s], [mx,my-s/4], 2)
    pygame.draw.line(sc, white, [mx+s,my], [mx+s/4,my], 2)
    pygame.draw.line(sc, white, [mx,my+s], [mx,my+s/4], 2)
    
    #ОБНОВЛЕНИЕ ЭКРАНА
    pygame.display.update()
    time.sleep(0.015)
exit()