import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from "@angular/forms";
import { AuthService } from "../../../../shared/auth.service";
import { Router } from "@angular/router";
import { SignupDTO } from "../../../../dto/SignupDTO";
import { CONSTANTS } from "../../../../config/Constants";

@Component({
  selector: 'app-signup-form',
  templateUrl: './signup-form.component.html',
  styleUrls: [
    './signup-form.component.scss',
    '../../_shared.scss'
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

  private initForm(): void {
    this.signupForm = new FormGroup({
      username: new FormControl(""),
      email: new FormControl(""),
      fullName: new FormControl(""),
      password: new FormControl("")
    });
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

  private prepareData(): SignupDTO {
    return {
      ...this.signupForm.value
    };
  }
}
