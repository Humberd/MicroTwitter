import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { TweetHttpService } from "../../../../shared/http/tweet-http.service";
import { PageDTO } from "../../../../dto/PageDTO";
import { TweetResponseDTO } from "../../../../dto/TweetResponseDTO";
import { Pageable } from "../../../../models/Pageable";
import { Observable } from "rxjs/Observable";

@Component({
  selector: 'app-tweets',
  templateUrl: './tweets.component.html',
  styleUrls: [
    './tweets.component.scss',
    '../_tab-card.scss'
  ]
})
export class TweetsComponent implements OnInit {
  currentPage: PageDTO<TweetResponseDTO>;

  constructor(private activatedRoute: ActivatedRoute,
              private tweetHttpService: TweetHttpService) {
  }

  ngOnInit() {
    this.activatedRoute.params
      .map(params => params.username)
      .flatMap(username => this.getPage(username))
      .subscribe();
  }

  private getPage(username: string, pageable?: Pageable): Observable<PageDTO<TweetResponseDTO>> {
    return this.tweetHttpService.getTweets(username, pageable)
      .do(page => this.currentPage = page)
      .do(page => console.log(page));
  }

}
