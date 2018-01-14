/** MVC model definitions **/

declare type id = number;

declare interface MsgItemData {
  id?: id;
  text?: string;
  userid?: id;
  createdat?: Date;
  updatedat?: Date;
  deletedat?: Date;
  isdeleted?: boolean;
}

declare type MsgStoreState = MsgItemData[];

declare interface ChatData {
  editingItem: undefined | MsgItemData;
  editingMode: boolean;
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
  deletedat?: Date;
  isdeleted: boolean;
}

declare type UserStoreState = UserItemData[];

