import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../services/auth.service";
import { CONSTANTS } from "../../../config/Constants";
import { LoginDTO } from "../../../dto/LoginDTO";
import { SnackBarService } from "../../services/snack-bar.service";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: [
    './login-form.component.scss',
  ]
})
export class LoginFormComponent implements OnInit {
  loginForm: FormGroup;
  @Input() shouldAutofocus: boolean;

  constructor(private authService: AuthService,
              private snackBarService: SnackBarService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.initForm();
  }

  login(): void {
    const requestData = this.prepareData();

    this.authService.login(requestData)
      .subscribe(() => {
          this.router.navigate(CONSTANTS.DEFAULT_AUTH_ROUTE);
        },
        error => {
          this.snackBarService.showLongInfoSnackBar(
            "The username and password that you entered did not match our records. Please double-check and try again.");
          console.error("Cannot login", error);
        });
  }

  private initForm(): void {
    this.loginForm = new FormGroup({
      username: new FormControl(""),
      password: new FormControl("")
    });
  }

  private prepareData(): LoginDTO {
    return {
      ...this.loginForm.value
    };
  }
}
