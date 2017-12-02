import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { TweetHttpService } from "../../../../shared/http/tweet-http.service";
import { PageDTO } from "../../../../dto/PageDTO";
import { TweetResponseDTO } from "../../../../dto/TweetResponseDTO";
import { Pageable } from "../../../../models/Pageable";
import { Observable } from "rxjs/Observable";
import { Subscription } from "rxjs/Subscription";

@Component({
  selector: 'app-tweets',
  templateUrl: './tweets.component.html',
  styleUrls: [
    './tweets.component.scss',
    '../_tab-card.scss'
  ]
})
export class TweetsComponent implements OnInit, OnDestroy {
  itemsList: TweetResponseDTO[];
  currentPage: PageDTO<TweetResponseDTO>;
  loadingNextPage = false;
  username: string;

  private routeParamsSub: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private tweetHttpService: TweetHttpService) {
  }

  ngOnInit() {
    // when username path variable changes we want a brand new state
    this.routeParamsSub = this.activatedRoute.params
      .map(params => params.username)
      .do(username => {
        this.username = username;
        this.loadingNextPage = true;
        this.itemsList = [];
      })
      .flatMap(username => this.getPage(username))
      .do(() => this.loadingNextPage = false)
      .subscribe();
  }

  ngOnDestroy(): void {
    if (this.routeParamsSub) {
      this.routeParamsSub.unsubscribe();
    }
  }

  requestNextPage(): void {
    this.loadingNextPage = true;
    if (this.currentPage.last) {
      console.info("Current page is the last page. Cannot get more.");
      this.loadingNextPage = false;
      return;
    }

    const nextPageNumber = this.currentPage.number + 1;
    console.info(`Requesting page number ${nextPageNumber}`);

    this.getPage(this.username, {page: nextPageNumber})
      .subscribe(() => {
        this.loadingNextPage = false;
      });
  }

  private getPage(username: string, pageable?: Pageable): Observable<PageDTO<TweetResponseDTO>> {
    return this.tweetHttpService.getTweets(username, pageable)
      .do(page => {
        this.currentPage = page;
        this.itemsList.push(...page.content);
        console.info(`Received page number ${page.number}`);
      });
  }

}
