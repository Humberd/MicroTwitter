import { Component, Input, OnInit } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";
import { TweetHttpService } from "../../http/tweet-http.service";
import { DialogService } from "../../services/dialog.service";
import { isFunction } from "util";

@Component({
  selector: 'app-tweet-actions',
  templateUrl: './tweet-actions.component.html',
  styleUrls: ['./tweet-actions.component.scss']
})
export class TweetActionsComponent implements OnInit {
  @Input() tweet: TweetResponseDTO;
  @Input() replyCommentAction: (tweet?: TweetResponseDTO) => any;

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
    /* When we want to override the default reply comment action */
    if (isFunction(this.replyCommentAction)) {
      this.replyCommentAction(this.tweet);
      return;
    }

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
