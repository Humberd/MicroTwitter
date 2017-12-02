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
import { DynamicBackgroundColorDirective } from "./directives/dynamic-background-color.directive";
import { DynamicColorDirective } from "./directives/dynamic-color.directive";
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
    DynamicColorDirective,
    DynamicBackgroundColorDirective,
    AutofocusDirective,

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
  ],
  exports: [
    DynamicColorDirective,
    DynamicBackgroundColorDirective,
    AutofocusDirective,

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
  ],
  entryComponents: [
    NewTweetDialogComponent,
    ReplyNewTweetDialogComponent,
    DeleteTweetDialogComponent,
  ],
  providers: [
    AuthHttpService,
    TweetHttpService,
    UserHttpService,

    AuthorizedGuard,
    UnauthorizedGuard,

    AuthService,
    DialogService,
  ]
})
export class SharedModule {
}
