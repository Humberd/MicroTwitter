import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";

@Component({
  selector: 'app-tweet-block',
  templateUrl: './tweet-block.component.html',
  styleUrls: ['./tweet-block.component.scss']
})
export class TweetBlockComponent {
  @Input() tweet: TweetResponseDTO;
  @Output() tweetDeleted = new EventEmitter<TweetResponseDTO>();

  constructor() {
  }
}