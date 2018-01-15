/** MVC model definitions **/

declare type id = number;

declare interface MsgItemData {
  id?: id;
  text: string;
  userinfo: UserItemData;
  createdat?: Date;
  updatedat?: Date;
}

declare type MsgStoreState = MsgItemData[];

declare interface ChatData {
  editingItem: undefined | MsgItemData;
  loggedInUser: undefined | UserItemData;
}
declare type ChatState = ChatData;


declare interface UserItemData {
  id: id;
  firstname?: string;
  lastname?: string;
  login?: string;
  email?: string;
  gender?: boolean;
  description?: string;
  createdat?: Date;
  updatedat?: Date;
}

// declare type UserStoreState = UserItemData[];

