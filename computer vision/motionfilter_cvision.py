import cv2 as cv

video=cv.VideoCapture('people.mp4')
f=cv.createBackgroundSubtractorMOG2(300,200)

while True:
    ret , frame = video.read()
    if ret :
        mask =f.apply(frame)
        cv.imshow('frame',frame)
        cv.imshow('mask',mask)
        if cv.waitKey (30) == ord('q'):
            break
    else:
        video=cv.VideoCapture('people.mp4')
cv.destroyAllWindows()
video.release()