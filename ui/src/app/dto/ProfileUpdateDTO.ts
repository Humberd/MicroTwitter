export interface ProfileUpdateDTO {
  fullName?: string;
  description?: string;
  location?: string;
  profileLinkColor?: string;
  url?: string;
  birthdate?: BirthdateDTO;
}

export interface BirthdateDTO {
  day?: number;
  month?: number;
  year?: number;
}
