import * as React from 'react';
import * as MsgActions from '../../actions/msgs';
import * as style from './style.css';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router';
import { RootState } from '../../reducers';
import { MainSection } from '../../components';

export namespace App {
  export interface Props extends RouteComponentProps<void> {
    msgs: MsgItemData[];
    chat: ChatState;
    actions: typeof MsgActions;
  }

  export interface State {
    /* empty */
  }
}

@connect(mapStateToProps, mapDispatchToProps)
export class App extends React.Component<App.Props, App.State> {

  render() {
    const { msgs, chat, actions, children } = this.props;
    console.log(msgs, chat, actions, children);
    return (
      <div className={style.normal}>
        <MainSection msgs={msgs} chat={chat} actions={actions} />
        {children}
      </div>
    );
  }
}

function mapStateToProps(state: RootState) {
  return {
    msgs: state.msgs,
    chat: state.chat
  };
}

function mapDispatchToProps(dispatch) {
  return {
    actions: bindActionCreators(MsgActions as any, dispatch)
  };
}
