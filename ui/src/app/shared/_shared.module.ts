import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthHttpService } from "./http/auth-http.service";
import { AuthService } from "./auth.service";
import { LayoutModule } from "./layout/layout.module";
import { TweetHttpService } from "./http/tweet-http.service";
import { UserHttpService } from "./http/user-http.service";
import { DirectivesModule } from "./directives/_directives.module";
import { AuthorizedGuard } from "./guards/authorized.guard";
import { UnauthorizedGuard } from "./guards/unauthorized.guard";
import { MatSnackBarModule } from "@angular/material";

@NgModule({
  imports: [
    CommonModule,
    LayoutModule,
    DirectivesModule,
    MatSnackBarModule
  ],
  declarations: [],
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
