import { Component, Input, OnInit } from '@angular/core';
import { UserResponseDTO } from "../../../../dto/UserResponseDTO";

@Component({
  selector: 'app-tabs',
  templateUrl: './tabs.component.html',
  styleUrls: ['./tabs.component.scss']
})
export class TabsComponent implements OnInit {
  @Input() user: UserResponseDTO;

  constructor() { }

  ngOnInit() {
  }

}
