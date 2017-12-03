import { Component, Input } from '@angular/core';
import { UserResponseDTO } from "../../../../dto/UserResponseDTO";

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.scss']
})
export class UserInfoComponent {
  @Input() user: UserResponseDTO;

  constructor() {
  }
}
