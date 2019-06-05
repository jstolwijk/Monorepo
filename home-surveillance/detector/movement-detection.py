from __future__ import print_function
import cv2 as cv
import argparse
import collections
import numpy

parser = argparse.ArgumentParser(
    description='This program shows how to use background subtraction methods provided by \
                                              OpenCV. You can process both videos and images.')
parser.add_argument('--input', type=str,
                    help='Path to a video or a sequence of image.', default='vtest.avi')

parser.add_argument('--algo', type=str,
                    help='Background subtraction method (KNN, MOG2).', default='MOG2')

args = parser.parse_args()

if args.algo == 'MOG2':
    backSub = cv.createBackgroundSubtractorMOG2()
else:
    backSub = cv.createBackgroundSubtractorKNN()


if args.input == 'CAMERA':
    capture = cv.VideoCapture(0)
else:
    capture = cv.VideoCapture(cv.samples.findFileOrKeep(args.input))


# Can't open file
if not capture.isOpened:
    print('Unable to open: ' + args.input)
    exit(0)

_, frame = capture.read()
total_pixels = frame.size
threshold = int(total_pixels / 20)

while True:
    _, frame = capture.read()

    if frame is None:
        break

    # fgMask = backSub.apply(frame)
    blur = cv.GaussianBlur(backSub.apply(frame), (5, 5), 0)
    _, movement = cv.threshold(blur, 0, 255, cv.THRESH_BINARY+cv.THRESH_OTSU)
    fgMask = cv.Canny(movement, 100, 200)

    unique, counts = numpy.unique(fgMask, return_counts=True)
    d = dict(zip(unique, counts))

    movement = d.get(255, 0)

    # Ignore the first 10 frames
    if movement > threshold and capture.get(cv.CAP_PROP_POS_FRAMES) > 10:
        print('Call the police!')

    # Draw frame count rectangle
    cv.rectangle(frame, (10, 2), (100, 20), (255, 255, 255), -1)
    cv.putText(frame, str(capture.get(cv.CAP_PROP_POS_FRAMES)), (15, 15),
               cv.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 0))

    cv.imshow('Frame', frame)
    cv.imshow('FG Mask', fgMask)

    keyboard = cv.waitKey(30)
    if keyboard == 'q' or keyboard == 27:
        break
