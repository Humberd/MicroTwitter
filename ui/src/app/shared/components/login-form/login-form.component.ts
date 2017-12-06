import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../services/auth.service";
import { CONSTANTS } from "../../../config/Constants";
import { LoginDTO } from "../../../dto/LoginDTO";

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
          console.log("error login", error);
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
