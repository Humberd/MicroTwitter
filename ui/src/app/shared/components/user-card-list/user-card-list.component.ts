import { Component, Input } from '@angular/core';
import { UserResponseDTO } from "../../../dto/UserResponseDTO";

@Component({
  selector: 'app-user-card-list',
  templateUrl: './user-card-list.component.html',
  styleUrls: ['./user-card-list.component.scss']
})
export class UserCardListComponent {
  @Input() userList: UserResponseDTO[];

  constructor() {
  }
}
