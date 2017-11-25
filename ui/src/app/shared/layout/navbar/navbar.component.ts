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

  logout() {
    this.authService.logout();
    this.router.navigate(CONSTANTS.DEFAULT_UNAUTH_ROUTE);
  }
}
