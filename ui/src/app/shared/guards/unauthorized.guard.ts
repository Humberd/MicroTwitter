import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { isUndefined } from "util";
import { MatSnackBar } from "@angular/material";
import { AuthService } from '../auth.service';

@Injectable()
export class UnauthorizedGuard implements CanActivate {
  constructor(private authService: AuthService,
              private router: Router,
              private snackBar: MatSnackBar) {
  }

  canActivate(next: ActivatedRouteSnapshot,
              state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return this.authService.getUserObs()
      .filter(user => !isUndefined(user))
      .map(user => !!user)
      .do(isAuthorized => {
        if (isAuthorized) {
          this.snackBar.open("You need to be unauthorized.", "Close");
          console.info(`Cannot go to the ${state.url}. User must be unauthorized`);

          /* If this is the first route loading */
          if (!this.router.navigated) {
            this.router.navigate(["wall"]);
          }
        }
      })
      .map(isAuthorized => !isAuthorized);
  }
}
