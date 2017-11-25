import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from "@angular/forms";
import { AuthService } from "../../../../shared/auth.service";
import { Router } from "@angular/router";
import { CONSTANTS } from "../../../../config/Constants";
import { LoginDTO } from "../../../../dto/LoginDTO";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: [
    './login-form.component.scss',
    '../../_shared.scss'
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

  private initForm(): void {
    this.loginForm = new FormGroup({
      username: new FormControl(""),
      password: new FormControl("")
    });
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

  private prepareData(): LoginDTO {
    return {
      ...this.loginForm.value
    };
  }

  mocklogin() {
    this.authService.signup({
      username: this.makeid(),
      email: this.makeid() + "@google.com",
      fullName: "AdminAdmin",
      password: "admin123"
    })
      .subscribe(response => {
        console.log(response);
        this.router.navigate(CONSTANTS.DEFAULT_AUTH_ROUTE);
      }, error => {
        console.error(error);
      });
  }

  makeid() {
    let text = "";
    const possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for (let i = 0; i < 5; i++) {
      text += possible.charAt(Math.floor(Math.random() * possible.length));
    }
    return text;
  }
}
