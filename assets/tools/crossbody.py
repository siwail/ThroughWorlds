import pygame, time, math

def save():
    global savedState
    print("Сохранено!")
    savedState=10
black = (10,10,30)
white = (240,240,240)

blank = {"head":{"x": 0, "y": 0, "r":0, "dir": 1}, "neck": {}}
frames = []
frame = 0

pygame.init()
w = 900
h = 900
mx = 0
my = 0
savedState = 0
lClicked = False
sc = pygame.display.set_mode((w,h), pygame.NOFRAME)
pygame.mouse.set_visible(False)
pygame.font.init() 
bfont = pygame.font.SysFont('Consolas', 35)
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
            if e.button == 1:
                lClicked = False
        if e.type == pygame.KEYDOWN:
            if e.key == pygame.K_s:
                save()
    savedState+=(-savedState)/10
    exitTouched = 0
    if mx>w-45 and mx<w-5 and my > 5 and my < 45:
        exitTouched+=50
        if lClicked:
            exitTouched+=50
            run = False
    pygame.draw.rect(sc, black, (0,0,w,h))
    ix = 0
    while ix < 40:
        iy = 0
        while iy < 40:
            if ix % 2 == (iy+1)%2:
                pygame.draw.rect(sc, (8,8,16), (ix*w/40+2.5,iy*h/40+2.5,w/40-5,h/40-5))
            iy+=1
        ix+=1
    pygame.draw.rect(sc, (200/(savedState+1),200-savedState*5,200/(savedState+1)), (0,0,w,h), 1)
    pygame.draw.rect(sc, (120+exitTouched,10+exitTouched,10+exitTouched), (w-45,5,40,40))
    if savedState>0.5:
        text = bfont.render("Сохранено!", True, (40,200,40))
        sc.blit(text, (20, 20))
    s = 8
    if lClicked:
        s+=4
    
    pygame.draw.line(sc, white, [mx-s,my], [mx-s/4,my], 2)
    pygame.draw.line(sc, white, [mx,my-s], [mx,my-s/4], 2)
    pygame.draw.line(sc, white, [mx+s,my], [mx+s/4,my], 2)
    pygame.draw.line(sc, white, [mx,my+s], [mx,my+s/4], 2)
    pygame.display.update()
    time.sleep(0.015)
exit()