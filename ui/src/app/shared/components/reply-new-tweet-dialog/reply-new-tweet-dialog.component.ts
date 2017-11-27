import { Component, Inject, Optional } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material";
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";

@Component({
  selector: 'app-reply-new-tweet-dialog',
  templateUrl: './reply-new-tweet-dialog.component.html',
  styleUrls: [
    './reply-new-tweet-dialog.component.scss',
    '../_dialog.scss'
  ]
})
export class ReplyNewTweetDialogComponent {
  inReplyToTweet: TweetResponseDTO;

  constructor(private dialogRef: MatDialogRef<ReplyNewTweetDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    this.inReplyToTweet = data.inReplyToTweet;
  }

  closeDialog() {
    this.dialogRef.close();
  }
}
