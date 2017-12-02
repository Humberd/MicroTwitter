import { Component, Input } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";
import { MatSnackBar } from "@angular/material";
import * as copy from 'copy-to-clipboard';

@Component({
  selector: 'app-tweet-options',
  templateUrl: './tweet-options.component.html',
  styleUrls: ['./tweet-options.component.scss']
})
export class TweetOptionsComponent {
  @Input() tweet: TweetResponseDTO;

  constructor(private snackBar: MatSnackBar) {
  }

  copyLinkToClipboard(): void {
    const url = new URL(location.toString());
    url.pathname = `/u/${this.tweet.user.username}/tweet/${this.tweet.id}`;

    copy(url.toString());

    this.snackBar.open("Link copied.", null,
      {
        verticalPosition: 'top',
        duration: 2000,
      });
  }
}
