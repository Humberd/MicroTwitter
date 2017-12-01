import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthHttpService } from "./http/auth-http.service";
import { AuthService } from "./services/auth.service";
import { TweetHttpService } from "./http/tweet-http.service";
import { UserHttpService } from "./http/user-http.service";
import { AuthorizedGuard } from "./guards/authorized.guard";
import { UnauthorizedGuard } from "./guards/unauthorized.guard";
import {
  MatAutocompleteModule, MatButtonModule, MatDialogModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule, MatMenuModule,
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
import { TweetComponent } from './components/tweet/tweet.component';
import { ReplyNewTweetDialogComponent } from './components/reply-new-tweet-dialog/reply-new-tweet-dialog.component';
import { RouterModule } from "@angular/router";
import { TweetListComponent } from './components/tweet-list/tweet-list.component';

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
  ],
  declarations: [
    DynamicColorDirective,
    DynamicBackgroundColorDirective,
    AutofocusDirective,

    SearchUserComponent,
    UserListElementSmallComponent,
    NewTweetFormComponent,
    NewTweetDialogComponent,
    TweetComponent,
    ReplyNewTweetDialogComponent,
    TweetListComponent,
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
    TweetListComponent
  ],
  entryComponents: [
    NewTweetDialogComponent,
    ReplyNewTweetDialogComponent
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
