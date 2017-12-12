import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthHttpService } from "./http/auth-http.service";
import { AuthService } from "./services/auth.service";
import { TweetHttpService } from "./http/tweet-http.service";
import { UserHttpService } from "./http/user-http.service";
import { AuthorizedGuard } from "./guards/authorized.guard";
import { UnauthorizedGuard } from "./guards/unauthorized.guard";
import {
  MatAutocompleteModule,
  MatButtonModule,
  MatDialogModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatMenuModule,
  MatOptionModule,
  MatSnackBarModule
} from "@angular/material";
import { AutofocusDirective } from "./directives/autofocus.directive";
import { SearchUserComponent } from './components/search-user/search-user.component';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { UserListElementSmallComponent } from './components/user-list-element-small/user-list-element-small.component';
import { NewTweetFormComponent } from './components/new-tweet-form/new-tweet-form.component';
import { NewTweetDialogComponent } from './components/new-tweet-dialog/new-tweet-dialog.component';
import { DialogService } from "./services/dialog.service";
import { TweetContentComponent } from './components/tweet-content/tweet-content.component';
import { ReplyNewTweetDialogComponent } from './components/reply-new-tweet-dialog/reply-new-tweet-dialog.component';
import { RouterModule } from "@angular/router";
import { TweetListComponent } from './components/tweet-list/tweet-list.component';
import { TweetBlockComponent } from './components/tweet-block/tweet-block.component';
import { TweetActionsComponent } from './components/tweet-actions/tweet-actions.component';
import { TweetOptionsComponent } from './components/tweet-options/tweet-options.component';
import { InfiniteScrollModule } from "ngx-infinite-scroll";
import { DeleteTweetDialogComponent } from './components/delete-tweet-dialog/delete-tweet-dialog.component';
import { UserCardComponent } from './components/user-card/user-card.component';
import { FollowButtonComponent } from './components/follow-button/follow-button.component';
import { UserCardListComponent } from './components/user-card-list/user-card-list.component';
import { UserBasicBigComponent } from './components/user-basic-big/user-basic-big.component';
import { BirthdatePipe } from './pipes/birthdate.pipe';
import { NewTweetInlineComponent } from './components/new-tweet-inline/new-tweet-inline.component';
import { TweetInfoBlockComponent } from './components/tweet-info-block/tweet-info-block.component';
import { TweetInfoDialogComponent } from './components/tweet-info-dialog/tweet-info-dialog.component';
import { LoginFormComponent } from "./components/login-form/login-form.component";
import { SignupFormComponent } from "./components/signup-form/signup-form.component";
import { AfterAuthCheckGuard } from "./guards/after-auth-check.guard";
import { SnackBarService } from "./services/snack-bar.service";
import { UpdateProfileFormComponent } from './components/update-profile-form/update-profile-form.component';
import { UrlPipe } from './pipes/url.pipe';
import { AvatarUrlPipe } from './pipes/avatar-url.pipe';
import { DynamicStylesService } from "./services/dynamic-styles.service";
import { AuthOnlyClickDirective } from './directives/auth-only-click.directive';
import { LoginDialogComponent } from './components/login-dialog/login-dialog.component';
import { NewToTwitterPanelComponent } from './components/new-to-twitter-panel/new-to-twitter-panel.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatAutocompleteModule,
    MatOptionModule,
    MatIconModule,
    MatMenuModule,
    MatDialogModule,
    MatButtonModule,
    MatSnackBarModule,
    InfiniteScrollModule,
  ],
  declarations: [
    AutofocusDirective,
    AuthOnlyClickDirective,

    BirthdatePipe,
    UrlPipe,
    AvatarUrlPipe,

    SearchUserComponent,
    UserListElementSmallComponent,
    NewTweetFormComponent,
    NewTweetDialogComponent,
    TweetContentComponent,
    ReplyNewTweetDialogComponent,
    TweetListComponent,
    TweetBlockComponent,
    TweetActionsComponent,
    TweetOptionsComponent,
    DeleteTweetDialogComponent,
    UserCardComponent,
    FollowButtonComponent,
    UserCardListComponent,
    UserBasicBigComponent,
    NewTweetInlineComponent,
    TweetInfoBlockComponent,
    TweetInfoDialogComponent,
    LoginFormComponent,
    SignupFormComponent,
    UpdateProfileFormComponent,
    LoginDialogComponent,
    NewToTwitterPanelComponent,
  ],
  exports: [
    AutofocusDirective,
    AuthOnlyClickDirective,

    BirthdatePipe,
    UrlPipe,
    AvatarUrlPipe,

    SearchUserComponent,
    NewTweetFormComponent,
    NewTweetDialogComponent,
    UserListElementSmallComponent,
    ReplyNewTweetDialogComponent,
    TweetListComponent,
    TweetBlockComponent,
    TweetActionsComponent,
    TweetOptionsComponent,
    DeleteTweetDialogComponent,
    UserCardComponent,
    FollowButtonComponent,
    UserCardListComponent,
    UserBasicBigComponent,
    NewTweetInlineComponent,
    LoginFormComponent,
    SignupFormComponent,
    UpdateProfileFormComponent,
    LoginDialogComponent,
    NewToTwitterPanelComponent,
  ],
  entryComponents: [
    NewTweetDialogComponent,
    ReplyNewTweetDialogComponent,
    DeleteTweetDialogComponent,
    TweetInfoDialogComponent,
    LoginDialogComponent,
  ],
  providers: [
    AuthHttpService,
    TweetHttpService,
    UserHttpService,

    AuthorizedGuard,
    UnauthorizedGuard,
    AfterAuthCheckGuard,

    AuthService,
    DialogService,
    SnackBarService,
    DynamicStylesService,
  ]
})
export class SharedModule {
}
