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

  updateProfile(): void {
    this.updateProfileForm.updateProfile()
      .subscribe(newUser => {
        this.isProfileEditing = false;
        this.user = newUser;
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
