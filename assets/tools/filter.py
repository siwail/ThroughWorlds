import cv2
import numpy as np
name = input("Название файла: ")
img = cv2.imread("../images/"+name, cv2.IMREAD_UNCHANGED)
height, width = img.shape[:2]
for y in range(height):
    for x in range(width):
        r,g,b,a = img[y][x]
        if a<255 or r>150:
            a = 0
            img[y][x] = [r,g,b,a]
outputName = name.split(".")[0]+"_r."+name.split(".")[1]
print(outputName)
cv2.imwrite(outputName, img)
