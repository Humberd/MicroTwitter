import { Component, Input } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";
import { UserResponseDTO } from "../../../dto/UserResponseDTO";

@Component({
  selector: 'app-tweet-info-block',
  templateUrl: './tweet-info-block.component.html',
  styleUrls: ['./tweet-info-block.component.scss']
})
export class TweetInfoBlockComponent {
  @Input() tweet: TweetResponseDTO;
  @Input() user: UserResponseDTO;
  @Input() replyCommentAction: (tweet: TweetResponseDTO) => any;

  constructor() {
  }

}
