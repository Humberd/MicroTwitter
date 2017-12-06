import { Component, Input } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";

@Component({
  selector: 'app-tweet-list',
  templateUrl: './tweet-list.component.html',
  styleUrls: ['./tweet-list.component.scss']
})
export class TweetListComponent {
  @Input() tweetList: TweetResponseDTO[];
  @Input() tweetClickAction: (tweet: TweetResponseDTO) => any;

  constructor() {
  }

  removeTweetFromList(index: number): void {
    this.tweetList.splice(index, 1);
  }
}
