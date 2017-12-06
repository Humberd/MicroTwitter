import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../services/auth.service";
import { CONSTANTS } from "../../../config/Constants";
import { SignupDTO } from "../../../dto/SignupDTO";

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
          console.log("error signup", error);
        });
  }

  private initForm(): void {
    this.signupForm = new FormGroup({
      username: new FormControl(""),
      email: new FormControl(""),
      fullName: new FormControl(""),
      password: new FormControl("")
    });
  }

  private prepareData(): SignupDTO {
    return {
      ...this.signupForm.value
    };
  }
}
