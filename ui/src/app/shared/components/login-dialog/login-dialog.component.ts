import { Component } from '@angular/core';
import { CONSTANTS } from "../../../config/Constants";
import { MatDialogRef } from "@angular/material";

@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: [
    './login-dialog.component.scss',
    '../_dialog.scss'
  ]
})
export class LoginDialogComponent {
  CONSTANTS = CONSTANTS;

  constructor(public dialogRef: MatDialogRef<LoginDialogComponent>) {
  }


  onSuccessLogin() {
    // TODO: better handle login success for example by sending event to other components or go to a different route and go back
    // https://github.com/angular/angular/issues/13831#issuecomment-319634921 ???
    location.reload();
  }
}
