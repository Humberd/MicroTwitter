import { Component, Input, OnInit } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";
import { TweetHttpService } from "../../http/tweet-http.service";
import { DialogService } from "../../services/dialog.service";

@Component({
  selector: 'app-tweet-actions',
  templateUrl: './tweet-actions.component.html',
  styleUrls: ['./tweet-actions.component.scss']
})
export class TweetActionsComponent implements OnInit {
  @Input() tweet: TweetResponseDTO;

  constructor(private tweetHttpService: TweetHttpService,
              private dialogService: DialogService) {
  }

  ngOnInit() {
  }

  toggleLikeAction(): void {
    if (this.tweet.liked) {
      this.triggerUnlikeAction();
    } else {
      this.triggerLikeAction();
    }
  }

  triggerReplyCommentAction(): void {
    const dialog = this.dialogService.showReplyToTweetDialog(this.tweet);
    dialog.afterClosed()
      .filter(newTweet => !!newTweet)
      .subscribe(() => {
        this.tweet.commentsCount++;
      });
  }

  private triggerLikeAction(): void {
    this.tweetHttpService.likeTweet(this.tweet.id)
      .subscribe(response => {
        Object.assign(this.tweet, response);
      });
  }

  private triggerUnlikeAction(): void {
    this.tweetHttpService.unlikeTweet(this.tweet.id)
      .subscribe(response => {
        Object.assign(this.tweet, response);
      });
  }

}
