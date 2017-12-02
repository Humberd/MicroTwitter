import { Component, Input, OnInit } from '@angular/core';
import { TweetResponseDTO } from "../../../dto/TweetResponseDTO";

@Component({
  selector: 'app-tweet-list',
  templateUrl: './tweet-list.component.html',
  styleUrls: ['./tweet-list.component.scss']
})
export class TweetListComponent implements OnInit {
  @Input() tweetList: TweetResponseDTO[];

  constructor() {
  }

  ngOnInit() {
    console.log(this.tweetList);
  }

}
