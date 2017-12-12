import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from "@angular/material";
import { NewTweetDialogComponent } from "../components/new-tweet-dialog/new-tweet-dialog.component";
import { TweetResponseDTO } from "../../dto/TweetResponseDTO";
import { ReplyNewTweetDialogComponent } from "../components/reply-new-tweet-dialog/reply-new-tweet-dialog.component";
import { DeleteTweetDialogComponent } from "../components/delete-tweet-dialog/delete-tweet-dialog.component";
import { TweetInfoDialogComponent } from "../components/tweet-info-dialog/tweet-info-dialog.component";
import { Router } from "@angular/router";
import { LoginDialogComponent } from "../components/login-dialog/login-dialog.component";

@Injectable()
export class DialogService {
  private readonly config: MatDialogConfig = {
    width: '590px',
    panelClass: 'app-custom-dialog',
    position: {
      top: '10%'
    },
    data: {
      dialogService: this
    }
  };

  constructor(private matDialog: MatDialog,
              private router: Router) {
  }

  public showNewTweetDialog(): MatDialogRef<NewTweetDialogComponent> {
    return this.matDialog.open(NewTweetDialogComponent, {...this.config});
  }

  public showReplyToTweetDialog(inReplyToTweet: TweetResponseDTO): MatDialogRef<ReplyNewTweetDialogComponent> {
    return this.matDialog.open(ReplyNewTweetDialogComponent, {
      ...this.config,
      data: {
        ...this.config.data,
        inReplyToTweet
      }
    });
  }

  public showDeleteTweetDialog(tweet: TweetResponseDTO): MatDialogRef<DeleteTweetDialogComponent> {
    return this.matDialog.open(DeleteTweetDialogComponent, {
      ...this.config,
      width: '520px',
      position: {},
      data: {
        ...this.config.data,
        tweet
      }
    });
  }

  public showLoginDialog(): MatDialogRef<LoginDialogComponent> {
    return this.matDialog.open(LoginDialogComponent, {
      ...this.config,
      position: {},
    });
  }

  /**
   * By default When the tweet info dialog appears it also changes url, but don't reload the route, only url change.
   * After dialog closes it restores the previous url.
   * [preventUrlChange] flag stop this behaviour
   */
  public showTweetInfoDialog(tweet: TweetResponseDTO, preventUrlChange = false): MatDialogRef<TweetInfoDialogComponent> {
    const dialogRef = this.matDialog.open(TweetInfoDialogComponent, {
      ...this.config,
      panelClass: "app-custom-dialog-tweet-info",
      position: {
        top: '5%'
      },
      width: '640px',
      data: {
        ...this.config.data,
        tweet
      }
    });

    if (!preventUrlChange) {
      let previousUrl: string;

      dialogRef.afterOpen()
        .subscribe(() => {
          previousUrl = this.router.routerState.snapshot.url;
          window.history.pushState("", "", ["/u", tweet.user.username, "tweet", tweet.id].join("/"));
        });

      dialogRef.afterClosed()
        .subscribe(() => {
          window.history.pushState("", "", previousUrl);
        });
    }

    return dialogRef;
  }
}
