import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserResponseDTO } from "../../../../dto/UserResponseDTO";
import { AbstractScrollPageableComponent } from "../../../../shared/AbstractScrollPageableComponent";
import { UserHttpService } from "../../../../shared/http/user-http.service";
import { ActivatedRoute } from "@angular/router";
import { Subscription } from "rxjs/Subscription";
import { Observable } from "rxjs/Observable";
import { PageDTO } from "../../../../dto/PageDTO";
import { AuthService } from "../../../../shared/services/auth.service";

@Component({
  selector: 'app-following',
  templateUrl: './following.component.html',
  styleUrls: [
    './following.component.scss',
    '../_tab-card.scss'
  ]
})
export class FollowingComponent extends AbstractScrollPageableComponent<UserResponseDTO> implements OnInit, OnDestroy {
  private username: string;
  private routeParamsSub: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private userHttpService: UserHttpService,
              public authService: AuthService) {
    super();
  }
  ngOnInit() {
    // when username path variable changes we want a brand new state
    this.routeParamsSub = this.activatedRoute.parent.params
      .map(params => params.username)
      .do(username => {
        this.username = username;
        this.itemsList = [];
      })
      .flatMap(username => this.getPage(username))
      .subscribe();
  }

  ngOnDestroy(): void {
    if (this.routeParamsSub) {
      this.routeParamsSub.unsubscribe();
    }
  }

  invokeGetPageMethod(...params: any[]): Observable<PageDTO<UserResponseDTO>> {
    return this.userHttpService.getFollowingUsers(...params);
  }

  public requestNextPage(): void {
    super.requestNextPage(this.username);
  }
}
