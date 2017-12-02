import { Component, Input, OnInit } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";

@Component({
  selector: 'app-tweet-options',
  templateUrl: './tweet-options.component.html',
  styleUrls: ['./tweet-options.component.scss']
})
export class TweetOptionsComponent implements OnInit {
  @Input() tweet: TweetResponseDTO;

  constructor() { }

  ngOnInit() {
  }

}
