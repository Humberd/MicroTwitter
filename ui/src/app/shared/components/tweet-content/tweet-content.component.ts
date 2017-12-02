import { Component, Input } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";

@Component({
  selector: 'app-tweet-content',
  templateUrl: './tweet-content.component.html',
  styleUrls: ['./tweet-content.component.scss'],
})
export class TweetContentComponent {
  @Input() tweet: TweetResponseDTO;

  constructor() {
  }
}
