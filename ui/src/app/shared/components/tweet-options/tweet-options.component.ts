import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";
import { MatSnackBar } from "@angular/material";
import * as copy from 'copy-to-clipboard';
import { DialogService } from "../../services/dialog.service";
import { AuthService } from "../../services/auth.service";
import { SnackBarService } from "../../services/snack-bar.service";

@Component({
  selector: 'app-tweet-options',
  templateUrl: './tweet-options.component.html',
  styleUrls: ['./tweet-options.component.scss']
})
export class TweetOptionsComponent {
  @Input() tweet: TweetResponseDTO;
  @Output() tweetDeleted = new EventEmitter<TweetResponseDTO>();

  constructor(private snackBarService: SnackBarService,
              public dialogService: DialogService,
              public authService: AuthService) {
  }

  copyLinkToClipboard(): void {
    const url = new URL(location.toString());
    url.pathname = `/u/${this.tweet.user.username}/tweet/${this.tweet.id}`;

    copy(url.toString());

    this.snackBarService.showInfoSnackBar("Link copied.");
  }

  showDeleteTweetDialog(): void {
    const dialog = this.dialogService.showDeleteTweetDialog(this.tweet);
    dialog.afterClosed()
      .filter(result => !!result)
      .subscribe(() => {
        console.log("Dialog deleted");
        this.tweetDeleted.next(this.tweet);
      });
  }
}
