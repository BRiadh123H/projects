import cv2 as cv
import numpy as np

camera = cv.VideoCapture(0)
f = cv.createBackgroundSubtractorMOG2(300, 200)

mode = 4   # start with normal view
print("1.Laplacian Edge Detection") 
print("2.Canny Edge Detection") 
print("3.Motion Detection")
while True:
    ret, frame = camera.read()
    if not ret:
        break

    frame = cv.flip(frame, 1)
    frame = cv.resize(frame, (1080, 720))

    # --- Select mode based on key presses ---
    key = cv.waitKey(1) & 0xFF  
    if key == ord('1'):
        mode = 1
    elif key == ord('2'):
        mode = 2
    elif key == ord('3'):
        mode = 3
    elif key == ord('4'):
        mode = 4
    elif key == ord('q'):
        break

    # --- Display based on mode ---
    if mode == 1:
        laplacian = cv.Laplacian(frame, cv.CV_64F)
        laplacian = np.uint8(laplacian)
        cv.imshow("Webcam", laplacian)

    elif mode == 2:
        edges = cv.Canny(frame, 100, 200)
        cv.imshow("Webcam", edges)

    elif mode == 3:
        mask = f.apply(frame)
        cv.imshow("Webcam", mask)

    else:
        cv.imshow("Webcam", frame)

camera.release()
cv.destroyAllWindows()
