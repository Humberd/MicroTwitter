import { Component, Inject } from '@angular/core';
import { TweetHttpService } from "../../http/tweet-http.service";
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material";
import { SnackBarService } from "../../services/snack-bar.service";

@Component({
  selector: 'app-delete-tweet-dialog',
  templateUrl: './delete-tweet-dialog.component.html',
  styleUrls: [
    './delete-tweet-dialog.component.scss',
    '../_dialog.scss'
  ]
})
export class DeleteTweetDialogComponent {
  tweet: TweetResponseDTO;

  constructor(private tweetHttpService: TweetHttpService,
              public dialogRef: MatDialogRef<DeleteTweetDialogComponent>,
              private snackBarService: SnackBarService,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    this.tweet = data.tweet;
  }

  triggerDeleteTweetAction(): void {
    this.tweetHttpService.deleteTweet(this.tweet.id)
      .subscribe(() => {
        this.dialogRef.close(true);
        this.snackBarService.showInfoSnackBar("Your Tweet has been deleted.");
      });
  }
}
