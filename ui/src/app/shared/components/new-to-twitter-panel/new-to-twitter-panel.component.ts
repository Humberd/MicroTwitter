import { Component, OnInit } from '@angular/core';
import { CONSTANTS } from "../../../config/Constants";

@Component({
  selector: 'app-new-to-twitter-panel',
  templateUrl: './new-to-twitter-panel.component.html',
  styleUrls: ['./new-to-twitter-panel.component.scss']
})
export class NewToTwitterPanelComponent implements OnInit {
  CONSTANTS = CONSTANTS;

  constructor() { }

  ngOnInit() {
  }

}
