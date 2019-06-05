import { put, takeEvery, delay } from "redux-saga/effects";

export function* incrementAsync() {
  yield delay(5000);
  yield put({ type: "INCREMENT" });
}

export function* rootSaga() {
  yield takeEvery("INCREMENT_ASYNC", incrementAsync);
}

export default function counter(state = 0, action: any) {
  switch (action.type) {
    case "INCREMENT":
      return state + 1;
    case "INCREMENT_IF_ODD":
      return state % 2 !== 0 ? state + 1 : state;
    case "DECREMENT":
      return state - 1;
    default:
      return state;
  }
}
