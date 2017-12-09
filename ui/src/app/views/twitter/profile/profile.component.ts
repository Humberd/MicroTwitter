import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Subscription } from "rxjs/Subscription";
import { UserHttpService } from "../../../shared/http/user-http.service";
import { UserResponseDTO } from "../../../dto/UserResponseDTO";
import { Observable } from "rxjs/Observable";
import { Title } from "@angular/platform-browser";
import { CONSTANTS } from "../../../config/Constants";
import { AuthService } from "../../../shared/services/auth.service";
import { UpdateProfileFormComponent } from "../../../shared/components/update-profile-form/update-profile-form.component";
import { SnackBarService } from "../../../shared/services/snack-bar.service";
import { isString } from "util";
import { DynamicStylesService } from "../../../shared/services/dynamic-styles.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy {
  user: UserResponseDTO;
  isProfileEditing = false;
  private paramSub: Subscription;

  @ViewChild("updateProfileForm") updateProfileForm: UpdateProfileFormComponent;

  constructor(private activatedRoute: ActivatedRoute,
              private userHttpService: UserHttpService,
              public authService: AuthService,
              private snackBarService: SnackBarService,
              private dynamicStylesService: DynamicStylesService,
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
      .do(user => {
        this.user = user;
        this.title.setTitle(`${user.profile.fullName} (@${user.username}) on ${CONSTANTS.APP_NAME}`);
        this.dynamicStylesService.setUser(user);
      });
  }

  updateProfile(): void {
    this.updateProfileForm.updateProfile()
      .subscribe(newUser => {
        this.isProfileEditing = false;
        this.user = newUser;
        this.authService.requestUserDataUpdate();
        this.title.setTitle(`${newUser.profile.fullName} (@${newUser.username}) on ${CONSTANTS.APP_NAME}`);
        this.dynamicStylesService.setUser(newUser);
        this.snackBarService.showInfoSnackBar("Profile has been updated");
      }, error => {
        if (isString(error)) {
          this.snackBarService.showInfoSnackBar(error);
        } else {
          this.snackBarService.showInfoSnackBar("Cannot update form");
        }
      });
  }

}
