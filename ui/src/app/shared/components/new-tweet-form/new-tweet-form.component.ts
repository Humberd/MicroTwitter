import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { TweetHttpService } from "../../http/tweet-http.service";
import { TweetCreateDTO } from "../../../dto/TweetCreateDTO";
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";
import { MatSnackBar } from "@angular/material";
import { isNumber } from "util";

@Component({
  selector: 'app-new-tweet-form',
  templateUrl: './new-tweet-form.component.html',
  styleUrls: ['./new-tweet-form.component.scss']
})
export class NewTweetFormComponent implements OnInit {
  @Input() shouldAutofocus: boolean;
  @Input() inReplyToTweet: TweetResponseDTO;
  @Output() tweetCreated = new EventEmitter<TweetResponseDTO>();

  newTweetForm: FormGroup;

  constructor(private tweetHttpService: TweetHttpService,
              private matSnackbar: MatSnackBar) {
  }

  ngOnInit() {
    this.initForm();
  }

  private initForm(): void {
    this.newTweetForm = new FormGroup({
      content: new FormControl("", [Validators.required]),
    });
  }

  createTweet(): void {
    const requestData = this.prepareData();

    this.tweetHttpService.createTweet(requestData)
      .do(newTweet => console.info("Tweet created"))
      .do(newTweet => this.tweetCreated.emit(newTweet))
      .do(newTweet => this.showSnackBar(newTweet))
      .subscribe();
  }

  private showSnackBar(newTweet: TweetResponseDTO): void {
    let message;
    if (isNumber(newTweet.inReplyToTweetId)) {
      message = `Your Tweet to @${newTweet.inReplyToUser.username} has been sent!`;
    } else {
      message = `Your Tweet was posted!`;
    }
    this.matSnackbar.open(message, "", {
      duration: 2500,
      verticalPosition: "top",
    });
  }

  private prepareData(): TweetCreateDTO {
    return {
      ...this.newTweetForm.value,
      inReplyToTweetId: this.inReplyToTweet ? this.inReplyToTweet.id : null
    };
  }

}
