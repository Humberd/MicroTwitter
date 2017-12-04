import { Component, Input } from '@angular/core';
import { UserResponseDTO } from "../../../../dto/UserResponseDTO";

@Component({
  selector: 'app-wall-user-card',
  templateUrl: './wall-user-card.component.html',
  styleUrls: ['./wall-user-card.component.scss']
})
export class WallUserCardComponent {
  @Input() user: UserResponseDTO;

  constructor() {
  }

}
