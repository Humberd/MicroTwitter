import { Component, Input, OnInit } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";

@Component({
  selector: 'app-tweet-block',
  templateUrl: './tweet-block.component.html',
  styleUrls: ['./tweet-block.component.scss']
})
export class TweetBlockComponent implements OnInit {
  @Input() tweet: TweetResponseDTO;

  constructor() { }

  ngOnInit() {
  }

}
