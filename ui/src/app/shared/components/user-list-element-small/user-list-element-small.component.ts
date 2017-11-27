import { Component, Input } from '@angular/core';
import { UserResponseDTO } from "../../../dto/UserResponseDTO";

@Component({
  selector: 'app-user-list-element-small',
  templateUrl: './user-list-element-small.component.html',
  styleUrls: ['./user-list-element-small.component.scss'],
})
export class UserListElementSmallComponent {
  @Input() user: UserResponseDTO;

  constructor() {
  }
}
