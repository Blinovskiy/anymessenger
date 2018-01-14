import * as React from 'react';
import * as style from './style.css';
import {postMessage} from '../../api/api';

export namespace MsgTextInput {
  export interface Props {
    editingItem?: MsgItemData;
    placeholder?: string;
    onSend: (msg: MsgItemData) => any;
    editLast: () => any;
    cancelEdit: () => any;
    // cancelEdit: (msg: MsgItemData) => any;
    updateMsgs: (msgs: MsgItemData[]) => any;
  }

  export interface State {
    text: string;
  }
}

export class MsgTextInput extends React.Component<MsgTextInput.Props, MsgTextInput.State> {

  constructor(props?: MsgTextInput.Props, context?: any) {
    super(props, context);
    this.state = {
      text: this.props.editingItem ? this.props.editingItem.text : undefined
    };
  }

  componentWillReceiveProps(nextProps) {
    // console.log('nextProps MTI -> ::',nextProps);
    if (nextProps.editingItem) this.setState({text: nextProps.editingItem.text})
  }

  handleSend = () => {
    if (this.state.text.trim() != '') {
      const text = this.state.text.trim();
      this.props.onSend({text});
    }
    this.setState({text: ''})
  };

  handleChange = (e) => {
    this.setState({text: e.target.value});
  };

  // handleKeyPress = (e) => {
  //   if (e.key === 'Enter') {
  //     this.handleSend()
  //   }
  // };

  // left = 37
  // up = 38
  // right = 39
  // down = 40
  handleKeyUp = (e) => {
    if (e.key === 'Enter') {
      this.handleSend()
    } else if (e.keyCode === 38) {
      this.props.editLast()
    } else if (e.keyCode === 27) {
      this.props.cancelEdit();
      this.setState({text: ''});
    }
    // else if (e.keyCode === 40) {
    //   postMessage(-1,this.state.text.trim(),2)
    //     .then(res => {console.log('postedMessage::',res)})
    //     .catch(reason => console.log(reason));
    // }
  };

  render() {
    console.log('INPUT STATE:', this.state.text);

    return (
      <div className={style.view}>
        <textarea className={style.newmsg}
                  autoFocus
                  placeholder={this.props.placeholder}
                  value={this.state.text}
                  onChange={this.handleChange}
                  onKeyUp={this.handleKeyUp}
        />
        <button
          className={style.sendbtn}
          onClick={this.handleSend}
        >
          {this.props.editingItem ? "Edit" : "Send"}
        </button>
      </div>
    );
  }
}
