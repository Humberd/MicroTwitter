import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs/Observable";
import { Injectable } from "@angular/core";
import { AuthService } from "../shared/auth.service";
import { CONSTANTS } from "./Constants";
import { Router } from "@angular/router";

@Injectable()
export class JWTHttpInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService,
              private router: Router) {

  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const appRequest = req.clone();

    if (this.authService.isUserLoggedIn()) {
      appRequest.headers.set(CONSTANTS.AUTH_HEADER_NAME, CONSTANTS.AUTH_TOKEN_PREFIX + this.authService.getUser().jwtToken);
    }

    return next.handle(appRequest)
      .do(response => {
        if (response instanceof HttpResponse) {
          if (response.status === 403) {
            this.authService.logout();
            this.router.navigate([""]);
          }
        }
      });
  }
}
