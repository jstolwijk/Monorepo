from __future__ import print_function
import cv2 as cv
import argparse
import collections
import numpy


# import the necessary packages
import numpy as np

# Malisiewicz et al.


def non_max_suppression_fast(boxes, overlapThresh):
    # if there are no boxes, return an empty list
    if len(boxes) == 0:
        return []

    # if the bounding boxes integers, convert them to floats --
    # this is important since we'll be doing a bunch of divisions
    if boxes.dtype.kind == "i":
        boxes = boxes.astype("float")

    # initialize the list of picked indexes
    pick = []

    # grab the coordinates of the bounding boxes
    x1 = boxes[:, 0]
    y1 = boxes[:, 1]
    x2 = boxes[:, 2]
    y2 = boxes[:, 3]

    # compute the area of the bounding boxes and sort the bounding
    # boxes by the bottom-right y-coordinate of the bounding box
    area = (x2 - x1 + 1) * (y2 - y1 + 1)
    idxs = numpy.argsort(y2)

    # keep looping while some indexes still remain in the indexes
    # list
    while len(idxs) > 0:
        # grab the last index in the indexes list and add the
        # index value to the list of picked indexes
        last = len(idxs) - 1
        i = idxs[last]
        pick.append(i)

        # find the largest (x, y) coordinates for the start of
        # the bounding box and the smallest (x, y) coordinates
        # for the end of the bounding box
        xx1 = numpy.maximum(x1[i], x1[idxs[:last]])
        yy1 = numpy.maximum(y1[i], y1[idxs[:last]])
        xx2 = numpy.minimum(x2[i], x2[idxs[:last]])
        yy2 = numpy.minimum(y2[i], y2[idxs[:last]])

        # compute the width and height of the bounding box
        w = numpy.maximum(0, xx2 - xx1 + 1)
        h = numpy.maximum(0, yy2 - yy1 + 1)

        # compute the ratio of overlap
        overlap = (w * h) / area[idxs[:last]]

        # delete all indexes from the index list that have
        idxs = numpy.delete(idxs, numpy.concatenate(([last],
                                                     numpy.where(overlap > overlapThresh)[0])))

    # return only the bounding boxes that were picked using the
    # integer data type
    return boxes[pick].astype("int")


def resize(image, width=None, height=None, inter=cv.INTER_AREA):
    # initialize the dimensions of the image to be resized and
    # grab the image size
    dim = None
    (h, w) = image.shape[:2]

    # if both the width and height are None, then return the
    # original image
    if width is None and height is None:
        return image

    # check to see if the width is None
    if width is None:
        # calculate the ratio of the height and construct the
        # dimensions
        r = height / float(h)
        dim = (int(w * r), height)

    # otherwise, the height is None
    else:
        # calculate the ratio of the width and construct the
        # dimensions
        r = width / float(w)
        dim = (width, int(h * r))

    # resize the image
    resized = cv.resize(image, dim, interpolation=inter)

    # return the resized image
    return resized


def grab_contours(cnts):
    return cnts[0]


parser = argparse.ArgumentParser(
    description='This program shows how to use background subtraction methods provided by \
                                              OpenCV. You can process both videos and images.')
parser.add_argument('--input', type=str,
                    help='Path to a video or a sequence of image.', default='vtest.avi')

args = parser.parse_args()

hog = cv.HOGDescriptor()
hog.setSVMDetector(cv.HOGDescriptor_getDefaultPeopleDetector())

capture = cv.VideoCapture(cv.samples.findFileOrKeep(args.input))

if not capture.isOpened:
    print('Unable to open: ' + args.input)
    exit(0)

_, frame = capture.read()


def get_people_from_image(image):
    (rects, _weights) = hog.detectMultiScale(
        image, winStride=(8, 8), padding=(64, 64), scale=1.05)

    rects = numpy.array([[x, y, x + w, y + h] for (x, y, w, h) in rects])
    return non_max_suppression_fast(rects, 0.65)


def absolute_rects(rects, x, y):
    return [[x + cx, y + cy, x + cx + cw, y + ch + cy] for (cx, cy, cw, ch) in rects]


backSub = cv.createBackgroundSubtractorMOG2()
prevFrame = None

while True:
    _, frame = capture.read()

    if frame is None:
        break

    # frame = resize(frame, width=1000)

    gray = cv.cvtColor(frame, cv.COLOR_BGR2GRAY)
    gray = cv.GaussianBlur(gray, (21, 21), 0)

    if prevFrame is None:
        prevFrame = gray
        continue

    frameDelta = cv.absdiff(prevFrame, gray)
    thresh = cv.threshold(frameDelta, 25, 255, cv.THRESH_BINARY)[1]

    # dilate the thresholded image to fill in holes, then find contours
    # on thresholded image
    thresh = cv.dilate(thresh, None, iterations=2)
    cnts = cv.findContours(thresh.copy(), cv.RETR_EXTERNAL,
                           cv.CHAIN_APPROX_SIMPLE)

    cnts = grab_contours(cnts)

    rects = []
    # loop over the contours
    for c in cnts:
        # if the contour is too small, ignore it
        if cv.contourArea(c) < 5000:
            continue

        # compute the bounding box for the contour, draw it on the frame,
        # and update the text
        (x, y, w, h) = cv.boundingRect(c)

        motion_slice = frame[y:y+h, x:x+w]
        rect = get_people_from_image(motion_slice)
        rects.extend(absolute_rects(rect, x, y))

    for index, (x, y, w, h) in enumerate(rects):
        cv.rectangle(frame, (x, y), (w, h), (0, 0, 255), 2)

    cv.imshow('Frame', frame)
    cv.imshow('Movement', thresh)

    keyboard = cv.waitKey(30)
    if keyboard == 'q' or keyboard == 27:
        break
