import React, { useState } from "react";
import "./tic-tac-toe.css";

enum Square {
  X = "X",
  O = "O",
  EMPTY = ""
}

enum GameState {
  IN_PROGRESS,
  DRAW,
  HAS_WINNER
}

const EMPTY_BOARD = Array(9).fill(Square.EMPTY);

const last = <T extends {}>(xs: T[]): T => {
  return xs[xs.length - 1];
};

const setValueAtIndex = <T extends {}>(xs: T[], x: T, index: number): T[] => {
  return [...xs.slice(0, index), x, ...xs.slice(index + 1, xs.length)];
};

const TicTacToe = () => {
  const [boards, setBoards] = useState<Square[][]>([EMPTY_BOARD]);
  const [current, setCurrent] = useState(0);
  const [isXTurn, setIsXTurn] = useState(true);
  const [gameState, setGameState] = useState(GameState.IN_PROGRESS);

  const handleSquareClick = (index: number) => {
    const currentBoard = last(boards);

    const newBoard = setValueAtIndex(
      currentBoard,
      isXTurn ? Square.X : Square.O,
      index
    );

    setBoards([...boards, newBoard]);
    setCurrent(current + 1);

    if (hasGameAWinner(newBoard)) {
      setGameState(GameState.HAS_WINNER);
    } else if (current >= 8) {
      setGameState(GameState.DRAW);
    } else {
      setIsXTurn(!isXTurn);
    }
  };

  const hasGameAWinner = (board: Square[]): boolean => {
    const gameHasWinnerStates = [
      [0, 1, 2],
      [3, 4, 5],
      [6, 7, 8],
      [0, 3, 6],
      [1, 4, 7],
      [2, 5, 8],
      [0, 4, 8],
      [2, 4, 6]
    ];

    return gameHasWinnerStates.some(
      ([first, second, thrid]) =>
        board[first] !== Square.EMPTY &&
        board[first] === board[second] &&
        board[second] === board[thrid]
    );
  };

  const getGameStateMessage = () => {
    if (current < boards.length - 1) {
      return "REPLAY";
    } else if (gameState === GameState.HAS_WINNER) {
      return `${isXTurn ? "X" : "O"} WON!`;
    } else if (gameState === GameState.DRAW) {
      return `ITS A DRAW!`;
    } else {
      return `It's ${isXTurn ? "X" : "O"}'s turn!`;
    }
  };

  return (
    <div>
      {getGameStateMessage()}
      <Board
        className="main-board"
        board={boards[current]}
        handleSquareClick={handleSquareClick}
        allowClicks={
          current === boards.length - 1 && gameState === GameState.IN_PROGRESS
        }
      />
      <button onClick={() => setCurrent(current - 1)}>{"<"}</button>
      <button onClick={() => setCurrent(current + 1)}>{">"}</button>
    </div>
  );
};

interface BoardProps {
  className: string;
  board: Square[];
  handleSquareClick: (index: number) => void;
  allowClicks: boolean;
}

const Board: React.FunctionComponent<BoardProps> = ({
  className,
  board,
  handleSquareClick,
  allowClicks
}) => {
  return (
    <div className={className}>
      {board.map((square, index) => (
        <div
          className="cell"
          onClick={() => {
            allowClicks ? handleSquareClick(index) : {};
          }}
        >
          {square}
        </div>
      ))}
    </div>
  );
};

export default TicTacToe;
