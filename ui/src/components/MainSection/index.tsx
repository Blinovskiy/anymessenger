import * as React from 'react';
import * as MsgActions from '../../actions/msgs';
import * as style from './style.css';
import {MsgItem} from '../MsgItem';
import {MsgTextInput} from "../MsgTextInput";
import {deleteMessage, getAllMessages, getLastNMessages, postMessage} from "../../api/api";

export namespace MainSection {
  export interface Props {
    msgs: MsgItemData[];
    chat: ChatState;
    actions: typeof MsgActions;
  }

  export interface State {
    text: string;
    id: number;
  }
}

export class MainSection extends React.Component<MainSection.Props, MainSection.State> {

  constructor(props?: MainSection.Props, context?: any) {
    super(props, context);
    const ei = this.props.chat.editingItem;
    this.state = {
      text: ei ? ei.text : '',
      id: ei ? ei.id : -1
    }
  }

  updateMsgs = () => {
    getLastNMessages(8)
      .then(res => {
        console.log('getLastNMessages::', res);
        this.props.actions.updateMsgs(res)
      })
      .catch(reason => console.log(reason));
  };

  componentDidMount() {
    this.updateMsgs();
  }

  componentWillReceiveProps(nextProps) {
    // console.log('nextProps MainSection -> ',nextProps);
    if (nextProps.chat.editingItem) this.setState({id: nextProps.chat.editingItem.id});
    else this.setState({id: -1})
  }

  handleSendMsg = (msg: MsgItemData) => {
    console.log('editingItem:', this.props.chat.editingItem);
    if (this.props.chat.editingItem) {
      const id = this.props.chat.editingItem.id;
      const text = msg.text;

      if (this.props.chat.editingItem.text !== text) {
        this.props.actions.editMsg({
          id: id,
          text: text,
          userinfo: {id: 1}
        });

        console.log('posting msg:', msg);
        postMessage(id, text, 1)
          .then(res => {
            console.log('updated message id:', res);
            this.updateMsgs();
          })
          .catch(reason => console.log(reason));
      } else {
        // console.log('Messages identical');
        this.props.actions.finishEditMsg(this.props.chat.editingItem);
      }
    } else {
      this.props.actions.addMsg({
        text: msg.text,
        userinfo: {id: 1}
      });

      console.log('posting msg:', msg);
      postMessage(-1, msg.text, 1)
        .then(res => {
          console.log('posted message id:', res);
          this.updateMsgs();
        })
        .catch(reason => {
            console.log(reason);
          }
        );
    }
  };

  handleEditLastMsg = () => {
    const {msgs, actions} = this.props;
    if (msgs.length > 0)
      actions.startEditMsg(msgs[msgs.length - 1])
  };

  // handleKeyUp = (e) => {
  //   if (e.keyCode === 27) {
  //     this.props.actions.finishEditMsg(this.props.chat.editingItem);
  //     // this.setState({text: ''});
  //   }
  // };

  handleDeleteMsg = (msg: MsgItemData) => {
    this.props.actions.deleteMsg(msg);
    deleteMessage(msg.id)
      .then(res => {
        console.log('postedMessage::', res);
        this.updateMsgs();
      })
      .catch(reason => console.log(reason));
  };

  render() {
    const {msgs, actions} = this.props;
    // console.log('editingItem MS:::', this.props.chat.editingItem);
    // console.log('editing mode:::', this.props.chat.editingMode);

    return (
      <section className={style.main}>
        <div className={style.msgs}>
          <ul className={style.normal}>
            {msgs.map(msg =>
              <MsgItem
                key={msg.id}
                msg={msg}
                startEditMsg={actions.startEditMsg}
                deleteMsg={this.handleDeleteMsg}
                editing={this.state.id === msg.id}
              />
            )}
          </ul>
        </div>
        <div className={style.bottompanel}>
          <MsgTextInput
            editingItem={this.props.chat.editingItem}
            onSend={this.handleSendMsg}
            editLast={this.handleEditLastMsg}
            cancelEdit={() => actions.finishEditMsg(this.props.chat.editingItem)}
            updateMsgs={actions.updateMsgs}
          />
        </div>
      </section>
    );
  }
}
