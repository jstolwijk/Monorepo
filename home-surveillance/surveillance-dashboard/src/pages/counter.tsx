import React from "react";

interface CounterProps {
  value: number;
  onIncrement: () => void;
  onIncrementAsync: () => void;
  onIncrementIfOdd: () => void;
  onDecrement: () => void;
}

const Counter: React.FC<CounterProps> = ({
  value,
  onIncrement,
  onIncrementAsync,
  onDecrement,
  onIncrementIfOdd
}) => (
  <p>
    Clicked: {value} times <button onClick={onIncrement}>+</button>{" "}
    <button onClick={onDecrement}>-</button>{" "}
    <button onClick={onIncrementIfOdd}>Increment if odd</button>{" "}
    <button onClick={onIncrementAsync}>Increment async</button>
  </p>
);

export default Counter;
