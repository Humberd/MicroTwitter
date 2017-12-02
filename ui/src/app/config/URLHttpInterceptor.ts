import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable } from "rxjs/Observable";
import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";

@Injectable()
export class URLHttpInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let appRequest = req;

    /* Only prepend apiUrl to relative paths. Absolute url paths should stay as there are */
    if (appRequest.url.startsWith("/api")) {
      appRequest = appRequest.clone({
        url: `${environment.apiUrl}${req.url.replace("/api", "")}`
      });
    }

    return next.handle(appRequest);
  }

}
