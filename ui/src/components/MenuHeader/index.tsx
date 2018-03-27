import * as React from 'react';
import * as style from './style.css';
import {slide as Menu} from 'react-burger-menu'
import {styles} from 'react-burger-menu'

export namespace MenuHeader {
  export interface Props {
    chat: ChatState
  }

  export interface State {
    /* empty */
  }
}

export class MenuHeader extends React.Component<MenuHeader.Props, MenuHeader.State> {

  constructor(props?: MenuHeader.Props, context?: any) {
    super(props, context);
  }

  render() {
    return (
      <Menu className={style["bm-menu"]} right>
        <a id="home" className={style["bm-item-list"]} href="/">Home</a>
        <a id="about" className={style["bm-item-list"]} href="#">About</a>
        <a id="contact" className={style["bm-item-list"]} href="#">Contact</a>
        <a onClick={() => console.log('burger menu click')} className={style["bm-item-list"]} href="">Settings</a>
      </Menu>
    );
  }
}
