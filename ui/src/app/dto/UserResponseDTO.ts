export interface UserResponseDTO {
  id?: number;
  createdAt?: string;
  username?: string;
  email?: string;
  tweetsCount?: number;
  likesCount?: number;
  followedUsersCount?: number;
  followedByUsersCount?: number;
  // does user follow user in this object
  following?: boolean;
  profile?: ProfileResponseDTO;
}

export interface ProfileResponseDTO {
  fullName?: string;
  description?: string;
  location?: string;
  profileLinkColor?: string;
  url?: string;
  birthdate: BirthdateResponseDTO;
}

export interface BirthdateResponseDTO {
  day?: number;
  month?: number;
  year?: number;
}
