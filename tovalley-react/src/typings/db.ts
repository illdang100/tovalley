export interface LostList {
  data: {
    id: number;
    title: string;
    content: string;
    author: string;
    category: string;
    commentCnt: number;
    postCreateAt: string;
    postImage: string;
  }[];
}

export interface PlaceName {
  waterPlaceId: number;
  waterPlaceName: string;
  address: string;
}

export interface LostPost {
  data: {
    title: string;
    content: string;
    author: string;
    waterPlaceName: string;
    waterPlaceAddress: string;
    postCreateAt: string;
    postImages: string[];
    isResolved: boolean;
    isMyBoard: boolean;
    boardAuthorProfile: string;
    commentCnt: number;
    comments: LostPostComment[];
  };
}

export interface LostPostComment {
  commentId: number;
  commentAuthor: string;
  commentContent: string;
  commentCreatedAt: string;
  isMyComment: boolean;
  commentAuthorProfile: string;
}

export interface ChatRoomList {
  data: {
    content: ChatRoomItem[];
    pageable: {
      sort: {
        empty: boolean;
        sorted: boolean;
        unsorted: boolean;
      };
      offset: number;
      pageSize: number;
      pageNumber: number;
      paged: boolean;
      unpaged: boolean;
    };
    first: boolean;
    last: boolean;
    size: number;
    number: number;
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    numberOfElements: number;
    empty: boolean;
  };
}

export interface ChatRoomItem {
  chatRoomId: number;
  chatRoomTitle: string;
  otherUserProfileImage: string;
  otherUserNick: string;
  createdChatRoomDate: string;
  lastMessageContent: string;
  lastMessageTime: string;
}
