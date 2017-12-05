import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";
import { DialogService } from "../../services/dialog.service";
import { isFunction } from "util";

@Component({
  selector: 'app-tweet-block',
  templateUrl: './tweet-block.component.html',
  styleUrls: ['./tweet-block.component.scss']
})
export class TweetBlockComponent {
  @Input() tweet: TweetResponseDTO;
  @Input() tweetClickAction: (tweet: TweetResponseDTO) => any;
  @Output() tweetDeleted = new EventEmitter<TweetResponseDTO>();

  constructor(public dialogService: DialogService) {
  }

  @HostListener("click")
  showTweetInfoDialog(): void {
    if (isFunction(this.tweetClickAction)) {
      this.tweetClickAction(this.tweet);
      return;
    }

    this.dialogService.showTweetInfoDialog(this.tweet);
  }
}
