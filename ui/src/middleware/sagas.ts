import { takeLatest } from 'redux-saga'
import { call, put } from 'redux-saga/effects'
import {addMsg, startEditMsg, finishEditMsg, editMsg, deleteMsg} from '../actions/msgs'

function* editMsgGen(action) {
  try {
    const res = yield call(editMsg, action.payload);
    yield put(finishEditMsg(res))
  } catch (e) {
    // yield put(loginFailure(`Failed to log in user: ${e.message}`))
  }
}

function* editMsgSaga() {
  yield takeLatest('EDIT_MSG', editMsgGen)
}

export default function* rootSaga() {
  yield [
    editMsgSaga()
  ]
}
