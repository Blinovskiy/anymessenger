import * as React from 'react';
import * as style from './style.css';

export namespace MsgTextInput {
  export interface Props {
    editingItem?: MsgItemData;
    onSend: (msg: MsgItemData) => any;
    editLast: () => any;
    cancelEdit: () => any;
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
      text: this.props.editingItem ? this.props.editingItem.text : ''
      // text: this.props.editingItem ? this.props.editingItem.text : undefined
    };
  }

  componentWillMount() {
    document.addEventListener("keyup", this.handleCancelKey)
  };

  componentWillUnmount() {
    document.removeEventListener("keyup", this.handleCancelKey)
  };

  componentWillReceiveProps(nextProps) {
    console.log('nextProps MsgTextInput -> ', nextProps);
    if (nextProps.editingItem) this.setState({text: nextProps.editingItem.text})
  }

  handleSend = () => {
    if (this.state.text.trim() != '') {
      const text = this.state.text.trim();
      const userinfo = {id: 1};
      this.props.onSend({text, userinfo});
      this.setState({text: ''});
    }
  };

  handleChange = (e) => {
    // console.log('e.target.value=',e.target.value);
    // if (e.target.value != '\n')
    this.setState({text: e.target.value});
  };

  handleKey = (e) => {
    if (e.key === 'Enter') {
      this.handleSend();
      this.setState({text: ''});
    } else if (e.keyCode === 38 && this.state.text.trim() == '') { // arrow up
      console.log('edit last');
      this.props.editLast();
      this.setState({text: ''});
    }
  };

  handleCancelKey = (e) => {
    switch (e.keyCode) {
      case 27: // esc
        this.props.cancelEdit();
        this.setState({text: ''});
        break;
      default:
        break;
    }
  };

  render() {
    console.log('INPUT STATE:', this.state.text);

    return (
      <div className={style.view}>
        <textarea className={style.newmsg}
                  autoFocus
                  value={this.state.text}
                  onChange={this.handleChange}
                  onKeyUp={this.handleKey}
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
