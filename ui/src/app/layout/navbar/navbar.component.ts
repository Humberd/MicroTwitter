import { Component } from '@angular/core';
import { AuthService } from "../../shared/services/auth.service";
import { Router } from "@angular/router";
import { CONSTANTS } from "../../config/Constants";
import { DialogService } from "../../shared/services/dialog.service";
import { TweetHttpService } from "../../shared/http/tweet-http.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  constructor(public authService: AuthService,
              public dialogService: DialogService,
              private tweetHttpService: TweetHttpService,
              private router: Router) {
    setTimeout(() => {
      this.tweetHttpService.getTweet(2)
        .subscribe(tweet => this.dialogService.showReplyToTweetDialog(tweet));
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(CONSTANTS.DEFAULT_UNAUTH_ROUTE);
  }
}
