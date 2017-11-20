import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs/Observable";
import { PageDTO } from "../../dto/PageDTO";
import { TweetResponseDTO } from "../../dto/TweetResponseDTO";
import { TweetCreateDTO } from "../../dto/TweetCreateDTO";

@Injectable()
export class TweetHttpService {

  constructor(private http: HttpClient) {
  }

  public getTweets(username?: string): Observable<PageDTO<TweetResponseDTO>> {
    return this.http.get<PageDTO<TweetResponseDTO>>("/tweets", {params: {username}});
  }

  public createTweet(body: TweetCreateDTO): Observable<TweetResponseDTO> {
    return this.http.post<TweetResponseDTO>("/tweets", body);
  }

  public deleteTweet(tweetId: number): Observable<void> {
    return this.http.delete<void>(`/tweets/${tweetId}`);
  }

  public getTweet(tweetId: number): Observable<TweetResponseDTO> {
    return this.http.get<TweetResponseDTO>(`/tweets/${tweetId}`);
  }

  public getComments(tweetId: number): Observable<PageDTO<TweetResponseDTO>> {
    return this.http.get<PageDTO<TweetResponseDTO>>(`/tweets/${tweetId}`);
  }

  public likeTweet(tweetId: number): Observable<TweetResponseDTO> {
    return this.http.post<TweetResponseDTO>(`/tweets/${tweetId}/like`, null);
  }

  public unlikeTweet(tweetId: number): Observable<TweetResponseDTO> {
    return this.http.post<TweetResponseDTO>(`/tweets/${tweetId}/unlike`, null);
  }

  public getWall(): Observable<PageDTO<TweetResponseDTO>> {
    return this.http.get<PageDTO<TweetResponseDTO>>("/wall");
  }
}
