import { Component, OnDestroy, OnInit } from '@angular/core';
import { TweetResponseDTO } from "../../../../dto/TweetResponseDTO";
import { PageDTO } from "../../../../dto/PageDTO";
import { Subscription } from "rxjs/Subscription";
import { ActivatedRoute } from "@angular/router";
import { TweetHttpService } from "../../../../shared/http/tweet-http.service";
import { Pageable } from "../../../../models/Pageable";
import { Observable } from "rxjs/Observable";
import { AbstractScrollPageableComponent } from "../../../../shared/AbstractScrollPageableComponent";
import { AuthService } from "../../../../shared/services/auth.service";

@Component({
  selector: 'app-likes',
  templateUrl: './likes.component.html',
  styleUrls: [
    './likes.component.scss',
    '../_tab-card.scss'
  ]
})
export class LikesComponent extends AbstractScrollPageableComponent<TweetResponseDTO> implements OnInit, OnDestroy {
  private username: string;
  private routeParamsSub: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private tweetHttpService: TweetHttpService,
              public authService: AuthService) {
    super();
  }

  ngOnInit() {
    // when username path variable changes we want a brand new state
    this.routeParamsSub = this.activatedRoute.parent.params
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
    return this.tweetHttpService.getLikedTweets(...params);
  }

  public requestNextPage(): void {
    super.requestNextPage(this.username);
  }
}
