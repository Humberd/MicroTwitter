import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs/Observable";
import { UserResponseDTO } from "../../dto/UserResponseDTO";
import { ProfileUpdateDTO } from "../../dto/ProfileUpdateDTO";
import { PageDTO } from "../../dto/PageDTO";

@Injectable()
export class UserHttpService {

  constructor(private http: HttpClient) {
  }

  public getMe(): Observable<UserResponseDTO> {
    return this.http.get<UserResponseDTO>("/me");
  }

  public updateProfile(body: ProfileUpdateDTO): Observable<UserResponseDTO> {
    return this.http.put<UserResponseDTO>("/me/profile", body);
  }

  public getUsers(username: string): Observable<PageDTO<UserResponseDTO>> {
    return this.http.get<PageDTO<UserResponseDTO>>("/users", {params: {username}});
  }

  public followUser(userId: number): Observable<void> {
    return this.http.post<void>(`/users/${userId}/follow`, null);
  }

  public unfollowUser(userId: number): Observable<void> {
    return this.http.post<void>(`/users/${userId}/unfollow`, null);
  }

  public getUser(username: string): Observable<UserResponseDTO> {
    return this.http.get<UserResponseDTO>(`/users/${username}`);
  }
}
