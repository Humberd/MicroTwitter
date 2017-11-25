import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import 'rxjs/Rx';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthViewsModule } from "./views/auth/auth.module";
import { SharedModule } from "./shared/_shared.module";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { JWTHttpInterceptor } from "./config/JWTHttpInterceptor";
import { URLHttpInterceptor } from "./config/URLHttpInterceptor";
import { TwitterViewsModule } from "./views/twitter/twitter.module";
import { AuthService } from "./shared/auth.service";
import { LayoutModule } from "./layout/layout.module";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    SharedModule,
    AuthViewsModule,
    TwitterViewsModule,
    LayoutModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: URLHttpInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JWTHttpInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {

  constructor(private authService: AuthService) {
    this.authService.readFromStorage();
  }
}
