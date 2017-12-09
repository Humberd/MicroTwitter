import { Component, Inject, Injector, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material";
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";
import { UserHttpService } from "../../http/user-http.service";
import { UserResponseDTO } from "../../../dto/UserResponseDTO";
import { TweetHttpService } from "../../http/tweet-http.service";
import { AbstractScrollPageableComponent } from "../../AbstractScrollPageableComponent";
import { PageDTO } from "../../../dto/PageDTO";
import { Observable } from "rxjs/Observable";
import { NewTweetInlineComponent } from "../new-tweet-inline/new-tweet-inline.component";
import { DialogService } from "../../services/dialog.service";
import { AuthService } from "../../services/auth.service";

@Component({
  selector: 'app-tweet-info-dialog',
  templateUrl: './tweet-info-dialog.component.html',
  styleUrls: ['./tweet-info-dialog.component.scss']
})
export class TweetInfoDialogComponent extends AbstractScrollPageableComponent<TweetResponseDTO> implements OnInit {
  tweet: TweetResponseDTO;
  user: UserResponseDTO;

  @ViewChild(NewTweetInlineComponent) newTweetInlineComponent: NewTweetInlineComponent;

  private dialogService: DialogService;

  constructor(private userHttpService: UserHttpService,
              private tweetHttpService: TweetHttpService,
              public dialogRef: MatDialogRef<TweetInfoDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    super();
    this.tweet = data.tweet;
    this.dialogService = data.dialogService;
  }

  ngOnInit(): void {
    this.userHttpService.getUser(this.tweet.user.username)
      .subscribe(user => this.user = user);

    super.getPage(this.tweet.id)
      .subscribe();
  }

  invokeGetPageMethod(...params: any[]): Observable<PageDTO<TweetResponseDTO>> {
    return this.tweetHttpService.getComments(...params);
  }

  public requestNextPage(): void {
    super.requestNextPage(this.tweet.id);
  }

  protected shouldRemoveNewPageItem(lastListItem, newPageFirstItem): boolean {
    return lastListItem["id"] >= newPageFirstItem["id"];
  }

  /**
   * When the user clicks comment we don't want to open an new modal with a comment.
   * Instead want to focus a a new tweet form that is currently visible in this modal
   */
  replyCommentAction(): void {
    if (!this.newTweetInlineComponent) {
      console.warn("There is no child NewTweetInlineComponent");
      return;
    }

    this.newTweetInlineComponent.focusTweetInput();
  }

  /**
   * When the user clicks
   * @param {TweetResponseDTO} tweet
   */
  tweetClickAction(tweet: TweetResponseDTO): void {
    this.dialogRef.close();
    this.dialogService.showTweetInfoDialog(tweet);
  }
}
