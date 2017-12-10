import { Component, OnInit } from '@angular/core';
import { DialogService } from "../../../shared/services/dialog.service";
import { ActivatedRoute, Router } from "@angular/router";
import { Observable } from "rxjs/Observable";
import { TweetHttpService } from "../../../shared/http/tweet-http.service";

@Component({
  selector: 'app-tweet-info-route',
  templateUrl: './tweet-info-route.component.html',
  styleUrls: ['./tweet-info-route.component.scss']
})
export class TweetInfoRouteComponent implements OnInit {

  constructor(private dialogService: DialogService,
              private tweetHttpService: TweetHttpService,
              private router: Router,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    /* Combines route params to a single object: {tweetId, username} */
    Observable.combineLatest(
      this.getTweetIdObs(),
      this.getUsernameObs()
    ).map(result => ({tweetId: result[0], username: result[1]}))
    /* For every combined params object get a tweet */
      .switchMap(resultObj => this.tweetHttpService.getTweet(parseInt(resultObj.tweetId, 10)))
      /* Open a tweet dialog */
      .subscribe(tweet => {
        const dialogRef = this.dialogService.showTweetInfoDialog(tweet, true);
        dialogRef.afterClosed()
          .subscribe(() => this.router.navigate(["/u", tweet.user.username]));
      });
  }

  private getTweetIdObs(): Observable<string> {
    return this.activatedRoute.params
      .map(params => params.tweetId);
  }

  private getUsernameObs(): Observable<string> {
    return this.activatedRoute.parent.params
      .map(params => params.username);
  }
}
