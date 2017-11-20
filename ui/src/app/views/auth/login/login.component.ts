import { Component, OnInit } from '@angular/core';
import { AuthHttpService } from "../../../shared/http/auth-http.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private authHttpService: AuthHttpService) {
  }

  ngOnInit() {
    // this.authHttpService.login()
  }

}
