import {handleActions} from 'redux-actions';
import * as Actions from '../constants/actions';

// const initialState: MsgStoreState = [{
//   id: 0,
//   text: 'initialState',
//   userid: 0,
//   createdat: null,
//   updatedat: null,
//   deletedat: null
// }];
const initialMsgStoreStateState: MsgStoreState = [];
const initialChatStateState: ChatData = {
  editingItem: undefined,
  loggedInUser: undefined
};

export const msgsReducer = handleActions<MsgStoreState, any>({
  [Actions.ADD_MSG]: (state, action) => {
    return [...state, {
      // id: state.reduce((maxId, msg) => Math.max(msg.id, maxId), -1) + 1,
      id: -1,
      ...action.payload,
    }];
  },

  [Actions.UPDATE_MSGS]: (state, action) => {
    console.log('action:::',action);
    // return [...state, ...action.payload]
    return action.payload
  },

  [Actions.EDIT_MSG]: (state, action) => {
    return state.map(msg => {
      return msg.id === action.payload.id
        ? { ...msg, text: action.payload.text }
        : msg;
    });
  },


  [Actions.DELETE_MSG]: (state, action) => {
    return state.filter(message => message.id !== action.payload.id);
  },

}, initialMsgStoreStateState);

export const chatReducer = handleActions<ChatData, MsgItemData>({
  [Actions.START_EDIT_MSG]: (state, action) => {
    // return action.payload as ChatState
    return {...state, editingItem: action.payload, editingMode: true}
  },

  [Actions.FINISH_EDIT_MSG]: (state, action) => {
    return {...state, editingItem: undefined}
  },
}, initialChatStateState);
