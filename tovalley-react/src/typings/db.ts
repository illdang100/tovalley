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
  commentCreateAt: string;
  commentByUser: boolean;
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
  otherUserProfileImage: string | null;
  otherUserNick: string;
  createdChatRoomDate: string;
  lastMessageContent: string | null;
  unReadMessageCount: number;
  lastMessageTime: string;
}

export interface ChatMessage {
  chatMessageId: string;
  chatType: string | null;
  imageUrl: string | null;
  senderId: number;
  myMsg: boolean;
  content: string;
  createdAt: string;
  readCount: number;
}

export interface MessageType {
  chatRoomId: number;
  chatType: string;
  imageName: string;
  imageUrl: string;
  senderId: number;
  content: string;
  createdAt: string;
  readCount: number;
}

export interface MessageListType {
  data: {
    memberId: number;
    chatRoomId: number;
    chatMessages: {
      content: ChatMessage[];
      pageable: {
        sort: {
          empty: boolean;
          sorted: boolean;
          unsorted: boolean;
        };
        offset: number;
        pageNumber: number;
        pageSize: number;
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
  };
}

export interface NotificationType {
  chatRoomId: number;
  recipientId: string;
  senderNick: string;
  createdDate: string;
  content: string;
  notificationType: string;
}

export interface AlarmListType {
  chatNotificationId: number;
  chatRoomId: number;
  senderNick: string;
  createdDate: string;
  content: string;
  hasRead: boolean;
}

export interface AlarmListResp {
  data: {
    content: AlarmListType[];
    pageable: {
      sort: {
        empty: boolean;
        sorted: boolean;
        unsorted: boolean;
      };
      offset: number;
      pageNumber: number;
      pageSize: number;
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

export interface RecentPostType {
  lostFoundBoardId: number;
  lostFoundBoardCategory: string;
  lostFoundBoardTitle: string;
  lostFoundBoardContent: string;
  lostFoundBoardCreatedAt: string;
}

export interface RecentReviewType {
  reviewId: number;
  reviewRating: number;
  reviewContent: string;
  reviewCreatedAt: string;
  waterPlaceId: number;
}
