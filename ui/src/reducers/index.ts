import { combineReducers, Reducer } from 'redux';
import {msgsReducer, chatReducer} from './msgs';

export interface RootState {
  msgs: MsgStoreState;
  chat: ChatState;
}

export default combineReducers<RootState>({
  msgs: msgsReducer, chat: chatReducer
});
