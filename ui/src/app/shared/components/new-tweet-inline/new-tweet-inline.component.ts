import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";

@Component({
  selector: 'app-new-tweet-inline',
  templateUrl: './new-tweet-inline.component.html',
  styleUrls: ['./new-tweet-inline.component.scss']
})
export class NewTweetInlineComponent {
  @Input() inReplyToTweet: TweetResponseDTO;
  @Output() tweetCreated = new EventEmitter<TweetResponseDTO>();

  constructor() {
  }
}
