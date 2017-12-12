import { Directive, EventEmitter, HostListener, Output } from '@angular/core';
import { AuthService } from "../services/auth.service";
import { DialogService } from "../services/dialog.service";

/**
 * This directive emits click events only when user has been authenticated otherwise prompt login dialog
 */
@Directive({
  // tslint:disable-next-line
  selector: '[authOnlyClick]'
})
export class AuthOnlyClickDirective {
  @Output() authOnlyClick = new EventEmitter<any>();

  constructor(private authService: AuthService,
              private dialogService: DialogService) {
  }

  @HostListener("click", ["$event"])
  onClick(e: Event) {
    if (!this.authService.isUserLoggedIn()) {
      e.preventDefault();
      e.stopPropagation();
      this.dialogService.showLoginDialog();
    } else {
      console.log("click?");
      this.authOnlyClick.next(event);
    }
  }
}
