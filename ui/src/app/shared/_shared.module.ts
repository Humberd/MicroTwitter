import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthHttpService } from "./http/auth-http.service";
import { AuthService } from "./auth.service";
import { LayoutModule } from "./layout/layout.module";
import { TweetHttpService } from "./http/tweet-http.service";
import { UserHttpService } from "./http/user-http.service";
import { DirectivesModule } from "./directives/_directives.module";

@NgModule({
  imports: [
    CommonModule,
    LayoutModule,
    DirectivesModule
  ],
  declarations: [],
  providers: [
    AuthHttpService,
    TweetHttpService,
    UserHttpService,

    AuthService,
  ]
})
export class SharedModule {
}
