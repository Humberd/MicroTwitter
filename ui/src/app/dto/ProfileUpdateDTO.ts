export interface ProfileUpdateDTO {
  fullName?: string;
  description?: string;
  location?: string;
  profileLinkColor?: string;
  url?: string;
  birthdate?: BirthdateDTO;
  avatarUrl ?: string;
  backgroundUrl ?: string;
}

export interface BirthdateDTO {
  day?: number;
  month?: number;
  year?: number;
}
