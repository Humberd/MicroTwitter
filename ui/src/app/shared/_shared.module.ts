import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthHttpService } from "./http/auth-http.service";
import { AuthService } from "./auth.service";
import { TweetHttpService } from "./http/tweet-http.service";
import { UserHttpService } from "./http/user-http.service";
import { AuthorizedGuard } from "./guards/authorized.guard";
import { UnauthorizedGuard } from "./guards/unauthorized.guard";
import {
  MatAutocompleteModule,
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

@NgModule({
  imports: [
    CommonModule,
    MatSnackBarModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatAutocompleteModule,
    MatOptionModule,
    MatIconModule,
    MatMenuModule,
  ],
  declarations: [
    DynamicColorDirective,
    DynamicBackgroundColorDirective,
    AutofocusDirective,

    SearchUserComponent,
    UserListElementSmallComponent,
  ],
  exports: [
    DynamicColorDirective,
    DynamicBackgroundColorDirective,
    AutofocusDirective,

    SearchUserComponent,
    UserListElementSmallComponent,
  ],
  providers: [
    AuthHttpService,
    TweetHttpService,
    UserHttpService,

    AuthorizedGuard,
    UnauthorizedGuard,

    AuthService,
  ]
})
export class SharedModule {
}
