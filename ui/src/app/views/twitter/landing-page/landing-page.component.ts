import { Component, OnInit } from '@angular/core';
import { CONSTANTS } from "../../../config/Constants";

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent implements OnInit {
  CONSTANTS = CONSTANTS;

  constructor() { }

  ngOnInit() {
  }

}
