import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable } from "rxjs/Observable";
import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";

@Injectable()
export class URLHttpInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const appRequest = req.clone({
      url: `${environment.apiUrl}${req.url}`
    });
    return next.handle(appRequest);
  }

}
