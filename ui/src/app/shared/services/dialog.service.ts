import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from "@angular/material";
import { NewTweetDialogComponent } from "../components/new-tweet-dialog/new-tweet-dialog.component";
import { TweetResponseDTO } from "../../dto/TweetResponseDTO";
import { ReplyNewTweetDialogComponent } from "../components/reply-new-tweet-dialog/reply-new-tweet-dialog.component";
import { DeleteTweetDialogComponent } from "../components/delete-tweet-dialog/delete-tweet-dialog.component";
import { TweetInfoDialogComponent } from "../components/tweet-info-dialog/tweet-info-dialog.component";

@Injectable()
export class DialogService {
  private config: MatDialogConfig = {
    width: '590px',
    panelClass: 'app-custom-dialog',
    position: {
      top: '10%'
    }
  };

  constructor(private matDialog: MatDialog) {
  }

  public showNewTweetDialog(): MatDialogRef<NewTweetDialogComponent> {
    return this.matDialog.open(NewTweetDialogComponent, {...this.config});
  }

  public showReplyToTweetDialog(inReplyToTweet: TweetResponseDTO): MatDialogRef<ReplyNewTweetDialogComponent> {
    return this.matDialog.open(ReplyNewTweetDialogComponent, {
      ...this.config,
      data: {inReplyToTweet}
    });
  }

  public showDeleteTweetDialog(tweet: TweetResponseDTO): MatDialogRef<DeleteTweetDialogComponent> {
    return this.matDialog.open(DeleteTweetDialogComponent, {
      ...this.config,
      width: '520px',
      position: {},
      data: {tweet}
    });
  }

  public showTweetInfoDialog(tweet: TweetResponseDTO): MatDialogRef<TweetInfoDialogComponent> {
    return this.matDialog.open(TweetInfoDialogComponent, {
      ...this.config,
      panelClass: "app-custom-dialog-tweet-info",
      position: {
        top: '5%'
      },
      width: '640px',
      data: {tweet}
    });
  }
}
