import { Component } from '@angular/core';
import { AuthService } from "../../../shared/services/auth.service";

@Component({
  selector: 'app-root-wrapper',
  templateUrl: './root-wrapper.component.html',
  styleUrls: ['./root-wrapper.component.scss']
})
export class RootWrapperComponent {

  constructor(public authService: AuthService) {
  }

}
