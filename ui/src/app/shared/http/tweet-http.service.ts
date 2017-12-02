import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs/Observable";
import { PageDTO } from "../../dto/PageDTO";
import { TweetResponseDTO } from "../../dto/TweetResponseDTO";
import { TweetCreateDTO } from "../../dto/TweetCreateDTO";
import { Pageable } from "../../models/Pageable";
import { PageHelper } from "../../helpers/PageHelper";

@Injectable()
export class TweetHttpService {

  constructor(private http: HttpClient) {
  }

  public getTweets(username?: string, pageable: Pageable = {}): Observable<PageDTO<TweetResponseDTO>> {
    return this.http.get<PageDTO<TweetResponseDTO>>("/api/tweets",
      {params: {username, ...PageHelper.convertToPageableStr(pageable)}});
  }

  public getLikedTweets(username?: string, pageable: Pageable = {}): Observable<PageDTO<TweetResponseDTO>> {
    return this.http.get<PageDTO<TweetResponseDTO>>("/api/liked-tweets",
      {params: {username, ...PageHelper.convertToPageableStr(pageable)}});
  }

  public getTweet(tweetId: number): Observable<TweetResponseDTO> {
    return this.http.get<TweetResponseDTO>(`/api/tweets/${tweetId}`);
  }

  public createTweet(body: TweetCreateDTO): Observable<TweetResponseDTO> {
    return this.http.post<TweetResponseDTO>("/api/tweets", body);
  }

  public deleteTweet(tweetId: number): Observable<void> {
    return this.http.delete<void>(`/api/tweets/${tweetId}`);
  }

  public getComments(tweetId: number, pageable: Pageable = {}): Observable<PageDTO<TweetResponseDTO>> {
    return this.http.get<PageDTO<TweetResponseDTO>>(`/api/tweets/${tweetId}`,
      {params: {...PageHelper.convertToPageableStr(pageable)}});
  }

  public likeTweet(tweetId: number): Observable<TweetResponseDTO> {
    return this.http.post<TweetResponseDTO>(`/api/tweets/${tweetId}/like`, null);
  }

  public unlikeTweet(tweetId: number): Observable<TweetResponseDTO> {
    return this.http.post<TweetResponseDTO>(`/api/tweets/${tweetId}/unlike`, null);
  }

  public getWall(pageable: Pageable = {}): Observable<PageDTO<TweetResponseDTO>> {
    return this.http.get<PageDTO<TweetResponseDTO>>("/api/wall",
      {params: {...PageHelper.convertToPageableStr(pageable)}});
  }
}
