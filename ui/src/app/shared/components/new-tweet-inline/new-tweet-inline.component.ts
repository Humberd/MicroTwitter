import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";
import { NewTweetFormComponent } from "../new-tweet-form/new-tweet-form.component";
import { AuthService } from "../../services/auth.service";

@Component({
  selector: 'app-new-tweet-inline',
  templateUrl: './new-tweet-inline.component.html',
  styleUrls: ['./new-tweet-inline.component.scss']
})
export class NewTweetInlineComponent {
  @Input() inReplyToTweet: TweetResponseDTO;
  @Output() tweetCreated = new EventEmitter<TweetResponseDTO>();
  @ViewChild(NewTweetFormComponent) newTweetFormComponent: NewTweetFormComponent;

  constructor(public authService: AuthService) {
  }

  public focusTweetInput(): void {
    if (!this.newTweetFormComponent) {
      console.warn("There is no NewTweetFormComponent");
      return;
    }

    this.newTweetFormComponent.focusTweetInput();
  }
}
