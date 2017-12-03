import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs/Observable";
import { UserResponseDTO } from "../../dto/UserResponseDTO";
import { ProfileUpdateDTO } from "../../dto/ProfileUpdateDTO";
import { PageDTO } from "../../dto/PageDTO";
import { CONSTANTS } from "../../config/Constants";
import { Pageable } from "../../models/Pageable";
import { PageHelper } from "../../helpers/PageHelper";

@Injectable()
export class UserHttpService {

  constructor(private http: HttpClient) {
  }

  /**
   * In case we did not logout the previous user and we login the next one.
   * When we try to get a new user data the JWTHttpInterceptor would still take an old user http from AuthService.
   * To override this behaviour we need to provide a custom jwt, so that the JWTHttpInterceptor would omit the old one.
   * @param {string} jwt optional token
   */
  public getMe(jwt?: string): Observable<UserResponseDTO> {
    let headers;
    if (jwt) {
      headers = new HttpHeaders({[CONSTANTS.AUTH_HEADER_NAME]: CONSTANTS.AUTH_TOKEN_PREFIX + jwt});
    }
    return this.http.get<UserResponseDTO>("/api/me", {headers: headers || {}});
  }

  public getUsers(usernameOrfullName: string, pageable: Pageable = {}): Observable<PageDTO<UserResponseDTO>> {
    return this.http.get<PageDTO<UserResponseDTO>>("/api/users",
      {params: {usernameOrfullName, ...PageHelper.convertToPageableStr(pageable)}});
  }

  public getFollowingUsers(username?: string, pageable: Pageable = {}): Observable<PageDTO<UserResponseDTO>> {
    return this.http.get<PageDTO<UserResponseDTO>>(`/api/users/${username}/following-users`,
      {params: {...PageHelper.convertToPageableStr(pageable)}});
  }

  public getFollowers(username?: string, pageable: Pageable = {}): Observable<PageDTO<UserResponseDTO>> {
    return this.http.get<PageDTO<UserResponseDTO>>(`/api/users/${username}/followers`,
      {params: {...PageHelper.convertToPageableStr(pageable)}});
  }

  public getUser(username: string): Observable<UserResponseDTO> {
    return this.http.get<UserResponseDTO>(`/api/users/${username}`);
  }

  public updateProfile(body: ProfileUpdateDTO): Observable<UserResponseDTO> {
    return this.http.put<UserResponseDTO>("/api/me/profile", body);
  }

  public followUser(userId: number): Observable<void> {
    return this.http.post<void>(`/api/users/${userId}/follow`, null);
  }

  public unfollowUser(userId: number): Observable<void> {
    return this.http.post<void>(`/api/users/${userId}/unfollow`, null);
  }
}
