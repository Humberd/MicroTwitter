import { Component, OnInit } from '@angular/core';
import { AuthService } from "../../auth.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  constructor(private authService: AuthService) { }

  ngOnInit() {

  }

  login() {
    this.authService.signup({
      username: "admin",
      email: "admin@admin.com",
      fullName: "AdminAdmin",
      password: "admin123"
    })
      .subscribe(response => {
        console.log(response);
      }, error => {
        console.error(error);
      });
  }

}
