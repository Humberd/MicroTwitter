import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Subscription } from "rxjs/Subscription";
import { UserHttpService } from "../../../shared/http/user-http.service";
import { UserResponseDTO } from "../../../dto/UserResponseDTO";
import { Observable } from "rxjs/Observable";
import { Title } from "@angular/platform-browser";
import { CONSTANTS } from "../../../config/Constants";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy {
  private paramSub: Subscription;

  user: UserResponseDTO;

  constructor(private activatedRoute: ActivatedRoute,
              private userHttpService: UserHttpService,
              private title: Title) {
  }

  ngOnInit() {
    this.activatedRoute.params
      .map(params => params.username)
      .flatMap(username => this.getUser(username))
      .subscribe();
  }

  ngOnDestroy(): void {
    if (this.paramSub) {
      this.paramSub.unsubscribe();
    }
    this.title.setTitle(CONSTANTS.DEFAULT_PAGE_TITLE);
  }

  private getUser(username: string): Observable<UserResponseDTO> {
    return this.userHttpService.getUser(username)
      .do(user => this.user = user)
      .do(user => this.title.setTitle(`${user.profile.fullName} (@${user.username}) on ${CONSTANTS.APP_NAME}`));
  }

}
