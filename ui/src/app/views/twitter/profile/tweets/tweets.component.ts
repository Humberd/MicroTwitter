import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { TweetHttpService } from "../../../../shared/http/tweet-http.service";
import { PageDTO } from "../../../../dto/PageDTO";
import { TweetResponseDTO } from "../../../../dto/TweetResponseDTO";
import { Observable } from "rxjs/Observable";
import { Subscription } from "rxjs/Subscription";
import { AbstractScrollPageableComponent } from "../../../../shared/AbstractScrollPageableComponent";
import { AuthService } from "../../../../shared/services/auth.service";

@Component({
  selector: 'app-tweets',
  templateUrl: './tweets.component.html',
  styleUrls: [
    './tweets.component.scss',
    '../_tab-card.scss'
  ]
})
export class TweetsComponent extends AbstractScrollPageableComponent<TweetResponseDTO> implements OnInit, OnDestroy {
  private username: string;
  private routeParamsSub: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private tweetHttpService: TweetHttpService,
              public authService: AuthService) {
    super();
  }

  ngOnInit() {
    // when username path variable changes we want a brand new state
    this.routeParamsSub = this.activatedRoute.params
      .map(params => params.username)
      .do(username => {
        this.username = username;
        this.itemsList = [];
      })
      .flatMap(username => this.getPage(username))
      .subscribe();
  }

  ngOnDestroy(): void {
    if (this.routeParamsSub) {
      this.routeParamsSub.unsubscribe();
    }
  }

  invokeGetPageMethod(...params: any[]): Observable<PageDTO<TweetResponseDTO>> {
    return this.tweetHttpService.getTweets(...params);
  }

  public requestNextPage(): void {
    super.requestNextPage(this.username);
  }
}
