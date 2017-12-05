import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material";
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";
import { UserHttpService } from "../../http/user-http.service";
import { UserResponseDTO } from "../../../dto/UserResponseDTO";
import { TweetHttpService } from "../../http/tweet-http.service";
import { AbstractScrollPageableComponent } from "../../AbstractScrollPageableComponent";
import { PageDTO } from "../../../dto/PageDTO";
import { Observable } from "rxjs/Observable";

@Component({
  selector: 'app-tweet-info-dialog',
  templateUrl: './tweet-info-dialog.component.html',
  styleUrls: ['./tweet-info-dialog.component.scss']
})
export class TweetInfoDialogComponent extends AbstractScrollPageableComponent<TweetResponseDTO> implements OnInit{
  tweet: TweetResponseDTO;
  user: UserResponseDTO;

  constructor(private userHttpService: UserHttpService,
              private tweetHttpService: TweetHttpService,
              public dialogRef: MatDialogRef<TweetInfoDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    super();
    this.tweet = data.tweet;
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
}
