import { UserResponseDTO } from "../dto/UserResponseDTO";

export interface AppUser {
  data?: UserResponseDTO;
  jwtToken?: string;
}
