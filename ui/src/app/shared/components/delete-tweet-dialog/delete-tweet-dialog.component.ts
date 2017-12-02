import { Component, Inject } from '@angular/core';
import { TweetHttpService } from "../../http/tweet-http.service";
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";
import { MAT_DIALOG_DATA, MatDialogRef, MatSnackBar } from "@angular/material";

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
              private snackBar: MatSnackBar,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    this.tweet = data.tweet;
  }

  triggerDeleteTweetAction(): void {
    this.tweetHttpService.deleteTweet(this.tweet.id)
      .subscribe(() => {
        this.dialogRef.close(true);
        this.snackBar.open("Your Tweet has been deleted.", null, {
          verticalPosition: 'top',
          duration: 3000,
        });
      });
  }
}
