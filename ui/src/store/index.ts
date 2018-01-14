import {applyMiddleware, createStore, Store} from 'redux';
import {composeWithDevTools} from "redux-devtools-extension";
// import { logger, sagas } from '../middleware';
import rootReducer, {RootState} from '../reducers';
import createSagaMiddleware from 'redux-saga'
import rootSaga from '../middleware/sagas'

export function configureStore(initialState?: RootState) {

  const sagaMiddleware = createSagaMiddleware();

  // let middleware = applyMiddleware(logger);
  let middleware = applyMiddleware(sagaMiddleware);

  if (process.env.NODE_ENV === 'development') {
    middleware = composeWithDevTools(middleware);
  }
  const store = createStore(rootReducer, initialState, middleware) as Store<RootState>;

  sagaMiddleware.run(rootSaga);

  if (module.hot) {
    module.hot.accept('../reducers', () => {
      const nextReducer = require('../reducers');
      store.replaceReducer(nextReducer);
    });
  }

  return store;
}
