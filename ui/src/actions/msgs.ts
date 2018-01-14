import { createAction } from 'redux-actions';
import * as Actions from '../constants/actions';

export const addMsg = createAction<MsgItemData>(Actions.ADD_MSG);
export const updateMsgs = createAction<MsgItemData[]>(Actions.UPDATE_MSGS);
export const startEditMsg = createAction<MsgItemData>(Actions.START_EDIT_MSG);
export const finishEditMsg = createAction<MsgItemData>(Actions.FINISH_EDIT_MSG);
export const editMsg = createAction<MsgItemData>(Actions.EDIT_MSG);
export const deleteMsg = createAction<MsgItemData>(Actions.DELETE_MSG);
