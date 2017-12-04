import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs/Observable";
import { Injectable, Injector } from "@angular/core";
import { AuthService } from "../shared/services/auth.service";
import { CONSTANTS } from "./Constants";
import { Router } from "@angular/router";
import { isString } from "util";

@Injectable()
export class JWTHttpInterceptor implements HttpInterceptor {
  private authService: AuthService;

  constructor(private injector: Injector,
              private router: Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    /* Have to inject AuthService through injector, because otherwise we have a circular dependency*/
    this.authService = this.authService || this.injector.get(AuthService);

    let newHeaders: HttpHeaders = req.headers;

    /* We add an Authorization header only to those requests that don't have it.
     * We do not want override every request, because some requests '/me' need a special treatment */
    if (!req.headers.has(CONSTANTS.AUTH_HEADER_NAME)) {
      if (this.authService.isUserLoggedIn()) {
        newHeaders = newHeaders.set(CONSTANTS.AUTH_HEADER_NAME, CONSTANTS.AUTH_TOKEN_PREFIX + this.authService.getUser().jwtToken);
      }
      /* If the logged user has not yet been confirmed, but we already have his jwt from storage lets add it to those requests */
      // tslint:disable-next-line one-line
      else if (isString(this.authService.initialJWT)) {
        newHeaders = newHeaders.set(CONSTANTS.AUTH_HEADER_NAME, CONSTANTS.AUTH_TOKEN_PREFIX + this.authService.initialJWT);
      }
    }

    const appRequest = req.clone({
      headers: newHeaders
    });

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
