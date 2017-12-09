import { Component, OnInit } from '@angular/core';
import { AuthService } from "../../../shared/services/auth.service";
import { DynamicStylesService } from "../../../shared/services/dynamic-styles.service";

@Component({
  selector: 'app-wall',
  templateUrl: './wall.component.html',
  styleUrls: ['./wall.component.scss']
})
export class WallComponent implements OnInit {
  constructor(public authService: AuthService,
              private dynamicStylesService: DynamicStylesService) {
  }

  ngOnInit(): void {
    this.dynamicStylesService.setUser(this.authService.getUser().data);
  }

}
