import React, { useState } from "react";
import Counter from "./counter";
import { connect } from "react-redux";
import ScreenShare from "./screen-share";

interface MainProps {
  value: number;
  action: (type: string) => void;
}

function Main({ value, action }: MainProps) {
  const [screenShareEnabled, setScreenShare] = useState(false);

  return (
    <>
      <button onClick={() => setScreenShare(!screenShareEnabled)}>
        Toggle screen share
      </button>
      {screenShareEnabled && <ScreenShare />}
      <Counter
        value={value}
        onIncrement={() => action("INCREMENT")}
        onDecrement={() => action("DECREMENT")}
        onIncrementIfOdd={() => action("INCREMENT_IF_ODD")}
        onIncrementAsync={() => action("INCREMENT_ASYNC")}
      />
    </>
  );
}

function mapStateToProps(state: any) {
  return { value: state };
}

function mapDispatchToProps(dispatch: any) {
  return {
    action: (type: string) => dispatch({ type })
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Main);
