import { Component, OnInit } from '@angular/core';
import { AuthService } from "../../auth.service";
import { Router } from "@angular/router";
import { CONSTANTS } from "../../../config/Constants";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  constructor(public authService: AuthService,
              private router: Router) {
  }

  ngOnInit() {

  }

  login() {
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

  logout() {
    this.authService.logout();
    this.router.navigate(CONSTANTS.DEFAULT_UNAUTH_ROUTE);
  }

}
