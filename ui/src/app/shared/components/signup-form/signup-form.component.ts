import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../services/auth.service";
import { CONSTANTS } from "../../../config/Constants";
import { SignupDTO } from "../../../dto/SignupDTO";
import { SnackBarService } from "../../services/snack-bar.service";

@Component({
  selector: 'app-signup-form',
  templateUrl: './signup-form.component.html',
  styleUrls: [
    './signup-form.component.scss',
  ]
})
export class SignupFormComponent implements OnInit {
  signupForm: FormGroup;
  @Input() shouldAutofocus: boolean;

  constructor(private authService: AuthService,
              private snackBarService: SnackBarService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.initForm();
  }

  signup(): void {
    const requestData = this.prepareData();

    this.authService.signup(requestData)
      .subscribe(() => {
          this.router.navigate(CONSTANTS.DEFAULT_AUTH_ROUTE);
        },
        error => {
          this.snackBarService.showLongInfoSnackBar("Error signing up");
          console.error("Cannot signup", error);
        });
  }

  private initForm(): void {
    this.signupForm = new FormGroup({
      username: new FormControl("", Validators.required),
      email: new FormControl("", [
        Validators.required,
        Validators.email
      ]),
      fullName: new FormControl("", Validators.required),
      password: new FormControl("", Validators.required)
    });
  }

  private prepareData(): SignupDTO {
    return {
      ...this.signupForm.value
    };
  }
}
