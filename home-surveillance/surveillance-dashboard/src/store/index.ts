import { createStore, applyMiddleware } from "redux";
import createSagaMiddleware from "redux-saga";
import reducer, { rootSaga } from "./counter";

const sagaMiddleware = createSagaMiddleware();
export const store = createStore(reducer, applyMiddleware(sagaMiddleware));
sagaMiddleware.run(rootSaga);
