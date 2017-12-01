import { Component, Input } from '@angular/core';
import { UserResponseDTO } from "../../../../dto/UserResponseDTO";

@Component({
  selector: 'app-tabs',
  templateUrl: './tabs.component.html',
  styleUrls: ['./tabs.component.scss']
})
export class TabsComponent {
  @Input() user: UserResponseDTO;

  constructor() {
  }
}
