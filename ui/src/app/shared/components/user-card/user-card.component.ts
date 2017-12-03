import { Component, Input, OnInit } from '@angular/core';
import { UserResponseDTO } from "../../../dto/UserResponseDTO";
import { AuthService } from "../../services/auth.service";

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.scss']
})
export class UserCardComponent implements OnInit {
  @Input() user: UserResponseDTO;

  constructor(public authService: AuthService) {
  }

  ngOnInit() {
  }

}
