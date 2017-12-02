export interface TweetResponseDTO {
  id?: number;
  content?: string;
  createdAt?: string;
  likesCount?: number;
  commentsCount?: number;
  user?: TweetUserResponseDTO;
  liked?: boolean;
  inReplyToTweetId?: number;
  inReplyToUser?: TweetUserResponseDTO;
}

export interface TweetUserResponseDTO {
  id?: number;
  username?: string;
  fullName?: string;
}
