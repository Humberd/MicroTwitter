import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { AuthService } from "../services/auth.service";

/**
 * Pass through only when the initial user check has been finished.
 */
@Injectable()
export class AfterAuthCheckGuard implements CanActivate {
  constructor(private authService: AuthService) {
  }

  canActivate(next: ActivatedRouteSnapshot,
              state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return this.authService.isInitialAuthCheckFinished()
      .filter(val => val);
  }
}
