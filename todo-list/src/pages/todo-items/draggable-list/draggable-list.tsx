// Original: https://github.com/chenglou/react-motion/tree/master/demos/demo8-draggable-list

import { render } from "react-dom";
import React, { useRef, useState, useEffect } from "react";
import clamp from "lodash-es/clamp";
import swap from "./move";
import { useGesture } from "react-with-gesture";
import { useSprings, animated, interpolate, useTransition } from "react-spring";
import "./styles.css";

interface SpringStyleProps {
  y: number;
  scale: number;
  zIndex: string;
  shadow: number;
  immediate: boolean | ((n: String) => boolean);
}

// Returns fitting styles for dragged/idle items
const fn = (
  order: any,
  down: boolean,
  originalIndex: number,
  curIndex: number,
  y: number
) => (index: number): SpringStyleProps => {
  return down && index === originalIndex
    ? {
        y: curIndex * 100 + y,
        scale: 1.1,
        zIndex: "1",
        shadow: 15,
        immediate: (n: String) => n === "y" || n === "zIndex"
      }
    : createMovedSpring(order)(index);
};

const createMovedSpring = (order: any) => (index: number): SpringStyleProps => {
  return {
    y: order.indexOf(index) * 100,
    scale: 1,
    zIndex: "0",
    shadow: 1,
    immediate: false
  };
};

interface DraggableListProps {
  initialItems: string[];
  initialOrder: number[];
  updateOrderCallback: (newOrder: number[]) => void;
}

const DraggableList: React.FunctionComponent<DraggableListProps> = ({
  initialItems,
  updateOrderCallback,
  initialOrder
}) => {
  const [springs, setSprings] = useSprings(
    initialItems.length,
    createMovedSpring(initialOrder)
  );

  const order = useRef(initialOrder);
  const items = useRef(initialItems);

  useEffect(() => {
    if (initialItems.length !== items.current.length) {
      console.log("new item added");
    }
    items.current = initialItems;
    order.current = initialOrder;
  }, [initialOrder, initialItems]);

  const bind = useGesture(({ args: [originalIndex], down, delta: [x, y] }) => {
    const curIndex = order.current.indexOf(originalIndex);
    const curRow = clamp(
      Math.round((curIndex * 100 + y) / 100),
      0,
      items.current.length - 1
    );

    const newOrder = swap(order.current, curIndex, curRow);
    setSprings(fn(newOrder, down, originalIndex, curIndex, y) as any); // Feed springs new style data, they'll animate the view without causing a single render

    if (!down) {
      order.current = newOrder;
      updateOrderCallback(newOrder);
    }
  });

  return (
    <div>
      <div className="content" style={{ height: initialItems.length * 100 }}>
        {springs.map(({ zIndex, shadow, y, scale }, index) => (
          <animated.div
            {...bind(index)}
            key={index}
            style={
              {
                zIndex,
                boxShadow: shadow.interpolate(
                  (s: number) => `rgba(0, 0, 0, 0.15) 0px ${s}px ${2 * s}px 0px`
                ),
                transform: interpolate(
                  [y, scale],
                  (y, s) => `translate3d(0,${y}px,0) scale(${s})`
                )
              } as any
            }
            children={initialItems[index]}
          />
        ))}
      </div>
    </div>
  );
};

export default DraggableList;
