import * as React from 'react';
import * as style from './style.css';
import * as classNames from 'classnames';

export namespace MsgItem {
  export interface Props {
    msg: MsgItemData;
    startEditMsg: (msg: MsgItemData) => any;
    deleteMsg: (msg: MsgItemData) => any;
    editing: boolean;
  }
}

export class MsgItem extends React.Component<MsgItem.Props> {

  constructor(props?: MsgItem.Props, context?: any) {
    super(props, context);
  }

  render() {
    const {msg, startEditMsg, deleteMsg} = this.props;

    const editClasses = classNames({
      [style.editing]: this.props.editing,
      [style.normal]: !this.props.editing
    });

    // console.log("msg item::: ", msg);
    return (
      <li className={editClasses}>
        <label className={style.username}>User_id [{msg.userid}]&nbsp;:&nbsp;</label>
        <label className={style.text}>{msg.text}</label>

        <a className={style.closebutton} onClick={() => deleteMsg(msg)}>×</a>
        <a className={style.closebutton} onClick={() => startEditMsg(msg)}>✎</a>

        <br/>
        <label className={style.datetime}>{new Date(msg.createdat).toLocaleString()}</label>
        {msg.updatedat ? <label className={style.datetime}>&nbsp;&nbsp;(edited at {new Date(msg.updatedat).toLocaleString()})</label> : null}

        {/*<a href="#background" className={style.closebutton} onClick={() => deleteMsg(msg)}>×</a>*/}
        {/*<a href="#background" className={style.closebutton} onClick={() => startEditMsg(msg)}>✎</a>*/}
      </li>
    );
  }
}
