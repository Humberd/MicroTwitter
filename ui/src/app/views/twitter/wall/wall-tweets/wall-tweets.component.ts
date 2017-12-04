import { Component, OnInit } from '@angular/core';
import { AbstractScrollPageableComponent } from "../../../../shared/AbstractScrollPageableComponent";
import { TweetResponseDTO } from "../../../../dto/TweetResponseDTO";
import { TweetHttpService } from "../../../../shared/http/tweet-http.service";
import { Observable } from "rxjs/Observable";
import { PageDTO } from "../../../../dto/PageDTO";

@Component({
  selector: 'app-wall-tweets',
  templateUrl: './wall-tweets.component.html',
  styleUrls: ['./wall-tweets.component.scss']
})
export class WallTweetsComponent extends AbstractScrollPageableComponent<TweetResponseDTO> implements OnInit {
  constructor(private tweetHttpService: TweetHttpService) {
    super();
  }

  ngOnInit() {
    super.getPage()
      .subscribe();
  }

  invokeGetPageMethod(...params: any[]): Observable<PageDTO<TweetResponseDTO>> {
    return this.tweetHttpService.getWall(...params);
  }
}
