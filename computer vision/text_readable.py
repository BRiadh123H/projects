import cv2 as cv

img= cv.imread('p.png')
if img is None:
    print("Image not found!")
    exit()
img= cv.cvtColor(img, cv.COLOR_RGB2GRAY)

adaptive_result = cv.adaptiveThreshold(img, 255, cv.ADAPTIVE_THRESH_GAUSSIAN_C, cv.THRESH_BINARY, 201, 7)
_, result = cv.threshold(img, 27, 255, cv.THRESH_BINARY)
cv.imshow('image', adaptive_result )
cv.imshow('result', result)
key = cv.waitKey(0)
if key == ord('q'):
    cv.destroyAllWindows()
cv.destroyAllWindows()